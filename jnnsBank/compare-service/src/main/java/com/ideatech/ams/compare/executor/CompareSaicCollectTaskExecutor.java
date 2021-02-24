package com.ideatech.ams.compare.executor;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.compare.dto.CompareCollectTaskDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.processor.OnlineCollectionProcessor;
import com.ideatech.ams.compare.service.CompareCollectTaskService;
import com.ideatech.ams.compare.service.DataTransformation;
import com.ideatech.ams.compare.vo.CompareCollectRecordVo;
import com.ideatech.ams.customer.dto.SaicMonitorDto;
import com.ideatech.ams.customer.enums.SaicMonitorEnum;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.kyc.dto.SaicInfoDto;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.kyc.service.SaicRequestService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.common.util.SecurityUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * 工商数据采集线程
 * 
 * @author Administrator
 *
 */
@Data
@Slf4j
public class CompareSaicCollectTaskExecutor implements Callable {

	public CompareSaicCollectTaskExecutor(Set<CompareCollectRecordVo> tokens) {
		this.tokens = tokens;
	}

	private OnlineCollectionProcessor onlineCollectionProcessor;

	private CompareCollectTaskService compareCollectTaskService;

	private TransactionUtils transactionUtils;

	private SaicRequestService saicRequestService;

	private SaicInfoService saicInfoService;

	private PlatformTransactionManager transactionManager;

	private DataTransformation dataTransformation;

	private DataSourceDto dataSourceDto;

	private int retryLimit;

	private Set<CompareCollectRecordVo> tokens;

	private String batch;

	private Long compareTaskId;

	private Long collectTaskId;

	private int processedNum;

	private int successNum;

	private int successPartNum;

	private int ignoreNum;

	private int ignorePartNum;

	private int totalSize;

    private boolean saicLocal;

    private SaicMonitorService saicMonitorService;

    private UserDto userDto;


	@Override
	public Object call() throws Exception {
		if (tokens != null && tokens.size() > 0) {
			totalSize= tokens.size();
			try {
				log.info(batch + "--总共需要采集的工商数据量为:"+tokens.size());
				collectData(1,0);
			} catch (Exception e) {
				log.error("工商采集异常", e);
			}
		}
		return System.currentTimeMillis();
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void collectData(int retry,int failCount) throws Exception {
		log.info(batch + "--开始采集工商数据，第" + retry + "次采集任务开始,需要采集的工商数据量为："+tokens.size()+"......");

		processedNum = 0;
		successNum = 0;
		successPartNum = 0;
		ignoreNum = 0;
		ignorePartNum = -failCount;

		TransactionDefinition definition = new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus transaction = null;

		Iterator<CompareCollectRecordVo> iterator = tokens.iterator();
		int index=0;
		while(iterator.hasNext()){
			index++;
			CompareCollectRecordVo token = iterator.next();
			boolean collectFlag = false;//采集结果标志位
			transaction = transactionManager.getTransaction(definition);
			try{
				collectFlag = collectOneData(token);
				transactionManager.commit(transaction);
				if(collectFlag){
					iterator.remove();
					successNum++;
					successPartNum++;
				}else{
					ignoreNum++;
					ignorePartNum++;
				}
			}catch(Exception e){
				log.error(batch + "--保存工商数据["+token+"]异常,进行回滚", e);
				ignoreNum++;
				ignorePartNum++;
				if(!transaction.isCompleted()){
					try{
						transactionManager.rollback(transaction);
					}catch (Exception e1){
						log.error(batch + "--回滚工商数据["+token+"]异常", e);
					}
				}
				updateFailSaicRecord(token);
			}

			if(successPartNum + ignorePartNum >20){
				updateProcessed();
				successPartNum=0;
				ignorePartNum=0;
			}
		}
		updateProcessed();

		log.info(batch + "--第" + retry + "次采集信息完成，共处理" + processedNum + "条，其中，成功" + successNum + "条,失败或者空白记录"+ignoreNum
				+"条,当前成功率是：" + (successNum * 1.0 / processedNum) * 100 + "%");
		try {

			if (retry < retryLimit && (0 != tokens.size())) {
				// 多次采集
				collectData(retry + 1,ignoreNum);
			} else {
				log.info(batch + "--采集工商数据" + retry + "次结束");
				//结束时更新任务处理数量
				updateFinalProcessed();
				tokens.clear();
				return;
			}
		} catch (InterruptedException e) {
			log.error(batch+"--重新采集数据异常", e);
		}

	}

	/**
	 * 更新任务表数量
	 */
	private  void updateProcessed() {
		synchronized (CompareSaicCollectTaskExecutor.class){
			try{
				transactionUtils.executeInNewTransaction(new TransactionCallback() {
					@Override
					public void execute() throws Exception {
						CompareCollectTaskDto compareCollectTaskDto = compareCollectTaskService.findById(collectTaskId);
						compareCollectTaskDto.setSuccessed((compareCollectTaskDto.getSuccessed()+successPartNum) > compareCollectTaskDto.getCount() ? compareCollectTaskDto.getCount() : compareCollectTaskDto.getSuccessed()+successPartNum);
						compareCollectTaskDto.setFailed(compareCollectTaskDto.getFailed()+ignorePartNum);
						log.info("这次成功的数量为:"+successPartNum+",失败的数量为:"+ignorePartNum);
						log.info("这次成功后的数量为:"+compareCollectTaskDto.getSuccessed()+",失败后的数量为:"+compareCollectTaskDto.getFailed());
						compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);
					}
				});
			}catch (Exception e){
				log.error("更新成功和失败数量失败");
			}
		}
	}

	/**
	 * 更新任务表数量
	 */
	private  void updateFinalProcessed() {
		synchronized (CompareSaicCollectTaskExecutor.class){
			try{
				transactionUtils.executeInNewTransaction(new TransactionCallback() {
					@Override
					public void execute() throws Exception {
						CompareCollectTaskDto compareCollectTaskDto = compareCollectTaskService.findById(collectTaskId);
						if(compareCollectTaskDto.getProcessed()+totalSize < compareCollectTaskDto.getCount()){
							compareCollectTaskDto.setProcessed(compareCollectTaskDto.getProcessed()+totalSize);
						}
						log.info("最终处理的数量为:"+compareCollectTaskDto.getProcessed());
						compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);
					}
				});
			}catch (Exception e){
				log.error("更新处理数量失败");
			}
		}
	}


	/*
	 * 保存失败--最有可能是字段长度问题
	 */
	private void updateFailSaicRecord(CompareCollectRecordVo token) {
		try{
			CompareDataDto compareDataDto = new CompareDataDto();
			compareDataDto.setAcctNo(token.getAcctNo());
			compareDataDto.setDepositorName(token.getDepositorName());
			compareDataDto.setRegNo(token.getRegNo());
			compareDataDto.setTaskId(compareTaskId);
			onlineCollectionProcessor.saveFailedRecord(compareDataDto,collectTaskId,"工商数据保存异常",dataSourceDto,Propagation.REQUIRES_NEW);
		}catch (Exception e){
			log.error("工商数据保存异常，记录保存异常，进行回滚",e);
		}
	}


	/**
	 * 采集单条工商数据
	 * @param token
	 * @return
	 * @throws Exception
	 */
	private boolean collectOneData(CompareCollectRecordVo token) throws Exception {
		boolean result = false;
		CompareDataDto compareDataDto = new CompareDataDto();
		compareDataDto.setAcctNo(token.getAcctNo());
		compareDataDto.setTaskId(compareTaskId);
		compareDataDto.setOrganCode(token.getOrganCode());
		compareDataDto.setOrganFullId(token.getOrganFullId());
		try {
			Long startTime = System.currentTimeMillis();
			log.info(batch + "--客户信息采集单条数据开始>>>>>>>>>>>>>>");
            SaicIdpInfo saicInfoBaseLocal=null;
			if(saicLocal){
				if(StringUtils.isNotBlank(token.getDepositorName())){//先取本地的存款人
					saicInfoBaseLocal = saicInfoService.getSaicInfoBaseLocalJustSaic(token.getDepositorName());
				}

				if(StringUtils.isNotBlank(token.getRegNo()) && saicInfoBaseLocal == null){//取本地工商注册号
					saicInfoBaseLocal = saicInfoService.getSaicInfoBaseLocalJustSaic(token.getRegNo());
				}
            }
            String idpJsonStr="";
            if(saicInfoBaseLocal == null){
				if(StringUtils.isNotBlank(token.getDepositorName())){//先取存款人
					idpJsonStr = saicRequestService.getSaicInfoExactJson(token.getDepositorName());
				}
                if(StringUtils.isBlank(idpJsonStr) && StringUtils.isNotBlank(token.getRegNo())){//判断用企业名称采集是否为null && 注册工商号不为空
                    log.info(batch + "--客户信息采集单条数据的企业名称为{}使用注册工商号({})采集--",token,token.getRegNo());
                    idpJsonStr = saicRequestService.getSaicInfoExactJson(token.getRegNo());
                }
                log.info(batch + "--客户信息采集单条数据使用远程数据--");
            }else{
                log.info(batch + "--客户信息采集单条数据使用在时效内的数据--");
            }
			Long endTime = System.currentTimeMillis();

			log.info(batch + "--客户信息采集单条数据结束，耗时{}>>>>>>>>>>>>>>", endTime - startTime);
			SaicIdpInfo saicIdpInfo = null;
			if(saicInfoBaseLocal !=null || StringUtils.isNotBlank(idpJsonStr)){
				if(saicInfoBaseLocal !=null){//取本地
					saicIdpInfo = saicInfoBaseLocal;
				}else{//远程调用
					saicIdpInfo = JSON.parseObject(idpJsonStr, SaicIdpInfo.class);
					if(!StringUtils.equals(token.getDepositorName(),saicIdpInfo.getName())){//判断企业名称是不是一致
						log.error("采集的企业名称不一致，企业名称为：{}，采集到的企业名称为：{}，采集到的工商注册号为：{}",token,saicIdpInfo.getName(),saicIdpInfo.getUnitycreditcode());
					}
					String url=saicRequestService.getSaicInfoExactUrl();
					String username = SecurityUtils.getCurrentUsername();
					String orgfullid = SecurityUtils.getCurrentOrgFullId();
					saicInfoService.inesrtSaicInfoByType(username,saicIdpInfo,url,SearchType.EXACT,orgfullid);
					compareDataDto.setDepositorName(saicIdpInfo.getName());
					compareDataDto.setRegNo(saicIdpInfo.getRegistno());
				}
				result = true;
				compareDataDto.setDepositorName(saicIdpInfo.getName());
				compareDataDto.setRegNo(saicIdpInfo.getRegistno());
				SaicInfoDto saicInfoDto = new SaicInfoDto();
				BeanUtils.copyProperties(saicIdpInfo,saicInfoDto);
				dataTransformation.dataTransformation(compareDataDto,saicInfoDto);
				onlineCollectionProcessor.saveSuccessCombine(compareDataDto,collectTaskId,dataSourceDto,Propagation.REQUIRED);

				//工商年检查询记录到统计表中
				log.info(token.getDepositorName() + "查询成功，比对查询工商保存工商统计表");
				if(saicIdpInfo != null){
					SaicMonitorDto saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(), userDto.getOrgId(),token.getDepositorName(),saicIdpInfo.getId(),StringUtils.isNotBlank(saicIdpInfo.getUnitycreditcode()) ? saicIdpInfo.getUnitycreditcode() : saicIdpInfo.getRegistno(), SaicMonitorEnum.ANNUAL);
					saicMonitorService.save(saicMonitorDto);
				}

			}else{
				result = false;
				if(StringUtils.isNotBlank(token.getDepositorName())){
					compareDataDto.setDepositorName(token.getDepositorName());
				}
				if(StringUtils.isNotBlank(token.getRegNo())){
					compareDataDto.setRegNo(token.getRegNo());
				}
				onlineCollectionProcessor.saveFailedRecord(compareDataDto,collectTaskId,"无对应的工商信息",dataSourceDto,Propagation.REQUIRED);
				//工商年检查询记录到统计表中
				log.info(token.getDepositorName() + "查询失败，比对查询工商保存工商统计表");
				SaicMonitorDto saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(), userDto.getOrgId(),token.getDepositorName(),null,"", SaicMonitorEnum.ANNUAL);
				saicMonitorService.save(saicMonitorDto);
			}
		} catch (Exception e) {
			log.error(batch + "--采集企业名称为" + token + "工商信息异常：", e);
			compareDataDto.setDepositorName(token.getDepositorName());
			compareDataDto.setRegNo(token.getRegNo());
			onlineCollectionProcessor.saveFailedRecord(compareDataDto,collectTaskId,"采集工商信息错误原因：" + StringUtils.substring(e.getMessage(),0,1900),dataSourceDto,Propagation.REQUIRED);
			result = false;
		}
		processedNum++;
		return result;
	}

}

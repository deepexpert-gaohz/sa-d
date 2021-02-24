package com.ideatech.ams.annual.executor;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.annual.dao.CollectTaskDao;
import com.ideatech.ams.annual.dto.CollectConfigDto;
import com.ideatech.ams.annual.dto.FetchSaicInfoDto;
import com.ideatech.ams.annual.entity.CollectTask;
import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.ams.annual.enums.CollectType;
import com.ideatech.ams.annual.service.AnnualResultService;
import com.ideatech.ams.annual.service.FetchSaicInfoService;
import com.ideatech.ams.customer.dto.SaicMonitorDto;
import com.ideatech.ams.customer.enums.SaicMonitorEnum;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.kyc.service.SaicRequestService;
import com.ideatech.ams.kyc.service.SaicSearchHistoryService;
import com.ideatech.ams.pbc.utils.NumberUtils;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.proof.dto.ProofReportDto;
import com.ideatech.ams.system.proof.enums.ProofType;
import com.ideatech.ams.system.proof.service.ProofReportService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.util.BeanUtil;
import com.ideatech.common.util.SecurityUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Iterator;
import java.util.Map;
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
public class SaicCollectTaskExecutor implements Callable {

	public SaicCollectTaskExecutor(Set<String> tokens) {
		this.tokens = tokens;
	}

//	private static final String IDP_STATE_SUCCESS = "success";

	private int retryLimit;

	private TransactionUtils transactionUtils;

	private Set<String> tokens;

	private String batch;

	private SaicRequestService saicRequestService;

	private SaicInfoService saicInfoService;

	private SaicSearchHistoryService saicSearchHistoryService;

	private FetchSaicInfoService fetchSaicInfoService;

	private CollectTaskDao collectTaskDao;

	private PlatformTransactionManager transactionManager;

	private CollectType collectType;

	private Long collectTaskId;

	private Map<String, Object> existSaicMap;

	private Map<String, Object> fetchSaicMap;

	private int processedNum;

	private int successNum;

	private int successPartNum;

	private int ignoreNum;

	private int ignorePartNum;

	private Long annualTaskId;

	private boolean saicCollectPause;

	private CollectConfigDto collectConfig;

	private AnnualResultService annualResultService;

	private int totalSize;

	private Map<String, Integer> strLengthMap;

    private Boolean saicLocal;

    private SaicMonitorService saicMonitorService;

    private UserService userService;

    private ConfigService configService;

    private ProofReportService proofReportService;

	private boolean writeMoney;

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

		Iterator<String> iterator = tokens.iterator();
		int index=0;
		while(iterator.hasNext()){
			index++;
			String token = iterator.next();
			Object obj =fetchSaicMap.get(token);
			if(obj == null){
				ignoreNum++;
				ignorePartNum++;
				processedNum++;
				iterator.remove();
				continue;
			}
			boolean collectFlag = false;//采集结果标志位
			transaction = transactionManager.getTransaction(definition);
			FetchSaicInfoDto fetchSaicInfo = (FetchSaicInfoDto) obj;
			try{
				collectFlag = collectOneData(token,fetchSaicInfo);
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
				updateFailSaicRecord(fetchSaicInfo);
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
		synchronized (SaicCollectTaskExecutor.class){
			TransactionDefinition definition = new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus transaction = transactionManager.getTransaction(definition);
			CollectTask collectTaskNew = collectTaskDao.findOne(collectTaskId);
			collectTaskNew.setSuccessed(collectTaskNew.getSuccessed()+successPartNum);
			collectTaskNew.setFailed(collectTaskNew.getFailed()+ignorePartNum);
			collectTaskDao.save(collectTaskNew);
			transactionManager.commit(transaction);
		}
	}

	/**
	 * 更新任务表数量
	 */
	private  void updateFinalProcessed() {
		synchronized (SaicCollectTaskExecutor.class){
			TransactionDefinition definition = new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus transaction = transactionManager.getTransaction(definition);
			CollectTask collectTaskNew = collectTaskDao.findOne(collectTaskId);
			if(collectTaskNew.getProcessed()+totalSize < collectTaskNew.getCount()){
				collectTaskNew.setProcessed(collectTaskNew.getProcessed()+totalSize);
			}
			collectTaskDao.save(collectTaskNew);
			transactionManager.commit(transaction);
		}
	}


	/*
	 * 保存失败--最有可能是字段长度问题
	 */
	private  void updateFailSaicRecord(FetchSaicInfoDto fetchSaicInfo) {
		TransactionDefinition definition = new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus transaction = null;
		try{
			transaction = transactionManager.getTransaction(definition);
			BeanUtil.checkStringLength(fetchSaicInfo, strLengthMap);
			fetchSaicInfo.setCollectState(CollectState.fail);
			fetchSaicInfo.setFailReason("工商数据保存异常");
			fetchSaicInfo.setAnnualTaskId(annualTaskId);
			fetchSaicInfoService.save(fetchSaicInfo);
			log.info("工商数据保存异常，进行记录保存");
			transactionManager.commit(transaction);
		}catch (Exception e){
			log.error("工商数据保存异常，记录保存异常，进行回滚",e);
			if(!transaction.isCompleted()){
				try{
					transactionManager.rollback(transaction);
				}catch (Exception e1){
					log.error("工商数据保存异常，记录保存异常，回滚失败",e);
				}
			}
		}
	}


	private boolean collectOneData(String token,FetchSaicInfoDto fetchSaicInfo) {
		boolean result = false;
		try {
			Long startTime = System.currentTimeMillis();
			log.info(batch + "--客户信息采集单条数据开始>>>>>>>>>>>>>>");
            SaicIdpInfo saicInfoBaseLocal=null;
			if(saicLocal){
                saicInfoBaseLocal = saicInfoService.getSaicInfoBaseLocalJustSaic(token);
            }
            String idpJsonStr="";
            if(saicInfoBaseLocal == null){
                idpJsonStr = saicRequestService.getSaicInfoExactJson(token);
                if(StringUtils.isBlank(idpJsonStr) && StringUtils.isNotBlank(fetchSaicInfo.getRegNo())){//判断用企业名称采集是否为null && 注册工商号不为空
                    log.info(batch + "--客户信息采集单条数据的企业名称为{}使用注册工商号({})采集--",token,fetchSaicInfo.getRegNo());
                    idpJsonStr = saicRequestService.getSaicInfoExactJson(fetchSaicInfo.getRegNo());
                }
                log.info(batch + "--客户信息采集单条数据使用远程数据--");
            }else{
                log.info(batch + "--客户信息采集单条数据使用在时效内的数据--");
            }
            //青海统计费用，并且本地查询无，使用IDP存量查询接口查回数据时进行保存
            if(writeMoney && saicInfoBaseLocal == null && StringUtils.isNotBlank(idpJsonStr)){
				ProofReportDto accountProofReportDto = new ProofReportDto();
				accountProofReportDto.setTypeDetil("年检工商存量查询...");
				accountProofReportDto.setOrganFullId("1");
				accountProofReportDto.setUsername("");
				accountProofReportDto.setProofBankName("");
				ConfigDto configDto = configService.findOneByConfigKey("saicMoney");
				if(configDto!=null){
					accountProofReportDto.setPrice(configDto.getConfigValue());
				}else{
					accountProofReportDto.setPrice("0");
				}
				accountProofReportDto.setType(ProofType.SAIC);
				accountProofReportDto.setEntname(token);
				accountProofReportDto.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
				proofReportService.save(accountProofReportDto);
			}
//            String idpJsonStr = saicRequestService.getSaicInfoExactJson(token);
//			String url = saicRequestService.getSaicInfoExactUrl();
			Long endTime = System.currentTimeMillis();

			log.info(batch + "--客户信息采集单条数据结束，耗时{}>>>>>>>>>>>>>>", endTime - startTime);
			UserDto userDto = userService.findById(2L);
			if (saicInfoBaseLocal != null || StringUtils.isNotBlank(idpJsonStr)) {
                String newIdpJsonStr = null;
                SaicIdpInfo saicIdpInfo = null;
			    if(saicInfoBaseLocal !=null){//使用本地数据
                    saicIdpInfo = saicInfoBaseLocal;
                    if(StringUtils.isNotBlank(saicIdpInfo.getRegistfund())){//资金转化
                        saicIdpInfo.setRegistfund(NumberUtils.formatCapital(NumberUtils.convertCapital(saicIdpInfo.getRegistfund())).toString());
                    }
                    newIdpJsonStr = JSON.toJSONString(saicIdpInfo);
                }else{//使用远程数据
//                    newIdpJsonStr = idpJsonStr;
                    saicIdpInfo = JSON.parseObject(idpJsonStr, SaicIdpInfo.class);
                    if(!StringUtils.equals(token,saicIdpInfo.getName())){//判断企业名称是不是一致
                    	log.error("采集的企业名称不一致，企业名称为：{}，采集到的企业名称为：{}，采集到的工商注册号为：{}",token,saicIdpInfo.getName(),saicIdpInfo.getUnitycreditcode());
					}
                    if(StringUtils.isNotBlank(saicIdpInfo.getRegistfund())){//资金转化
                        saicIdpInfo.setRegistfund(NumberUtils.formatCapital(NumberUtils.convertCapital(saicIdpInfo.getRegistfund())).toString());
					}
					newIdpJsonStr = JSON.toJSONString(saicIdpInfo);
					String url=saicRequestService.getSaicInfoExactUrl();
                    String username = SecurityUtils.getCurrentUsername();
                    String orgfullid = SecurityUtils.getCurrentOrgFullId();
                    saicInfoService.inesrtSaicInfoByType(username,saicIdpInfo,url,SearchType.EXACT,orgfullid);
                }

                //工商年检查询记录到统计表中
				log.info(token + "查询成功，年检查询工商保存工商统计表");
                if(saicIdpInfo != null){
					SaicMonitorDto saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(),userDto.getOrgId(),token,saicIdpInfo.getId(),StringUtils.isNotBlank(saicIdpInfo.getUnitycreditcode()) ? saicIdpInfo.getUnitycreditcode() : saicIdpInfo.getRegistno(),SaicMonitorEnum.ANNUAL);
					saicMonitorService.save(saicMonitorDto);
				}
				fetchSaicInfo.setEnddate(saicIdpInfo.getEnddate());
				fetchSaicInfo.setState(saicIdpInfo.getState());
				fetchSaicInfo.setUnitycreditcode(saicIdpInfo.getUnitycreditcode());
				fetchSaicInfo.setCollectState(CollectState.success);
				fetchSaicInfo.setFailReason(null);
				fetchSaicInfo.setIdpJsonStr(newIdpJsonStr);
				result = true;
			} else {
				//工商年检查询记录到统计表中
				log.info(token + "查询成功，年检查询工商保存工商统计表");
				SaicMonitorDto saicMonitorDto = saicMonitorService.getSaicMonitor(userDto.getUsername(),userDto.getOrgId(),token,null,"", SaicMonitorEnum.ANNUAL);
				saicMonitorService.save(saicMonitorDto);

				fetchSaicInfo.setCollectState(CollectState.fail);
				fetchSaicInfo.setFailReason("无对应的工商信息");
				result = false;
			}
		} catch (Exception e) {
			log.error(batch + "--采集企业名称为" + token + "工商信息异常：", e);
			fetchSaicInfo.setCollectState(CollectState.fail);
			fetchSaicInfo.setFailReason("采集工商信息错误原因：" + e.getMessage());
			result = false;
		}
		fetchSaicInfo.setAnnualTaskId(annualTaskId);
		fetchSaicInfoService.save(fetchSaicInfo);
		FetchSaicInfoDto fetchSaicInfoDto = new FetchSaicInfoDto();
		BeanUtils.copyProperties(fetchSaicInfo,fetchSaicInfoDto);
		annualResultService.updateAnnualResultData(annualTaskId,fetchSaicInfoDto);
		processedNum++;
		return result;
	}

}

package com.ideatech.ams.annual.executor;

import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.annual.dao.CollectTaskDao;
import com.ideatech.ams.annual.dao.FetchPbcDao;
import com.ideatech.ams.annual.dto.CollectConfigDto;
import com.ideatech.ams.annual.dto.FetchPbcInfoDto;
import com.ideatech.ams.annual.dto.PbcCollectAccountDto;
import com.ideatech.ams.annual.entity.CollectTask;
import com.ideatech.ams.annual.entity.FetchPbcInfo;
import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.ams.annual.enums.CollectTaskState;
import com.ideatech.ams.annual.service.AnnualResultService;
import com.ideatech.ams.annual.service.PbcCollectAccountService;
import com.ideatech.ams.annual.service.PbcCollectionServiceImpl;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.AtomicLongUtils;
import com.ideatech.ams.pbc.utils.NumberUtils;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.ExceptionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 抓取人行账管数据定时任务
 * 
 * @author wanghongjie
 *
 */
@Data
@Slf4j
public class PbcCollectTaskExecutor implements Callable {

	private List<PbcCollectAccountDto> accountList = new ArrayList<PbcCollectAccountDto>();

	private OrganizationDto organizationDto;

	private PbcAmsService pbcAmsService;

	private PbcCollectAccountService pbcCollectAccountService;

	private PbcAccountService pbcAccountService;

	protected TransactionUtils transactionUtils;

	private FetchPbcDao fetchPbcDao;

	private AnnualResultService annualResultService;

	private boolean pbcCollectPause;

	private CollectConfigDto collectConfig;

	private CollectTaskDao collectTaskDao;

	private Long annualTaskId;

	private Long collectTaskId;

	private Boolean countTypeFlag;

	private PlatformTransactionManager transactionManager;

	public PbcCollectTaskExecutor(List<PbcCollectAccountDto> amsCollectAccount) {
		this.accountList = amsCollectAccount;
	}

	private int successedNumber=0;

	private int failedNumber=0;

    //是否启用人行采集限制机制
    private Boolean pbcCollectionLimitUse;

    //人行采集数量控制
    private Long pbcCollectionLimitNum;

	@Override
	public Object call() throws Exception {
		try {
			mainAccess();
		} catch (Exception e) {
			log.error("[" + organizationDto.getName() + "]采集异常", e);
		}
		return System.currentTimeMillis();
	}


	/**
	 * 采集并保存账管数据方法入口
	 * 
	 * @throws Exception
	 */
	protected void mainAccess() throws Exception {
		if (accountList != null && accountList.size() > 0) {
			//用以存放需要暂停的数据
			List<PbcCollectAccountDto> pauseList = new ArrayList<PbcCollectAccountDto>();
			for (PbcCollectAccountDto pbcCollectAccountDto : accountList) {
				log.info("开始采集[" + organizationDto.getName() + ","+organizationDto.getCode()+"]账号:" + pbcCollectAccountDto.getAcctNo() + "的人行账管信息");
				// 2017年8月22日19:12:40 添加暂停
				if (PbcCollectionServiceImpl.manualPause || pbcCollectPause) {
					pauseList.add(pbcCollectAccountDto);
					continue;
				}
				// 根据账号获取人行信息并保存到数据库中
				getAmsAccountFromAms(pbcCollectAccountDto, 0,null);
			}

			for (PbcCollectAccountDto pbcCollectAccountDto : pauseList) {
				log.info("开始采集暂停集合[" + organizationDto.getName() + ","+organizationDto.getCode()+"]账号:" + pbcCollectAccountDto.getAcctNo() + "的人行账管信息");
				// 2017年8月22日19:12:40 添加暂停
				manualPause();
				// 根据账号获取人行信息并保存到数据库中
				getAmsAccountFromAms(pbcCollectAccountDto, 0,null);
			}
			//结束时更新任务处理数量
			updateProcessed();
		}
	}


	private void updateProcessed() throws Exception {
		synchronized (PbcCollectTaskExecutor.class){
			TransactionDefinition definition = new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus transaction = transactionManager.getTransaction(definition);

			CollectTask collectTask = collectTaskDao.findById(collectTaskId);
//			collectTask.setProcessed(collectTask.getProcessed()+accountList.size());
			collectTask.setSuccessed((collectTask.getSuccessed()+successedNumber) > collectTask.getCount() ? collectTask.getCount() : collectTask.getSuccessed()+successedNumber);
			collectTask.setFailed((collectTask.getFailed()+failedNumber) <0 ? 0 : collectTask.getFailed()+failedNumber);
			log.info("成功的数量为:"+successedNumber+",失败的数量为:"+failedNumber);
			log.info("成功的数量最终为:"+collectTask.getSuccessed()+",失败的数量最终为:"+collectTask.getFailed());
			collectTaskDao.save(collectTask);
			transactionManager.commit(transaction);
		}
	}


	private void manualPause() {
		while(PbcCollectionServiceImpl.manualPause||isInInvalidTime()) {
			log.info("手动暂停采集人行数据, 10分钟后重试");

			try {
				TimeUnit.MINUTES.sleep(10L);
			} catch (InterruptedException var1) {
				log.error("线程暂停异常", var1);
			}
		}

	}

	//在当前设置的时间
	private boolean isInInvalidTime(){
		/*if(collectConfig ==null){
			return false;
		}else{
			return !DateUtils.isInInvalidTime(collectConfig.getPbcStartTime(),collectConfig.getPbcEndTime());
		}*/
		if(collectConfig ==null){
			if(isWorkDay()){
				//判断当前是否是周六日日，如果是，则设置成采集时间为全天采集
				log.info("当前是周六日，设置采集时间为全天采集------");
				return !DateUtils.isInInvalidTime("00:00","23:59");
			}else{
				log.info("当前不是周六日，设置成页面配置的采集时间采集-----");
				return !DateUtils.isInInvalidTime(collectConfig.getPbcStartTime(),collectConfig.getPbcEndTime());
			}
		}else{
			return false;
		}
	}


	private boolean isWorkDay(){
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
		String str= formatter.format(new Date());
		if(str.contains("六") || str.contains("日") || str.contains("天")){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 从人行账管系统获取账户的客户信息
	 * 
	 * @param pbcCollectAccountDto
	 *            账号
	 * @param tryNum
	 *            递归尝试次数 ，第一次调用为0
	 * @throws Exception
	 */
	private void getAmsAccountFromAms(PbcCollectAccountDto pbcCollectAccountDto, int tryNum,Exception ex) throws Exception {
		if (tryNum > 5) {// 递归出口
			if(countTypeFlag){//首次采集和excel采集
				failedNumber++;
			}
			if(ex !=null && ex instanceof SyncException){
				updateCollectAccountByFail(pbcCollectAccountDto, ex.getMessage());
				return;
			}
			updateCollectAccountByFail(pbcCollectAccountDto, "人行采集失败");
			return;
		}
		try {
			AmsAccountInfo accountInfo = null;
			if(pbcCollectionLimitUse && !AtomicLongUtils.isPause(pbcCollectionLimitNum)){
				log.info("方法：线程人行查询数量：" + AtomicLongUtils.al.getAndIncrement());
				accountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organizationDto.getCode(),pbcCollectAccountDto.getAcctNo());//获取人行数据
			}else{
				accountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organizationDto.getCode(),pbcCollectAccountDto.getAcctNo());
			}
			if (accountInfo != null && StringUtils.isNotBlank(accountInfo.getDepositorName())) {
				save(accountInfo, pbcCollectAccountDto);
				log.info("账号[" + organizationDto.getName() + ","+organizationDto.getCode()+"," + pbcCollectAccountDto.getAcctNo() + "]:第"+(tryNum+1)+"次采集成功");
			}
		} catch (Exception e) {
			log.error("账号[" + organizationDto.getName() + ","+organizationDto.getCode()+"," + pbcCollectAccountDto.getAcctNo() + "]:第"+(tryNum+1)+"次采集失败", e);
			processException(e, pbcCollectAccountDto, tryNum);
		}
	}

	/**
	 * 处理采集时出现的异常情况
	 * 
	 * @param e
	 * @param pbcCollectAccountDto
	 * @param tryNum
	 * @throws Exception
	 */
	private void processException(Exception e, PbcCollectAccountDto pbcCollectAccountDto, int tryNum) throws Exception {
		sleep(1);// 暂停1分钟
		String exceptionMsg = e.getMessage();
//		updateCollectAccountByFail(pbcCollectAccountDto, e);
		if (ExceptionUtils.isNetExption(e)) {
			getAmsAccountFromAms(pbcCollectAccountDto, tryNum + 1,e);
		} else if (StringUtils.isNotBlank(exceptionMsg) && exceptionMsg.contains("服务已关闭")) {
			updateCollectTaskProcessed(collectTaskId, CollectTaskState.waitToOpen);
            sleep(30);// 暂停30分钟后 重复调用
			updateCollectTaskProcessed(collectTaskId,CollectTaskState.collecting);
			getAmsAccountFromAms(pbcCollectAccountDto, 1,e);
		} else if (StringUtils.isNotBlank(exceptionMsg) && exceptionMsg.contains("用户登录信息失效")) {
			getAmsAccountFromAms(pbcCollectAccountDto, tryNum + 1,e);
		} else {
			getAmsAccountFromAms(pbcCollectAccountDto, tryNum + 1,e);
		}
	}

	/**
	 * 采集失败，更新采集账户列表
	 * 
	 * @param pbcCollectAccountDto
	 * @param errorMsg
	 * @throws Exception
	 */
	private void updateCollectAccountByFail(final PbcCollectAccountDto pbcCollectAccountDto, final String errorMsg)
			throws Exception {
		transactionUtils.executeInNewTransaction(new TransactionCallback() {
			@Override
			public void execute() throws Exception {
				pbcCollectAccountDto.setCollectState(CollectState.fail);
				pbcCollectAccountDto.setParDate("");
				pbcCollectAccountDto.setParErrorMsg(errorMsg);
				pbcCollectAccountService.save(pbcCollectAccountDto);
			}
		});
	}

	/**
	 * 保存人行账管账户信息
	 * 
	 * @param accountInfo
	 *            从人行采集到的账户信息
	 * @param collectAccountDto
	 * @throws Exception
	 */
	protected void save(final AmsAccountInfo accountInfo, final PbcCollectAccountDto collectAccountDto) throws Exception {
		transactionUtils.executeInNewTransaction(new TransactionCallback() {
			@Override
			public void execute() throws Exception {
				// 人行账管账户信息
				FetchPbcInfo dataAccount = new FetchPbcInfo();
				BeanUtils.copyProperties(accountInfo, dataAccount);
				dataAccount.setOrganFullId(organizationDto.getFullId());
				dataAccount.setAccountStatus(accountInfo.getAccountStatus());
				dataAccount.setParDate(DateUtils.getNowDateShort("yyyy-MM-dd hh:mm:ss"));
				dataAccount.setIndusRegArea(dataAccount.getRegAddress());
				dataAccount.setAnnualTaskId(annualTaskId);
				dataAccount.setCollectTaskId(collectTaskId);
				dataAccount.setCollectAccountId(collectAccountDto.getId());
				if(StringUtils.isNotBlank(dataAccount.getRegisteredCapital())){
					BigDecimal bigDecimal = NumberUtils.formatCapital(NumberUtils.convertCapital(dataAccount.getRegisteredCapital()));
					dataAccount.setRegisteredCapital(bigDecimal.toString());
				}
				fetchPbcDao.deleteByAcctNo(accountInfo.getAcctNo());
				fetchPbcDao.save(dataAccount);
				// 账户采集信息
				collectAccountDto.setCollectState(CollectState.success);
				collectAccountDto.setParDate(DateUtils.getNowDateShort("yyyy-MM-dd hh:mm:ss"));
				collectAccountDto.setParErrorMsg("");
				pbcCollectAccountService.save(collectAccountDto);

				//比对结果表
				FetchPbcInfoDto fetchPbcInfoDto = new FetchPbcInfoDto();
				BeanUtils.copyProperties(dataAccount,fetchPbcInfoDto);
				annualResultService.updateAnnualResultData(annualTaskId,fetchPbcInfoDto);
				successedNumber++;
				if(!countTypeFlag){//失败的人行数据重新采集
					failedNumber--;
				}
			}
		});
	}

	/**
	 * 暂停
	 * 
	 * @param min
	 */
	private void sleep(int min) {
		try {
			TimeUnit.MINUTES.sleep(min);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
     * 更新任务状态
     * @param collectTaskId
     * @param collectStatus
     */
    private void updateCollectTaskProcessed(Long collectTaskId,CollectTaskState collectStatus){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CollectTask collectTask = collectTaskDao.findById(collectTaskId);
        collectTask.setCollectStatus(collectStatus);
        collectTaskDao.save(collectTask);
        transactionManager.commit(transaction);

    }
}

package com.ideatech.ams.compare.executor;

import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.compare.dto.CompareCollectRecordDto;
import com.ideatech.ams.compare.dto.CompareCollectTaskDto;
import com.ideatech.ams.compare.dto.ComparePbcInfoDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.enums.CollectState;
import com.ideatech.ams.compare.enums.CollectTaskState;
import com.ideatech.ams.compare.processor.OnlineCollectionProcessor;
import com.ideatech.ams.compare.service.CompareCollectRecordService;
import com.ideatech.ams.compare.service.CompareCollectTaskService;
import com.ideatech.ams.compare.service.ComparePbcInfoService;
import com.ideatech.ams.compare.service.DataTransformation;
import com.ideatech.ams.compare.vo.CompareCollectRecordVo;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.AtomicLongUtils;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.ExceptionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Propagation;

import java.util.HashSet;
import java.util.Set;
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
public class ComparePbcCollectTaskExecutor implements Callable {

	private Set<CompareCollectRecordVo> accountList = new HashSet<CompareCollectRecordVo>();

	private AccountsAllService accountsAllService;

	private OrganizationService organizationService;

	private CompareCollectRecordService compareCollectRecordService;

	private CompareCollectTaskService compareCollectTaskService;

	private OnlineCollectionProcessor onlineCollectionProcessor;

	private ComparePbcInfoService comparePbcInfoService;

	private DataTransformation dataTransformation;

	private Long compareTaskId;

	private Long collectTaskId;

	private DataSourceDto dataSourceDto;

	private PbcAmsService pbcAmsService;

	protected TransactionUtils transactionUtils;

	private boolean pbcCollectPause;

	private boolean countTypeFlag;

	private boolean useLocal;

	private boolean pbcIgnore;

	private int retryLimit;

	private String token;//第几线程

    //是否启用人行采集限制机制
    private Boolean pbcCollectionLimitUse;

    //人行采集数量控制
    private Long pbcCollectionLimitNum;

//	private PlatformTransactionManager transactionManager;

	public ComparePbcCollectTaskExecutor(Set<CompareCollectRecordVo> amsCollectAccount) {
		this.accountList = amsCollectAccount;
	}

	private int successedNumber=0;

	private int failedNumber=0;

	@Override
	public Object call() throws Exception {
		try {
			mainAccess();
		} catch (Exception e) {
			log.error("人行采集异常", e);
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
//			//用以存放需要暂停的数据
//			List<PbcCollectAccountDto> pauseList = new ArrayList<PbcCollectAccountDto>();
//			for (CompareCollectRecordDto compareCollectRecordDto : accountList) {
//				log.info("开始采集[" + organizationDto.getName() + ","+organizationDto.getCode()+"]账号:" + compareCollectRecordDto.getAcctNo() + "的人行账管信息");
//				// 2017年8月22日19:12:40 添加暂停
//				if (PbcCollectionServiceImpl.manualPause || pbcCollectPause) {
//					pauseList.add(compareCollectRecordDto);
//					continue;
//				}
//				// 根据账号获取人行信息并保存到数据库中
//				getAmsAccountFromAms(compareCollectRecordDto, 0,null);
//			}

			for (CompareCollectRecordVo compareCollectRecordDto : accountList) {
//				log.info("开始采集账号[" + compareCollectRecordDto.getAcctNo() + "] 的人行账管信息");
				OrganizationDto organizationDto = null;
				try{
					if(StringUtils.isNotBlank(compareCollectRecordDto.getOrganCode())){//行内机构号不为空
						organizationDto = organizationService.findByCode(compareCollectRecordDto.getOrganCode());
					}
					if(organizationDto == null){//使用账号到本地账管进行查询
						AccountsAllInfo accountsAllInfo = accountsAllService.findByAcctNo(compareCollectRecordDto.getAcctNo());
						if(accountsAllInfo==null){
							failedNumber++;
							throw new Exception("无法找到对应的机构号");
						}
						organizationDto = organizationService.findByOrganFullId(accountsAllInfo.getOrganFullId());
						if(organizationDto == null){
							failedNumber++;
							throw new Exception("账号对应的机构号为空");
						}
					}
					// 2017年8月22日19:12:40 添加暂停
//					manualPause();
					// 根据账号获取人行信息并保存到数据库中
					if(useLocal){
						ComparePbcInfoDto comparePbcInfoBaseLocal = comparePbcInfoService.getComparePbcInfoBaseLocal(compareCollectRecordDto.getAcctNo());
						if(comparePbcInfoBaseLocal != null){//使用本地有效期内的数据
							log.info("采集账号[" + compareCollectRecordDto.getAcctNo() + "] 的人行账管信息:使用本地有效期内的数据");
							CompareDataDto compareData = new CompareDataDto();
							dataTransformation.dataTransformation(compareData,comparePbcInfoBaseLocal);
							compareData.setTaskId(compareTaskId);
							onlineCollectionProcessor.saveSuccessCombine(compareData,collectTaskId,dataSourceDto, Propagation.REQUIRES_NEW);
							successedNumber++;
						}else{//远程访问人行数据
							log.info("采集账号[" + compareCollectRecordDto.getAcctNo() + "] 的人行账管信息:访问人行的远程数据");
							getAmsAccountFromAms(organizationDto,compareCollectRecordDto.getAcctNo(), 0,null);
						}
					}else{
						log.info("采集账号[" + compareCollectRecordDto.getAcctNo() + "] 的人行账管信息:访问人行的远程数据");
						getAmsAccountFromAms(organizationDto,compareCollectRecordDto.getAcctNo(), 0,null);
					}
					log.info("结束采集账号[" + compareCollectRecordDto.getAcctNo() + "] 的人行账管信息");
				}catch (Exception e){
					log.error("采集账号[" + compareCollectRecordDto.getAcctNo() + "] 的人行账管信息异常:[{}]",e.getMessage());
					updateCollectAccountByFail(compareCollectRecordDto.getAcctNo(),e.getMessage(),organizationDto==null?null:organizationDto.getCode());
				}
			}
			//结束时更新任务处理数量
			updateProcessed();
		}
	}


	private void updateProcessed() throws Exception {
		synchronized (ComparePbcCollectTaskExecutor.class){
			transactionUtils.executeInNewTransaction(new TransactionCallback() {
				@Override
				public void execute() throws Exception {
					CompareCollectTaskDto compareCollectTaskDto = compareCollectTaskService.findById(collectTaskId);
					compareCollectTaskDto.setSuccessed((compareCollectTaskDto.getSuccessed()+successedNumber) > compareCollectTaskDto.getCount() ? compareCollectTaskDto.getCount() : compareCollectTaskDto.getSuccessed()+successedNumber);
					compareCollectTaskDto.setFailed((compareCollectTaskDto.getFailed()+failedNumber) <0 ? 0 : compareCollectTaskDto.getFailed()+failedNumber);
					log.info("成功的数量为:"+successedNumber+",失败的数量为:"+failedNumber);
					log.info("成功的数量最终为:"+compareCollectTaskDto.getSuccessed()+",失败的数量最终为:"+compareCollectTaskDto.getFailed());
					compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);
				}
			});
		}
	}


	private void manualPause() {
		while (isInInvalidTime()) {
			log.info("暂停" + token + "采集人行数据, 10分钟后再检测是否在人行采集时间段内，" + dataSourceDto.getPbcStartTime() + "到" + dataSourceDto.getPbcEndTime());
			try {
				TimeUnit.MINUTES.sleep(10L);
			} catch (InterruptedException var1) {
				log.error("线程暂停异常", var1);
			}
		}

	}

	//在当前设置的时间
	private boolean isInInvalidTime() {
		if (dataSourceDto == null || StringUtils.isBlank(dataSourceDto.getPbcStartTime()) || StringUtils.isBlank(dataSourceDto.getPbcEndTime())) {
			return false;
		} else {
			return !DateUtils.isInInvalidTime(dataSourceDto.getPbcStartTime(), dataSourceDto.getPbcEndTime());
		}
	}


	/**
	 * 从人行账管系统获取账户的客户信息
	 * @param organizationDto
	 * @param acctNo
	 * @param tryNum
	 * @param ex
	 * @throws Exception
	 */
	private void getAmsAccountFromAms(OrganizationDto organizationDto,String acctNo, int tryNum,Exception ex) throws Exception {
		manualPause();
		if (tryNum >= retryLimit) {// 递归出口
//			if(countTypeFlag){//首次采集和excel采集
			failedNumber++;
//			}
			if(ex !=null && ex instanceof SyncException){
				updateCollectAccountByFail(acctNo, ex.getMessage(),organizationDto.getCode());
				return;
			}
			updateCollectAccountByFail(acctNo, "人行采集失败",organizationDto.getCode());
			return;
		}
		try {
			AmsAccountInfo accountInfo=null;
			if(pbcIgnore){
				accountInfo = getAmsAccountInfoByAcctNo(organizationDto.getCode(),acctNo);
			}else{
				if(pbcCollectionLimitUse && !AtomicLongUtils.isPause(pbcCollectionLimitNum)){
					log.info("方法：线程人行查询数量：" + AtomicLongUtils.al.getAndIncrement());
					accountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organizationDto.getCode(),acctNo);//获取人行数据
				}else{
					accountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organizationDto.getCode(),acctNo);
				}
			}
			if (accountInfo != null && StringUtils.isNotBlank(accountInfo.getDepositorName())) {
				save(accountInfo,organizationDto);
				log.info("账号[" + organizationDto.getName() + ","+organizationDto.getCode()+"," + acctNo + "]:第"+(tryNum+1)+"次采集成功");
			}
		} catch (Exception e) {
			log.error("账号[" + organizationDto.getName() + ","+organizationDto.getCode()+"," + acctNo + "]:第"+(tryNum+1)+"次采集失败", e);
			processException(e, organizationDto,acctNo, tryNum);
		}
	}

	/**
	 * 处理采集时出现的异常情况
	 * @param e
	 * @param organizationDto
	 * @param acctNo
	 * @param tryNum
	 * @throws Exception
	 */
	private void processException(Exception e, OrganizationDto organizationDto,String acctNo, int tryNum) throws Exception {
		sleep(1);// 暂停1分钟
		String exceptionMsg = e.getMessage();
//		updateCollectAccountByFail(pbcCollectAccountDto, e);
		if (ExceptionUtils.isNetExption(e)) {
			getAmsAccountFromAms(organizationDto,acctNo, tryNum + 1,e);
		} else if (StringUtils.isNotBlank(exceptionMsg) && exceptionMsg.contains("服务已关闭")) {
			updateCollectTaskProcessed(collectTaskId, CollectTaskState.waitToOpen);
            sleep(30);// 暂停30分钟后 重复调用
			updateCollectTaskProcessed(collectTaskId,CollectTaskState.collecting);
			getAmsAccountFromAms(organizationDto,acctNo, 1,e);
		} else if (StringUtils.isNotBlank(exceptionMsg) && exceptionMsg.contains("用户登录信息失效")) {
			getAmsAccountFromAms(organizationDto,acctNo, tryNum + 1,e);
		} else {
			getAmsAccountFromAms(organizationDto,acctNo, tryNum + 1,e);
		}
	}

	/**
	 * 采集失败，更新采集账户列表
	 * @param acctNo
	 * @param errorMsg
	 * @throws Exception
	 */
	private void updateCollectAccountByFail(final String acctNo, final String errorMsg,final String organCode)
			throws Exception {
		transactionUtils.executeInNewTransaction(new TransactionCallback() {
			@Override
			public void execute() throws Exception {
				CompareCollectRecordDto collectTaskIdAndCompareTaskIdAndAcctNo = compareCollectRecordService.findByCollectTaskIdAndCompareTaskIdAndAcctNo(collectTaskId, compareTaskId, acctNo);
				if(collectTaskIdAndCompareTaskIdAndAcctNo == null){
					CompareCollectRecordDto compareCollectRecordDto = new CompareCollectRecordDto();
					compareCollectRecordDto.setAcctNo(acctNo);
					compareCollectRecordDto.setFailReason(errorMsg);
					compareCollectRecordDto.setCollectTaskId(collectTaskId);
					compareCollectRecordDto.setCompareTaskId(compareTaskId);
					compareCollectRecordDto.setCollectState(CollectState.fail);
					compareCollectRecordDto.setDataSourceType(dataSourceDto.getDataType().name());
					compareCollectRecordDto.setOrganCode(organCode);
					compareCollectRecordService.saveCompareCollectRecord(compareCollectRecordDto);
				}else{
					if(collectTaskIdAndCompareTaskIdAndAcctNo.getCollectState() == CollectState.fail){
						collectTaskIdAndCompareTaskIdAndAcctNo.setFailReason(errorMsg);
						compareCollectRecordService.saveCompareCollectRecord(collectTaskIdAndCompareTaskIdAndAcctNo);
					}else{
						log.error("比对任务[{}]--采集任务[{}]--账号[{}]的之前采集状态为[{}],不能更改为失败",collectTaskIdAndCompareTaskIdAndAcctNo.getCompareTaskId()
								,collectTaskIdAndCompareTaskIdAndAcctNo.getCollectTaskId(),collectTaskIdAndCompareTaskIdAndAcctNo.getAcctNo()
								,collectTaskIdAndCompareTaskIdAndAcctNo.getCollectState().name());
					}
				}
			}
		});
	}

	/**
	 * 保存人行账管账户信息
	 * @param accountInfo
	 * @throws Exception
	 */
	protected void save(final AmsAccountInfo accountInfo,final OrganizationDto organizationDto) throws Exception {
		transactionUtils.executeInNewTransaction(new TransactionCallback() {
			@Override
			public void execute() throws Exception {
				// 人行账管账户信息
				CompareDataDto compareData = new CompareDataDto();
				String[] ignoreProperties = {"id"};
				BeanUtils.copyProperties(accountInfo, compareData,ignoreProperties);
				compareData.setTaskId(compareTaskId);
				ComparePbcInfoDto comparePbcInfoDto = new ComparePbcInfoDto();
				BeanUtils.copyProperties(accountInfo,comparePbcInfoDto,ignoreProperties);
				comparePbcInfoDto.setParDate(DateUtils.getNowDateShort("yyyy-MM-dd hh:mm:ss"));
				comparePbcInfoDto.setIndusRegArea(comparePbcInfoDto.getRegAddress());
				comparePbcInfoDto.setOrganFullId(organizationDto.getFullId());
				comparePbcInfoDto.setAccountStatus(accountInfo.getAccountStatus());
				comparePbcInfoService.saveComparePbcInfo(comparePbcInfoDto);
				dataTransformation.dataTransformation(compareData,comparePbcInfoDto);
				onlineCollectionProcessor.saveSuccessCombine(compareData,collectTaskId,dataSourceDto, null);

//				BeanUtils.copyProperties(accountInfo, dataAccount);
//				dataAccount.setOrganFullId(organizationDto.getFullId());
//				dataAccount.setAccountStatus(accountInfo.getAccountStatus());
//				dataAccount.setParDate(DateUtils.getNowDateShort("yyyy-MM-dd hh:mm:ss"));
//				dataAccount.setIndusRegArea(dataAccount.getRegAddress());
//				dataAccount.setAnnualTaskId(annualTaskId);
//				dataAccount.setCollectTaskId(collectTaskId);
//				dataAccount.setCollectAccountId(collectAccountDto.getId());
//				if(StringUtils.isNotBlank(dataAccount.getRegisteredCapital())){
//					BigDecimal bigDecimal = NumberUtils.formatCapital(NumberUtils.convertCapital(dataAccount.getRegisteredCapital()));
//					dataAccount.setRegisteredCapital(bigDecimal.toString());
//				}
//				fetchPbcDao.deleteByAcctNo(accountInfo.getAcctNo());
//				fetchPbcDao.save(dataAccount);
//				// 账户采集信息
//				collectAccountDto.setCollectState(CollectState.success);
//				collectAccountDto.setParDate(DateUtils.getNowDateShort("yyyy-MM-dd hh:mm:ss"));
//				collectAccountDto.setParErrorMsg("");
//				pbcCollectAccountService.save(collectAccountDto);
//
//				//比对结果表
//				BeanUtils.copyProperties(dataAccount,fetchPbcInfoDto);
//				annualResultService.updateAnnualResultData(annualTaskId,fetchPbcInfoDto);
				successedNumber++;
//				if(!countTypeFlag){//失败的人行数据重新采集
//					failedNumber--;
//				}
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
//			TimeUnit.SECONDS.sleep(min);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
     * 更新任务状态
     * @param collectTaskId
     * @param collectStatus
     */
    private void updateCollectTaskProcessed(final Long collectTaskId, final CollectTaskState collectStatus) throws Exception {
		transactionUtils.executeInNewTransaction(new TransactionCallback() {
			 @Override
			 public void execute() throws Exception {
				 CompareCollectTaskDto compareCollectTaskDto = compareCollectTaskService.findById(collectTaskId);
				 compareCollectTaskDto.setCollectStatus(collectStatus);
				 compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);
			 }
		 });
    }

	/**
	 * 测试数据
	 * @param orgCode
	 * @param acctNo
	 * @return
	 * @throws Exception
	 */
	private AmsAccountInfo getAmsAccountInfoByAcctNo(String orgCode, String acctNo) throws Exception{
		AmsAccountInfo amsAccountInfo = new AmsAccountInfo();
		amsAccountInfo.setAcctNo(acctNo);
		amsAccountInfo.setOrgCode("123456789");
		amsAccountInfo.setDepositorName("浙江省易得融信软件有限公司");
		amsAccountInfo.setLegalName("徐涛");
		amsAccountInfo.setFileNo("330211000096273");
		amsAccountInfo.setBusinessScope("（取消开户许可证核发）为金融机构提供计算机软硬件技术咨询服务及外包服务；计算机软硬件的研发、销售及技术服务；计算机系统集成；企业管理咨询服务；计算机网络信息数据服务；企业宣传服务。");
		amsAccountInfo.setRegisteredCapital("20000000.00");
		return amsAccountInfo;
	}

}

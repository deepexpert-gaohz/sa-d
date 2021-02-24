package com.ideatech.ams.account.service.core;

import com.ideatech.ams.account.dao.bill.AccountBillsAllDao;
import com.ideatech.ams.account.dao.core.CorePublicAccountDao;
import com.ideatech.ams.account.dao.core.CorePublicAccountErrorDao;
import com.ideatech.ams.account.dao.core.CorePublicAccountFinishDao;
import com.ideatech.ams.account.entity.CorePublicAccount;
import com.ideatech.ams.account.entity.CorePublicAccountError;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.account.enums.bill.BillFromSource;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.executor.CoreBillsUpdateExecutor;
import com.ideatech.ams.account.executor.CoreFileSaveExecutor;
import com.ideatech.ams.account.service.AccountPublicService;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.customer.service.CustomersAllService;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component()
@Transactional(rollbackFor = Exception.class)
public class DefaultCoreDataImportListener implements CompanyImportListener {

	@Autowired
	private CompanyImportListener companyImportListener;

	public final static ConcurrentHashMap<String, String> coreFileMaps = new ConcurrentHashMap<>();

//	private static final String FILE_EXTENSION[] = {"txt","TXT"};

	private static final String TEMPFILE_EXTENSION = ".tmp";


	private List<Future<Long>> futureForFileList;

	private List<Future<Long>> futureCoreBillsUpdateList;

	/**
	 * 增量处理标识
	 */
	private static final String DATA_IMPORT_TYPE_INCREASE = "increase";

	private static final String[] COMPARE_FIELDS = { "legalIdcardType", "legalIdcardNo", "legalIdcardDue", "legalTelephone", "orgType", "orgTypeDetail", "bankCardNo", "setupDate", "economyType",
			"industryCode", "regOffice", "economyIndustryCode", "economyIndustryName", "orgEccsNo", "orgStatus", "stateTaxRegNo", "stateTaxDue", "taxDue", "regType", "fileNo", "fileType",
			"fileSetupDate", "fileDue", "fileNo2", "fileType2", "fileSetupDate2", "fileDue2", "regCurrencyType", "businessScope", "corpScale", "taxRegNo", "regNo", "orgCode", "orgCodeDue",
			"regFullAddress", "zipcode", "workProvinceChname", "workCityChname", "workAreaChname", "workAddress", "workFullAddress", "telephone", "parAccountKey", "parOrgCode", "parOrgCodeDue",
			"parCorpName", "parOrgEccsNo", "parRegType", "parRegNo", "parLegalIdcardNo", "parLegalIdcardType", "parLegalIdcardDue", "parLegalType", "parLegalName", "parLegalTelephone", "acctNo",
			"accountName", "acctBigType", "acctType", "accountStatus", "remark", "effectiveDate", "bankCode", "bankName", "acctCreateReason", "capitalProperty", "enchashmentType", "currencyType",
			"fundManager", "fundManagerIdcardType", "fundManagerIdcardNo", "fundManagerIdcardDue", "fundManagerTelephone", "insideDeptName", "insideLeadName", "insideLeadIdcardType",
			"insideLeadIdcardNo", "insideLeadIdcardDue", "insideTelephone", "insideZipcode", "nontmpProjectName", "nontmpLegalName", "nontmpTelephone", "nontmpZipcode", "nontmpAddress",
			"nontmpLegalIdcardType", "nontmpLegalIdcardNo", "nontmpLegalIdcardDue", "acctFileType", "acctFileNo", "acctFileType2", "acctFileNo2", "accountNameFrom", "saccprefix", "saccpostfix",
			"accountKey", "basicAccountStatus", "basicBankCode", "basicBankName", "credentialType", "credentialNo", "credentialDue", "customerNo", "depositorName", "depositorType", "orgEnName",
			"regProvinceChname", "regCityChname", "regArea", "regAreaChname", "legalType", "legalName", "customerCategory" };

	@Value("${ams.t-plus-one.full:false}")
	private boolean tplusoneFull;

	@Value("${import.file.chartset:UTF-8}")
	private String chartset;

	@Value("${import.file.split:|!}")
	protected String split;

	@Value("${import.file.lineEndPrefix:}")
	protected String lineEndPrefix;

	@Value("${import.file.lineEnd:true}")
	protected boolean lineEndAuto;

	@Value("#{'${import.file.fileType}'.split(',')}")
	protected String[] fileType;

	@Value(("${import.file.location}"))
	private String importFileLocation;

	@Value(("${import.file.locationFinish}"))
	private String importFileLocationForFinish;

	@Value(("${ams.company.data-import-type:increase}"))
	private String dataImportType;

	@Value(("${import.file.pbcCoverCore}"))
	private boolean pbcCoverCore;

	/**
	 * 核心数据文件转化的线程数
	 */
	@Value("${ams.company.import.executor-file.num:5}")
	private int coreInitExecutorFileNum;

	@Autowired
	private CorePublicAccountDao corePublicAccountDao;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private AmsCoreAccountService amsCoreAccountService;

	@Autowired
	private CorePublicAccountErrorDao corePublicAccountErrorDao;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private PbcAmsService pbcAmsService;

	@Autowired
	private AccountsAllService accountsAllService;

	private boolean isFirstFile = false;

	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private ThreadPoolTaskExecutor pbcCoverCoreExecutor;

	@Autowired
	private CorePublicAccountFinishDao corePublicAccountFinishDao;

	@Autowired
	private ConfigService configService;

	@Autowired
	private AccountBillsAllDao accountBillsAllDao;

	@Autowired
	private AccountPublicService accountPublicService;

	@Autowired
	private CustomerPublicService customerPublicService;

	@Autowired
	private CustomersAllService customersAllService;

	@Autowired
	private CustomerPublicLogService customerPublicLogService;

	@Override
	public boolean preListener() {
		return false;
	}

	/**
	 * 是否启用人行限制采集机制
	 */
	@Value("${ams.company.pbcCollectionLimit.use:false}")
	private Boolean pbcCollectionLimitUse;

	/**
	 * 人行限制采集数量
	 */
	@Value("${ams.company.pbcCollectionLimit.num:10000}")
	private Long pbcCollectionLimitNum;

//	@Override
	/*public void afterListener(List<CompanyAccountInfo> accountList, List<CompanyErrorAccountInfo> errorAccountList) {
		// TODO Auto-generated method stub

	}*/

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Boolean preFileListener() throws Exception {
		log.info("存量数据文件读取路径：" + importFileLocation);
		File folder = new File(importFileLocation);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		log.info("存量数据文件读取结束存放路径：" + importFileLocationForFinish);
		File finishedFilefolder = new File(importFileLocationForFinish);
		if (!finishedFilefolder.exists()) {
			finishedFilefolder.mkdirs();
		}

		// 保存所有文本数据至数据库
		// 如果是全量先全删
		// if (tplusoneFull) {
		// corePublicAccountRepository.deleteAll();
		// }
		// 获取本批次处理日期
		Boolean isFirst = null;
		if (folder.listFiles().length > 0) {
			for (File file : folder.listFiles()) {
				if (FilenameUtils.isExtension(file.getName(), fileType)) {
					isFirst = companyImportListener.saveFile2Core(file);
				}
			}
		}else{
			log.info("文件夹下无存量数据文件，导入结束！");
		}
		return isFirst;
	}


    /**
     * 分线程进行批处理
     * @param tokens
     * @return
     */
	private Map<String, Set<String>> getBatchTokens(String[] tokens) {
		Map<String, Set<String>> returnMap = new HashMap<String, Set<String>>(16);
		if (tokens != null && tokens.length > 1) {
			int allLeafSum = tokens.length;
			int tokensNum = (allLeafSum / coreInitExecutorFileNum) + 1;
			int num = 0;
			int batchNum = 0;
			Set<String> batchTokens = new HashSet<String>();
            Set<String> allTokens = new HashSet<>();
            for(int i=1;i<allLeafSum;i++){
				if (num > 0 && num % tokensNum == 0) {
					batchNum++;
					returnMap.put("第" + batchNum + "线程", batchTokens);
					batchTokens = new HashSet<String>();
				}
				if(!allTokens.contains(tokens[i])){//判断行的内容是否一模一样重复
                    batchTokens.add(tokens[i]);
                    allTokens.add(tokens[i]);
                }
				num++;
			}
			returnMap.put("第" + (batchNum + 1) + "线程", batchTokens);
            log.info("删选一模一样的行内容之后的行数为："+allTokens.size());
		}else{
            log.info("行数小于1，无法进行导入");
        }
        return returnMap;
	}

	/**
	 * 分线程进行批处理
	 * @param tokens
	 * @return
	 */
	private Map<String, Set<String>> getOrganatchTokens(String[] tokens,String[] methodNames) {
		Map<String, Set<String>> returnMap = new HashMap<>(16);
		Map<String, Integer> fieldLengthForStringMap = BeanUtil.fieldMapForErrorAccount(CorePublicAccount.class);
		if (tokens != null && tokens.length > 1) {

			Set<String> organCodes = new HashSet<>();
			Set<String> batchTokens = new HashSet<>();
			String splitNative = EncodeUtils.ascii2native(split);
			String organCode = "";
			int index = 0;
			int batchNum = 0;
			//找出机构字段的下标
			for(int i = 0;i < methodNames.length;i++){
				String name = methodNames[i];
				if(StringUtils.equalsIgnoreCase("organFullId",name)){
					index = i;
					break;
				}
			}
			//找出所有机构的set集合
			for(int i = 1 ; i < tokens.length ; i++){
				String token = tokens[i];
				if(StringUtils.isNotBlank(token)){
					String[] names = StringUtils.splitByWholeSeparatorPreserveAllTokens(token, splitNative);
					organCode = names[index];
					organCodes.add(organCode);
				}
			}

			//遍历set集合  分组
			for(String code : organCodes){
				batchTokens = new HashSet<>();
				batchNum++;
				for(int i = 1 ; i < tokens.length ; i++){
					String token = tokens[i];
					if(StringUtils.isNotBlank(token)){
						String[] names = StringUtils.splitByWholeSeparatorPreserveAllTokens(token, splitNative);
						organCode = names[index];
						if(code.equals(organCode) && !batchTokens.contains(token)){
							batchTokens.add(token);
						} else if(code.equals(organCode) && batchTokens.contains(token)){
							String[] strs = StringUtils.splitByWholeSeparatorPreserveAllTokens(token, splitNative);
							CorePublicAccount corePublicAccount  = new CorePublicAccount();
							amsCoreAccountService.arrayToCorePublicAccount(methodNames, strs, corePublicAccount, fieldLengthForStringMap);
							saveCorePublicErrorAccount(corePublicAccount,token,"该数据已存在，无法重复导入");
							System.out.println(token);
						}
					}
				}
				returnMap.put("第" + (batchNum) + "线程", batchTokens);
				log.info("删选一模一样的行内容之后的行数为：" + batchTokens.size());
			}
		}else{
			log.info("行数小于1，无法进行导入");
		}
		return returnMap;


//		if (tokens != null && tokens.length > 1) {
//			int allLeafSum = tokens.length;
//			int tokensNum = (allLeafSum / coreInitExecutorFileNum) + 1;
//			int num = 0;
//			int batchNum = 0;
////			Set<String> batchTokens = new HashSet<String>();
////			Set<String> allTokens = new HashSet<>();
//			for(int i=1;i<allLeafSum;i++){
//				if (num > 0 && num % tokensNum == 0) {
//					batchNum++;
//					returnMap.put("第" + batchNum + "线程", batchTokens);
//					batchTokens = new HashSet<String>();
//				}
//				if(!allTokens.contains(tokens[i])){//判断行的内容是否一模一样重复
//					batchTokens.add(tokens[i]);
//					allTokens.add(tokens[i]);
//				}
//				num++;
//			}
//			returnMap.put("第" + (batchNum + 1) + "线程", batchTokens);
//			log.info("删选一模一样的行内容之后的行数为："+allTokens.size());
//		}else{
//			log.info("行数小于1，无法进行导入");
//		}
//		return returnMap;
	}


	/**
	 * 暂停
	 *
	 * @param min
	 */
	private void sleep(int min) {
		try {
			Thread.sleep(1000 * 60 * min);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 判断核心数据转化是否完成
	 *
	 * @throws Exception
	 */
	private void valiInitCompleted() throws Exception {
		for (Iterator<Future<Long>> iterator = futureForFileList.iterator(); iterator.hasNext();) {
			Future<Long> future = iterator.next();
			if(future.isDone()){
				iterator.remove();
			}
		}
		if(futureForFileList.size()>0){
			// 暂停20秒
			TimeUnit.SECONDS.sleep(20);
			valiInitCompleted();
		}
	}

	/**
	 * 判断核心流水更新是否完成
	 *
	 * @throws Exception
	 */
	private void verifyUpdateCompleted(){
		for (Iterator<Future<Long>> iterator = futureCoreBillsUpdateList.iterator(); iterator.hasNext();) {
			Future<Long> future = iterator.next();
			if(future.isDone()){
				iterator.remove();
			}
		}
		if(futureCoreBillsUpdateList.size()>0){
			try {
				// 暂停30秒
				log.info("暂停30秒");
				TimeUnit.SECONDS.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			verifyUpdateCompleted();
		}
	}

//	/**
//	 * 判断核心数据转化是否完成
//	 *
//	 * @throws Exception
//	 */
//	private void valiInitCompleted() throws Exception {
//		while (pbcCoverCoreExecutor.getActiveCount() != 0) {
//			try {
//				// 暂停30s
//				Thread.sleep(1000 * 30 * 1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Boolean saveFile2Core(File file) throws Exception {
		Boolean isFirst = null;
		File finishedFilefolder = new File(importFileLocationForFinish);
		String fileExtension = FilenameUtils.getExtension(file.getName());
		String fileExtensionReal = FilenameUtils.EXTENSION_SEPARATOR_STR + fileExtension;
		// 只处理txt结尾的数据
		if (FilenameUtils.isExtension(file.getName(), fileType)) {
			log.info("存量导入开始读取文件：" + file.getName());
			// 开始处理改后缀为tmp
			File tempFile = new File(file.getAbsolutePath().replace(fileExtensionReal, TEMPFILE_EXTENSION));
			FileUtils.moveFile(file, tempFile);
//			TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//			TransactionStatus transaction = transactionManager.getTransaction(definition);
//			CorePublicAccountError corePublicAccountError = new CorePublicAccountError();
			try {
				isFirst = corePublicAccountDao.count() == 0;
				//如果是增量更新的模式，永远返回true，每次只处理增量（表中不存在的）账户数据
				if (DATA_IMPORT_TYPE_INCREASE.equalsIgnoreCase(dataImportType)) {
					isFirst = true;
				}
				String str = FileUtils.readFileToString(tempFile,chartset);
				String lineEnd ="";
				String lineEndPrefixNative= EncodeUtils.ascii2native(lineEndPrefix);
				String splitNative = EncodeUtils.ascii2native(split);
				if(lineEndAuto){//自动增加换行符
					if(StringUtils.isNotBlank(lineEndPrefixNative)){
						int charNative = StringUtils.indexOf(str, lineEndPrefixNative);
						String property ="";
						if(charNative>-1 && charNative < str.length()){
							while(++charNative < str.length() && (str.charAt(charNative) == '\r' || str.charAt(charNative) == '\n')){
								property += str.charAt(charNative);
							}
						}
						lineEnd = lineEndPrefixNative + property;
					}else{
						int rIndex = StringUtils.indexOf(str, '\r');
						int nIndex = StringUtils.indexOf(str, '\n');
						if(rIndex> -1 || nIndex>-1){
							int firstIndex = rIndex > -1 ? (nIndex > -1 ? (rIndex > nIndex ? nIndex : rIndex): rIndex) : nIndex;
							String property ="";
							property +=str.charAt(firstIndex);
							if(firstIndex>-1 && firstIndex < str.length()){
								while(++firstIndex < str.length() && (str.charAt(firstIndex) == '\r' || str.charAt(firstIndex) == '\n')){
									property += str.charAt(firstIndex);
								}
							}
							lineEnd = property;
						}else{
//							transactionManager.rollback(transaction);
							throw new EacException("核心初始化导入文件的分隔符有误，全文无换行符");
						}
					}
				}else{
					lineEnd = lineEndPrefixNative;
				}
				log.info("核心初始化的行分隔符："+EncodeUtils.ascii2native(lineEnd));
				String[] lines = StringUtils.splitByWholeSeparatorPreserveAllTokens(str, lineEnd);
				log.info("核心初始化的行数为："+lines.length);

				Set<String> organSet =new HashSet<String>();
				Set<String> acctNoSet = new HashSet<String>();

				if(lines.length >1){
					organSet = organizationService.findCodeAllInSet();
					acctNoSet = accountsAllService.findAcctNoAllInSet();
					Iterator<String> iterator = acctNoSet.iterator();
					while(iterator.hasNext()){
						String keyAndValue = iterator.next();
						coreFileMaps.put(keyAndValue,keyAndValue);
					}
				}

				String[] methodNames = null;

				futureForFileList = new ArrayList<Future<Long>>();
				Map<String, Integer> fieldLengthForStringMap = BeanUtil.fieldMapForErrorAccount(CorePublicAccount.class);
				if(lines.length>1){
					log.info("存量数据读取获取表头字段信息...");
					methodNames = StringUtils.splitByWholeSeparatorPreserveAllTokens(lines[0], splitNative);
					log.info("根据机构进行分批次拆分...");
					Map<String, Set<String>> batchTokens = getOrganatchTokens(lines,methodNames);
					log.info("多线程保存存量数据开始...");
					for (String batch : batchTokens.keySet()) {
						CoreFileSaveExecutor coreFileSaveExecutor = new CoreFileSaveExecutor();
						coreFileSaveExecutor.setOrganSet(organSet);
						coreFileSaveExecutor.setTokens(batchTokens.get(batch));
						coreFileSaveExecutor.setBatch(batch);
						coreFileSaveExecutor.setAmsCoreAccountService(amsCoreAccountService);
						coreFileSaveExecutor.setOrganizationService(organizationService);
						coreFileSaveExecutor.setPbcAmsService(pbcAmsService);
						coreFileSaveExecutor.setLineEndPrefixNative(lineEndPrefixNative);
						coreFileSaveExecutor.setSplitNative(splitNative);
						coreFileSaveExecutor.setMethodNames(methodNames);
						coreFileSaveExecutor.setCorePublicAccountErrorDao(corePublicAccountErrorDao);
						coreFileSaveExecutor.setCorePublicAccountDao(corePublicAccountDao);
						coreFileSaveExecutor.setTransactionManager(transactionManager);
						coreFileSaveExecutor.setCorePublicAccountFinishDao(corePublicAccountFinishDao);
						coreFileSaveExecutor.setFieldLengthForStringMap(fieldLengthForStringMap);
						coreFileSaveExecutor.setPbcCoverCore(pbcCoverCore);
						coreFileSaveExecutor.setDictionaryService(dictionaryService);
						coreFileSaveExecutor.setPbcCollectionLimitUse(pbcCollectionLimitUse);
						coreFileSaveExecutor.setPbcCollectionLimitNum(pbcCollectionLimitNum);
						futureForFileList.add(pbcCoverCoreExecutor.submit(coreFileSaveExecutor));
//						pbcCoverCoreExecutor.execute(coreFileSaveExecutor);
					}

					valiInitCompleted();
				}else{
					log.error("文件的行数不对，未使用");
				}
				coreFileMaps.clear();
//				for(int i=0;i<lines.length;i++){
//					String line = lines[i];
//					if(i==0) {
//						methodNames = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, splitNative);
//					}else if(StringUtils.isNotBlank(line)){
//						if (i > 0 && i % 20 == 0) {
//							try{
//								log.info("已经核心数据初始化处理" + i + "条账户");
//								transactionManager.commit(transaction);
//							}catch(Exception e){
//								log.error("批量提交出错",e);
//							}finally {
//								transaction = transactionManager.getTransaction(definition);
//							}
//						}
//						try {
//							if(lineEndPrefixNative != null && lineEndPrefixNative.length()>0 && StringUtils.endsWith(line,lineEndPrefixNative)){
//								line = StringUtils.removeEnd(line,lineEndPrefixNative);
//							}
//							String[] strs = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, splitNative);
//							CorePublicAccount corePublicAccount  = new CorePublicAccount();
//							amsCoreAccountService.arrayToCorePublicAccount(methodNames,strs,corePublicAccount);
//
//
//							if (corePublicAccount != null) {
//								String dateStr = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
//								corePublicAccount.setDataDate(dateStr);
//
//								// 赋值给Error Bean
//								corePublicAccountError = new CorePublicAccountError();
//								BeanCopierUtils.copyProperties(corePublicAccount, corePublicAccountError);
//								corePublicAccountError.setId(null);
//
//								if(!checkRequiredValue(corePublicAccount,corePublicAccountError)){
//									log.debug("未通过必填项验证--"+corePublicAccountError.toString());
//									continue;
//								}
//
//								if(!checkOrganAndAcctNo(corePublicAccount,corePublicAccountError,organSet,acctNoSet)){
//									log.debug("未通过必填项验证--"+corePublicAccountError.toString());
//									continue;
//								}
//
//								// 非第一次导入
//								if (!isFirst) {
//
//									String acctNo = corePublicAccount.getAcctNo();
//									if (StringUtils.isBlank(acctNo)) {
//										saveErrorAccount(corePublicAccountError, "账户账号为空，无法继续处理");
//										continue;
//									}
//
//									// 必要字段的校验，不满足则无法继续处理
//									// 必须有账户状态
//									String coreAccountStatus = corePublicAccount.getAccountStatus();
//									if (StringUtils.isBlank(coreAccountStatus)) {
//										saveErrorAccount(corePublicAccountError, "账户状态为空，无法继续处理");
//										continue;
//									}
//
//									// 筛选逻辑，如果有用则保存
//									// 根据账号去库中查询
//									CorePublicAccount oldCorePublicAccount = corePublicAccountDao.findFirstByAcctNoOrderByCreatedDateDesc(acctNo);
//									// 状态为正常
//									// 数据库不存在
//									if (oldCorePublicAccount == null) {
//										// 如果是正常类型
//
//										/*if (coreAccountStatus.equals(accountStatusCore2PbcMap.get(AccountStatus.normal.name()))) {*/
//										if (coreAccountStatus.equals(dictionaryService.transalte("core2pbc-accountStatus", AccountStatus.normal.name()))) {
//											// 继续处理，插入核心表
//										} else {
//											// 销户与久悬插入错误表
//											saveErrorAccount(corePublicAccountError, "存量数据不存在，但账户状态为" + coreAccountStatus + "，不做处理");
//											updateHandleStatusAndSave(corePublicAccount, CompanyIfType.Yes);
//											continue;
//										}
//
//									} else {
//										String oldCoreAccountStatus = oldCorePublicAccount.getAccountStatus();
//										if (coreAccountStatus.equals(dictionaryService.transalte("core2pbc-accountStatus", AccountStatus.normal.name()))) {
//											// 与oldCorePublicAccount比对是否有区别
//											String[] notMatchFields = compare(oldCorePublicAccount, corePublicAccount);
//											if (notMatchFields.length > 0) {
//												List<String> notMatchFieldList = Arrays.asList(notMatchFields);
//												//机构变更、久悬激活、存款人类别、这三种不支持变更，需要先销户再开户
//												//这类数据保存至核心表但是不操作，保存该数据下次比对使用
//												if(notMatchFieldList.contains("bankCode")) {
//													saveErrorAccount(corePublicAccountError, "机构变更数据需要先销户后开户，不支持自动处理");
//													updateHandleStatusAndSave(corePublicAccount, CompanyIfType.Yes);
//													continue;
//												}
//
//												if(notMatchFieldList.contains("depositorType")) {
//													saveErrorAccount(corePublicAccountError, "存款人类别变更数据需要先销户后开户，不支持自动处理");
//													updateHandleStatusAndSave(corePublicAccount, CompanyIfType.Yes);
//													continue;
//												}
//
//												if(notMatchFieldList.contains("accountStatus")) {
//													//久悬激活
//													if(oldCoreAccountStatus.equals(dictionaryService.transalte("core2pbc-accountStatus", AccountStatus.suspend.name()))) {
//														saveErrorAccount(corePublicAccountError, "久悬激活需要先销户后开户，不支持自动处理");
//														updateHandleStatusAndSave(corePublicAccount, CompanyIfType.Yes);
//														continue;
//													}
//												}
//
//												// 继续处理，插入核心表
//												corePublicAccount.setChangeFieldsStr(Arrays.toString(notMatchFields));
//											} else {
//												// 不处理、插入错误表setChangeFieldsStr
//												saveErrorAccount(corePublicAccountError, "账户数据未变更，不做变更处理");
//												continue;
//											}
//										} else if (coreAccountStatus.equals(dictionaryService.transalte("core2pbc-accountStatus", AccountStatus.revoke.name()))) {
//											// 存量数据为销户，则不处理
//											if (oldCoreAccountStatus.equals(dictionaryService.transalte("core2pbc-accountStatus", AccountStatus.revoke.name()))) {
//												saveErrorAccount(corePublicAccountError, "存量账户数据为销户，不做销户处理");
//												continue;
//											}
//											// 继续处理，插入核心表
//										} else if (coreAccountStatus.equals(dictionaryService.transalte("core2pbc-accountStatus", AccountStatus.suspend.name()))) {
//											if (oldCoreAccountStatus.equals(dictionaryService.transalte("core2pbc-accountStatus", AccountStatus.suspend.name()))) {
//												saveErrorAccount(corePublicAccountError, "存量账户数据为久悬，不做久悬处理");
//												continue;
//											}
//											if (oldCoreAccountStatus.equals(dictionaryService.transalte("core2pbc-accountStatus", AccountStatus.revoke.name()))) {
//												saveErrorAccount(corePublicAccountError, "存量账户数据为销户，不做久悬处理");
//												//数据有变动，需要保存核心表
//												updateHandleStatusAndSave(corePublicAccount, CompanyIfType.Yes);
//												continue;
//											}
//
//											// 继续处理，插入核心表
//										}
//									}
//									// 默认设置未处理
//									// 如果为初次导入，直接为YES
//									corePublicAccount.setHandleStatus(CompanyIfType.No);
//								} else {
//									corePublicAccount.setHandleStatus(CompanyIfType.Yes);
//								}
//								corePublicAccountDao.save(corePublicAccount);
//							}
//						} catch (Exception e) {
//							log.error("核心数据初始化数据" + line + "处理异常", e);
//							transactionManager.rollback(transaction);
//							transaction = transactionManager.getTransaction(definition);
//						} finally {
////							i++;
//						}
//					}
//				}
////				LineIterator.closeQuietly(it);
//				transactionManager.commit(transaction);
				log.info("核心数据初始化文件导入完成");
				log.info("核心数据初始化文件进行转移");
				try {
					FileUtils.moveFile(tempFile, new File(finishedFilefolder + File.separator + file.getName().replace(TEMPFILE_EXTENSION, fileExtensionReal)));
				} catch (FileExistsException e) {
					FileUtils.moveFile(tempFile, new File(finishedFilefolder + File.separator + RandomStringUtils.randomNumeric(5) + file.getName().replace(TEMPFILE_EXTENSION, fileExtensionReal)));
				} finally {
					log.info("核心数据初始化文件转移完成");
				}
			} catch (Exception e) {
//				saveErrorAccount(corePublicAccountError, "数据账号"+corePublicAccountError.getAcctNo()+"保存错误");
				// 恢复文件名
				FileUtils.moveFile(tempFile, file);
				log.error("读取文件异常", e);
			}
		}

		System.gc();
		return isFirst;

	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void afterListener() throws Exception {

		List<OrganizationDto> organizationDtoList = organizationService.listAll();
		futureCoreBillsUpdateList = new ArrayList<Future<Long>>();
        boolean pbcCoverCoreTimeUse = false;
        String pbcCoverCoreTime=null;
        List<ConfigDto> pbcLogin = configService.findByKey("pbcCoverCoreTimeUse");
        if(pbcLogin!=null && pbcLogin.size()>0){
            pbcCoverCoreTimeUse = Boolean.valueOf(pbcLogin.get(0).getConfigValue());
            log.info("是否使用暂定方式(存量导入人行覆盖核心)：{}",pbcCoverCoreTimeUse);
        }
        List<ConfigDto> start = configService.findByKey("pbcCoverCoreTimeWorkStart");
        List<ConfigDto> stop = configService.findByKey("pbcCoverCoreTimeWorkStop");
        if(start!=null && start.size()>0){
            log.info("人行覆盖核心(周一至周五)不执行时间段(其他时间默认执行)开始配置：{}",start.get(0).getConfigValue());
            pbcCoverCoreTime=start.get(0).getConfigValue();
        }
        if(stop!=null && stop.size()>0){
            log.info("人行覆盖核心(周一至周五)不执行时间段(其他时间默认执行)停止配置：{}",stop.get(0).getConfigValue());
            pbcCoverCoreTime=pbcCoverCoreTime+"-"+stop.get(0).getConfigValue();
        }
		for (OrganizationDto organizationDto:organizationDtoList){

			if (organizationDto.getFullId()==null){
				log.info("机构fullId异常:{}",organizationDto.toString());
				continue;
			}
			List<AccountBillsAll> all = accountBillsAllDao.findAllByBillTypeAndFromSourceAndString005AndOrganFullId(BillType.ACCT_INIT,BillFromSource.INIT,"0",organizationDto.getFullId());
			log.info("{}机构:BillType=ACCT_INIT、BillFromSource=INIT、String005=0、OrganFullId={}的数量为{}",organizationDto.getName(),organizationDto.getFullId(),all.size());

			if (all.size()==0){
				log.info("{}机构-存量账户为空，跳过。",organizationDto.getName());
				continue;
			}
			CoreBillsUpdateExecutor coreBillsUpdateExecutor = new CoreBillsUpdateExecutor();
			coreBillsUpdateExecutor.setPbcCollectionLimitNum(pbcCollectionLimitNum);
			coreBillsUpdateExecutor.setPbcCollectionLimitUse(pbcCollectionLimitUse);
			coreBillsUpdateExecutor.setDictionaryService(dictionaryService);
			coreBillsUpdateExecutor.setOrganizationService(organizationService);
			coreBillsUpdateExecutor.setPbcAmsService(pbcAmsService);
			coreBillsUpdateExecutor.setConfigService(configService);
			coreBillsUpdateExecutor.setAccountsAllService(accountsAllService);
			coreBillsUpdateExecutor.setAccountPublicService(accountPublicService);
			coreBillsUpdateExecutor.setAccountBillsAllDao(accountBillsAllDao);
			coreBillsUpdateExecutor.setCustomerPublicService(customerPublicService);
			coreBillsUpdateExecutor.setCustomersAllService(customersAllService);
			coreBillsUpdateExecutor.setCustomerPublicLogService(customerPublicLogService);
			coreBillsUpdateExecutor.setAccountBillsAllList(all);
			coreBillsUpdateExecutor.setPbcCoverCoreTime(pbcCoverCoreTime);
			coreBillsUpdateExecutor.setPbcCoverCoreTimeUse(pbcCoverCoreTimeUse);
			futureCoreBillsUpdateList.add(pbcCoverCoreExecutor.submit(coreBillsUpdateExecutor));
			log.info("{}机构-启动人行覆盖核心机构成功。",organizationDto.getName());
		}

		verifyUpdateCompleted();
	}

	/*private String[] compare(CorePublicAccount corePublicAccount1, CorePublicAccount corePublicAccount2) {
		Set<String> list = new HashSet<String>(16);
		// 该类型为big
		if (corePublicAccount1.getRegisteredCapital() != null && corePublicAccount2.getRegisteredCapital() != null) {
			BigDecimal c1bd = corePublicAccount1.getRegisteredCapital();
			BigDecimal c2bd = corePublicAccount2.getRegisteredCapital();

			if (c1bd.compareTo(c2bd) != 0) {
				list.add("registeredCapital");

			}
		} else {
			list.add("registeredCapital");
		}

		for (String field : COMPARE_FIELDS) {
			Object object1 = BeanValueUtils.getValue(corePublicAccount1, field);
			Object object2 = BeanValueUtils.getValue(corePublicAccount2, field);

			object1 = ObjectUtils.defaultIfNull(object1, "");
			object2 = ObjectUtils.defaultIfNull(object2, "");

			if (!ObjectUtils.equals(object1, object2)) {
				list.add(field);
			}

		}
		String[] result = new String[list.size()];
		return list.toArray(result);
	}*/

	/**
	 * 保存至核心表
	 * @param corePublicAccount
	 * @param handleStatus
	 */
	private void updateHandleStatusAndSave(CorePublicAccount corePublicAccount, CompanyIfType handleStatus) {
		corePublicAccount.setHandleStatus(handleStatus);
		corePublicAccountDao.save(corePublicAccount);
	}

	/**
	*@Description: 校验必填项
	*@Param: [corePublicAccount, corePublicAccountError]
	*@return: boolean
	*@Author: wanghongjie
	*@date: 2018/9/16
	*/
	public boolean checkRequiredValue(CorePublicAccount corePublicAccount,CorePublicAccountError corePublicAccountError){
		if(StringUtils.isBlank(corePublicAccount.getAcctNo())){
			saveErrorAccount(corePublicAccountError, "账号不能为空");
			return false;
		}else if(StringUtils.isBlank(corePublicAccount.getCustomerNo())){
			saveErrorAccount(corePublicAccountError, "客户号不能为空");
			return false;
		}else if(StringUtils.isBlank(corePublicAccount.getDepositorName())){
			saveErrorAccount(corePublicAccountError, "存款人名称不能为空");
			return false;
		}else if(StringUtils.isBlank(corePublicAccount.getOrganFullId())){
			saveErrorAccount(corePublicAccountError, "核心机构的代码不能为空");
			return false;
		}else if(StringUtils.isBlank(corePublicAccount.getAcctBigType())){
			saveErrorAccount(corePublicAccountError, "账户性质大类不能为空");
			return false;
		}else{
			return true;
		}
	}


	/**
	*@Description: 判断机构号是否存在、账号是否重复
	*@Param: [corePublicAccount, corePublicAccountError, organSet, acctNoSet]
	*@return: boolean
	*@Author: wanghongjie
	*@date: 2018/9/16
	*/
	private boolean checkOrganAndAcctNo(CorePublicAccount corePublicAccount,CorePublicAccountError corePublicAccountError,Set<String> organSet,Set<String> acctNoSet){
		String acctNo = corePublicAccount.getAcctNo();
		String organFullId = corePublicAccount.getOrganFullId();
		if(!organSet.contains(organFullId)){
			saveErrorAccount(corePublicAccountError, "无法找到对应的核心机构代码");
			return false;
		}else{
			if(acctNoSet.contains(acctNo)){//已经含有该账号
				saveErrorAccount(corePublicAccountError, "该账号已存在，无法重复导入");
				return false;
			}else{
				acctNoSet.add(acctNo);
				return true;
			}
		}
	}



	private void saveErrorAccount(CorePublicAccountError corePublicAccountError, String errorMsg) {
		corePublicAccountError.setErrorReason(errorMsg);
		corePublicAccountErrorDao.save(corePublicAccountError);
	}

	private void saveCorePublicErrorAccount(CorePublicAccount corePublicAccount,String token, String errorMsg) {
		CorePublicAccountError corePublicAccountError = new CorePublicAccountError();
		BeanCopierUtils.copyProperties(corePublicAccount, corePublicAccountError);
		corePublicAccountError.setId(null);
		corePublicAccountError.setErrorToken(token);
		saveErrorAccount(corePublicAccountError, errorMsg);
	}

}

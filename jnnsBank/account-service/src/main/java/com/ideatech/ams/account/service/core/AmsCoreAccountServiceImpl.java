package com.ideatech.ams.account.service.core;

import com.ideatech.ams.account.dao.AllBillsPublicDao;
import com.ideatech.ams.account.dao.core.CorePublicAccountDao;
import com.ideatech.ams.account.dao.core.CorePublicAccountErrorDao;
import com.ideatech.ams.account.dao.core.CorePublicAccountFinishDao;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.entity.CorePublicAccount;
import com.ideatech.ams.account.entity.CorePublicAccountError;
import com.ideatech.ams.account.entity.CorePublicAccountFinish;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.AcctBigType;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.*;
import com.ideatech.ams.account.executor.CoreBillsInitExecutor;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.bill.BillNoSeqService;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.account.util.AcctCheckNullUtil;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.pbc.utils.DateUtils;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.dict.dto.DictionaryDto;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.jpa.IdeaNamingStrategy;
import com.ideatech.common.util.ReflectionUtil;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.util.SecurityUtils.UserInfo;
import com.ideatech.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author wanghongjie
 *
 * @version 2018-06-21 21:29
 */
@Service
//@Transactional
@Slf4j
public class AmsCoreAccountServiceImpl implements AmsCoreAccountService{

	public final static ConcurrentHashMap<String, AllBillsPublicDTO> coreObjectMaps = new ConcurrentHashMap<>();

	public final static ConcurrentHashMap<Long, String> coreErrorReasonMaps = new ConcurrentHashMap<>();

	public final static ConcurrentHashMap<String, Boolean> duplicateCustomerNoMaps = new ConcurrentHashMap<>();

	public static Boolean coreObjectSaveFlag = false;

	@Autowired
	private AmsCoreAccountService amsCoreAccountService;

	@Value("${ams.nmnx.core.file.path}")
	private String filePath;

	@Value("${ams.company.import.file.split}")
	private String fileSplit;

	@Value("${ams.company.import.file.chartset}")
	private String fileChartset;
	
	@Value("${ams.company.t+1.threshold:100000}")
	protected int threshold;

	/**
	 * 核心数据转化的线程数
	 */
	@Value("${ams.company.import.executor.num:5}")
	private int coreInitExecutorNum;


	@Autowired
	private UserService userService;
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Autowired
	private CorePublicAccountDao corePublicAccountDao;

	@Autowired
	private CorePublicAccountErrorDao corePublicAccountErrorDao;

	@Autowired
	private CorePublicAccountFinishDao corePublicAccountFinishDao;

	@Autowired
	private AllBillsPublicDao allBillsPublicDao;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private Map2DomainService map2DomainService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private BillNoSeqService billNoSeqService;
	
	@Autowired
	private AllBillsPublicService allBillsPublicService;

    @Autowired
	private PbcAccountService pbcAccountService;

    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private PbcAmsService pbcAmsService;

	private List<DictionaryDto> core2pbcDictionaryList;
	
	private List<DictionaryDto> core2pbcFileTypeDictionaryList;

	private Map<String,String> core2pbcMap;

	private List<Future<Long>> futureForInitList;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	/* 
	 * 对象映射
	 * (non-Javadoc)
	 * @see com.ideatech.ams.account.service.core.AmsCoreAccountService#arrayToCorePublicAccount(java.lang.String[], java.lang.String[], java.lang.Object)
	 */
	@Override
	public HashMap<String, Integer> arrayToCorePublicAccount(String[] methodNames,String[] values,Object obj,Map<String, Integer> fieldLengthForStringMap) {
		HashMap<String, Integer> inValidLengthForString = new HashMap<String, Integer>();
		if(methodNames.length >0 && values.length>0 && methodNames.length == values.length) {
			Map<String, Field> fieldMap = ReflectionUtil.getFieldObjMap(obj);
			for (int i = 0; i < methodNames.length; i++) {
				//2019年04月30日 表头数据，支持中文
				String methodName = methodNames[i].toUpperCase();
				try {
					String[] methodNamess = StringUtils.splitByWholeSeparatorPreserveAllTokens(methodName, "-");
					if (methodNamess.length == 2) {
						methodName = methodNamess[1];
					}
				} catch (Exception e) {
					log.error("核心数据初始化，表头解析异常", e);
				}

				if (fieldMap.containsKey(methodName)) {
					Field field = fieldMap.get(methodName);
					if (field != null) {
						field.setAccessible(true);
						if (field.getType().isAssignableFrom(String.class)) {
							String strValue = values[i].trim();
							if (StringUtils.isNotBlank(strValue) && fieldLengthForStringMap.containsKey(methodName)) {//是否在校验长度的Map中
								int length = fieldLengthForStringMap.get(methodName);
								if (length > -1) {//不含@Lob
									if (length < StringUtils.length(strValue)) {//不在@Column规定的长度之内
										strValue = StringUtils.substring(strValue, 0, length);
										inValidLengthForString.put(methodName, length);
									}
								}
							}
							ReflectionUtils.setField(field, obj, strValue);
						} else if (field.getType().isAssignableFrom(BigDecimal.class)) {
							ReflectionUtils.setField(field, obj, new BigDecimal(StringUtils.isBlank(values[i]) ? "0" : values[i]));
						} else if (field.getType().isAssignableFrom(Long.class)) {
							ReflectionUtils.setField(field, obj, Long.valueOf(StringUtils.isBlank(values[i]) ? "0" : values[i]));
						} else if (field.getType().isAssignableFrom(CompanyAcctType.class)) {
							if (StringUtils.isNotBlank(values[i])) {
								ReflectionUtils.setField(field, obj, CompanyAcctType.valueOf(values[i]));
							}
						}
					}
				}
			}
		}
		return inValidLengthForString;
	}

	@Override
	public boolean isHaveFile() {
		File file = new File(filePath);
		if (!file.exists()) {
			log.info("文件夹不存在！");
			file.mkdir();
		}
		File list[] = file.listFiles();
		if (list.length <= 0) {
			log.info("文件不存在！");
			return false;
		} else {
			log.info("文件已存在！");
			return true;
		}
	}

	@Override
	public void initData() {
		if (!isHaveFile()) {
			throw new RuntimeException("文件不存在");
		}
		log.info("开始初始化采集核心账户信息数据");
		File file = new File(filePath);
		File files[] = file.listFiles();
		for (File file2 : files) {
			/*if (file2.getName().contains("customer")) {
				arrayToCorePublicAccount(file2);
			} else if (file2.getName().contains("partner")) {
				arrayToCoreCompanyPartner(file2);
			} else if (file2.getName().contains("relate")) {
				arrayToCoreRelateCompany(file2);
			}*/
		}
		/*
		 * List<AllBillsPublicDTO> billsPublicDTO =
		 * allAccountPublicRepository.findAallData();
		 * checkIsNull(billsPublicDTO);
		 */
		log.info("核心账户信息数据采集完成");
		
	}

	@Override
	public void initTableData() {
		log.info("开始初始化核心存量数据");
		UserInfo userInfo = SecurityUtils.getCurrentUser();
		UserDto userDto = null;
		if (userInfo == null) {// 没有用户取虚拟用户
			userDto = userService.findVirtualUser();
		} else {
			userDto = new UserDto();
			BeanUtils.copyProperties(userInfo, userDto);
		}

		//获取必要字典
		log.info("存量数据获取core2pbc字典信息...............");
		core2pbcDictionaryList = dictionaryService.getDictionaryLikeName("core2pbc-");
		core2pbcFileTypeDictionaryList = dictionaryService.getDictionaryLikeName("core2pbc-fileType-");
		core2pbcFileTypeDictionaryList.addAll(dictionaryService.getDictionaryLikeName("core2pbc-fileType2-"));
		core2pbcFileTypeDictionaryList.addAll(dictionaryService.getDictionaryLikeName("core2pbc-acctFileType-"));
		core2pbcFileTypeDictionaryList.addAll(dictionaryService.getDictionaryLikeName("core2pbc-acctFileType2-"));

		core2pbcMap = new HashMap<>();

		AmsCoreAccountServiceImpl.coreObjectMaps.clear();
		AmsCoreAccountServiceImpl.coreErrorReasonMaps.clear();
		AmsCoreAccountServiceImpl.duplicateCustomerNoMaps.clear();
		log.info("存量数据查询视图yd_public_core_all_v信息...");
		String sql = "select * from yd_public_core_all_v ";
		//2018年8月24日添加只导入系统中没有的数据
//		sql+=" WHERE yd_acct_no NOT IN ( SELECT yd_acct_no FROM yd_accounts_all WHERE yd_acct_no IS NOT NULL) ";
		Pageable pageable = new PageRequest(0, threshold);
		long count = allBillsPublicDao.getCount(sql, null);
		Page list = null;
		boolean hasNext = true;
		log.info("需要处理的总核心数据的数量为："+count);
		while (hasNext && count > 0) {
			try {
				AmsCoreAccountServiceImpl.coreObjectSaveFlag= true;
				list = allBillsPublicDao.findPageMap(sql, null, pageable);
				hasNext = pageable.getPageSize() * (pageable.getPageNumber() + 1) < count;
				pageable = pageable.next();
				amsCoreAccountService.saveAllBillsPublic(list, userDto);
			} catch (Exception e) {
				log.error("分页处理YD_PUBLIC_CORE_ALL_V数据异常", e);
			} finally {
				AmsCoreAccountServiceImpl.coreObjectSaveFlag= false;
			}
		}
		log.info("核心存量数据转移完成");


		log.info("核心存量表数据开始进行转移");
		Long startTime = System.currentTimeMillis();
		TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus transaction = transactionManager.getTransaction(definition);;
		List<CorePublicAccount> all = corePublicAccountDao.findAll();
		int index=0;
		for (CorePublicAccount corePublicAccount : all){
			index++;
			if(corePublicAccount != null && AmsCoreAccountServiceImpl.coreErrorReasonMaps.containsKey(corePublicAccount.getId())){
				CorePublicAccountError corePublicAccountError = new CorePublicAccountError();
				BeanUtils.copyProperties(corePublicAccount,corePublicAccountError);
				corePublicAccountError.setId(null);
				corePublicAccountError.setErrorReason(AmsCoreAccountServiceImpl.coreErrorReasonMaps.get(corePublicAccount.getId()));
				corePublicAccountErrorDao.save(corePublicAccountError);
				AmsCoreAccountServiceImpl.coreErrorReasonMaps.remove(corePublicAccount.getId());
			}else{
				CorePublicAccountFinish corePublicAccountFinish = new CorePublicAccountFinish();
				BeanUtils.copyProperties(corePublicAccount,corePublicAccountFinish);
				corePublicAccountFinish.setId(null);
				corePublicAccountFinishDao.save(corePublicAccountFinish);
			}
			corePublicAccountDao.delete(corePublicAccount.getId());
			if(index >0 && index%5000 ==0){
				transactionManager.commit(transaction);
				transaction = transactionManager.getTransaction(definition);
				log.info("核心存量表数据转移中，转移数量-{}",index);
			}
		}
		transactionManager.commit(transaction);
		Long endTime = System.currentTimeMillis();
		log.info("核心存量表数据转移成功,耗时" + (endTime - startTime) / 1000 + "秒");
		AmsCoreAccountServiceImpl.coreObjectMaps.clear();
		AmsCoreAccountServiceImpl.coreErrorReasonMaps.clear();
		AmsCoreAccountServiceImpl.duplicateCustomerNoMaps.clear();
	}
	
	

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void saveAllBillsPublic(Page list, UserDto userInfo) throws Exception {
		futureForInitList = new ArrayList<Future<Long>>();
		List<Object> objs = list.getContent();
		Map<String, Set<Object>> batchTokens = getBatchTokens(objs);
		if (MapUtils.isNotEmpty(batchTokens) && batchTokens.size() > 0) {
			CoreBillsInitExecutor executor = null;
			for (String batch : batchTokens.keySet()) {
				executor = new CoreBillsInitExecutor(batchTokens.get(batch));
				executor.setBatch(batch);
				executor.setAmsCoreAccountService(amsCoreAccountService);
				executor.setBillNoSeqService(billNoSeqService);
				executor.setUserDto(userInfo);
				executor.setTransactionManager(transactionManager);
				executor.setCorePublicAccountDao(corePublicAccountDao);
				executor.setCorePublicAccountFinishDao(corePublicAccountFinishDao);
				futureForInitList.add(taskExecutor.submit(executor));
				log.info(batch + "启动，开始核心数据处理==================");
			}
			valiInitCompleted();
			log.info("核心数据的转化和保存完成");
		}

	}


	/**
	 * 判断核心数据转化是否完成
	 *
	 * @throws Exception
	 */
	private void valiInitCompleted() throws Exception {
		for (Iterator<Future<Long>> iterator = futureForInitList.iterator(); iterator.hasNext();) {
			Future<Long> future = iterator.next();
			if(future.isDone()){
				iterator.remove();
			}
		}
		if(futureForInitList.size()>0){
			// 暂停20秒
			TimeUnit.SECONDS.sleep(20);
			valiInitCompleted();
		}else{
			AmsCoreAccountServiceImpl.coreObjectSaveFlag= false;
		}
	}

	private Map<String, Set<Object>> getBatchTokens(List<Object> tokens) {
		Map<String, Set<Object>> returnMap = new HashMap<String, Set<Object>>(16);
		if (tokens != null && tokens.size() > 0) {
			int allLeafSum = tokens.size();
			int tokensNum = (allLeafSum / coreInitExecutorNum) + 1;
			int num = 0;
			int batchNum = 0;
			Set<Object> batchTokens = new HashSet<Object>();
			Set<Object> allTokens = new HashSet<>();
			for (Object token : tokens) {
				if (num > 0 && num % tokensNum == 0) {
					batchNum++;
					returnMap.put("第" + batchNum + "线程", batchTokens);
					batchTokens = new HashSet<Object>();
				}
				if(!allTokens.contains(token)){//判断行的内容是否一模一样重复
					batchTokens.add(token);
					allTokens.add(token);
				}
				num++;
			}
			returnMap.put("第" + (batchNum + 1) + "线程", batchTokens);
			log.info("删选之后需要进行核心数据转化的数量为："+allTokens.size());
		}else{
			log.info("行数小于1，无法进行核心数据转化");
		}
		return returnMap;
	}

	@Override
	public void initAllBillsPublicData(AllBillsPublicDTO allBillsPublicDTO) {
		allBillsPublicDTO.setBillType(BillType.ACCT_INIT);
		String coreBankCode = allBillsPublicDTO.getOrganCode();
		if (StringUtils.isBlank(coreBankCode)) {
			coreBankCode = allBillsPublicDTO.getOrganFullId();
		}
		OrganizationDto organ = organizationService.findByCode(coreBankCode);
		if (organ != null) {
			allBillsPublicDTO.setOrganFullId(organ.getFullId().toString());
			allBillsPublicDTO.setCustOrganFullId(organ.getFullId().toString());
			allBillsPublicDTO.setAcctOrgFullid(organ.getFullId().toString());
			allBillsPublicDTO.setBankCode(organ.getPbcCode());
			allBillsPublicDTO.setBankName(organ.getName());
			allBillsPublicDTO.setOrganCode(coreBankCode);
		}else{
//			log.error("行内机构号不能为空且必须匹配对应的机构号");
			throw new EacException("行内机构号不能为空且必须匹配对应的机构号");
		}
		if(allBillsPublicDTO.getAcctBigType() != null && allBillsPublicDTO.getAcctType() ==null) {
			AcctBigType acctBigType = allBillsPublicDTO.getAcctBigType();
			if("jiben".equals(acctBigType.name())) {
				allBillsPublicDTO.setAcctType(CompanyAcctType.str2enum("jiben"));
			}else if("yiban".equals(acctBigType.name())) {
				allBillsPublicDTO.setAcctType(CompanyAcctType.str2enum("yiban"));
			}else if("zhuanyong".equals(acctBigType.name())) {
				allBillsPublicDTO.setAcctType(CompanyAcctType.str2enum("specialAcct"));
			}else if("linshi".equals(acctBigType.name())) {
				allBillsPublicDTO.setAcctType(CompanyAcctType.str2enum("tempAcct"));
			}
		}
		//先设置accttype中的大类到小类
		allBillsPublicService.acctBigType2AcctType(allBillsPublicDTO);

		//配置开关打开则转换
		List<ConfigDto> initImportConfig = configService.findByKey("import");
		if (CollectionUtils.isNotEmpty(initImportConfig) && Boolean.valueOf(initImportConfig.get(0).getConfigValue())) {
			log.info("存量导入初始导入页面控制为：" + Boolean.valueOf(initImportConfig.get(0).getConfigValue()));
			//上报前查看是否是大类  大类根据账号去人行查询小类并保存  基本户  一般户去处理
			if(coreBankCode !=null && allBillsPublicDTO.getAcctType() != null && (allBillsPublicDTO.getAcctType() == CompanyAcctType.tempAcct || allBillsPublicDTO.getAcctType() == CompanyAcctType.specialAcct)) {
				PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganCode(coreBankCode, EAccountType.AMS);
				if(pbcAccountDto !=null) {
					try {
						//查询人行返回对象
						AmsAccountInfo amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(pbcAccountDto, allBillsPublicDTO.getAcctNo());
						if (amsAccountInfo.getAcctType() != null) {
							//根据人行对象账户性质转换billsPublic小类对象
							allBillsPublicDTO.setAcctType(CompanyAcctType.valueOf(amsAccountInfo.getAcctType().name()));
							//销户日期赋值  正常账户为“-”
							allBillsPublicDTO.setCancelDate(amsAccountInfo.getCancelDate());
						}
					} catch (Exception e) {
						//如果查询失败直接跳过处理
						log.info("存量数据导入大类转小类人行查询错误：" + e);
					}

				}else {
					//如果未配置人行用户则直接跳过处理
					log.info("存量数据导入大类转小类人行查询AmsAccountInfo为空");
				}
			}
		}else{
			log.info("存量导入初始导入页面控制为：false");
		}
		if (allBillsPublicDTO.getAccountStatus() == null) {
			if(StringUtils.isNotBlank(allBillsPublicDTO.getCancelDate()) && !"-".equals(allBillsPublicDTO.getCancelDate())){
				allBillsPublicDTO.setAccountStatus(AccountStatus.revoke);
			}else{
				allBillsPublicDTO.setAccountStatus(AccountStatus.normal);
			}

		}
//		if (StringUtils.isBlank(allBillsPublicDTO.getBillNo())) {
//			String billNo = billNoSeqService.getBillNo(DateUtils.DateToStr(new Date(), "yyyyMMdd"), coreBankCode, BillTypeNo.valueOf(allBillsPublicDTO.getBillType().name()));
//			// 单据编号
//			allBillsPublicDTO.setBillNo(billNo);
//		}
		allBillsPublicDTO.setId(null);
	}
	

	@Override
	public void checkIsNull(AllBillsPublicDTO allBillsPublicDTO, UserDto userInfo) {
		// 判断字段是否需要全
		AcctCheckNullUtil nullUtil = new AcctCheckNullUtil();
		setDate(allBillsPublicDTO);
		setAcctName(allBillsPublicDTO); // 账户名称
		setOrgCode(allBillsPublicDTO);//组织机构代码
		// 账户性质为空则补录
		if (allBillsPublicDTO.getAcctType() == null || StringUtils.isBlank(allBillsPublicDTO.getAcctType().toString())) {
			log.info("存量数据账户性质为空方法进入..............");
			allBillsPublicDTO.setInitFullStatus("1");
			allBillsPublicDTO.setBillType(BillType.ACCT_INIT);
			try {
				
				//初始化默认全部处理完成
				allBillsPublicDTO.setStatus(BillStatus.APPROVED);
				log.info("存量数据保存数据库..............");
				Long billId = allBillsPublicService.save(allBillsPublicDTO, userInfo, false);
				log.info("存量数据保存数据库..............");
				allBillsPublicDTO.setId(billId);
				
//				BillStatus billStatus = null;
//				if ("1".equals(allBillsPublicDTO.getInitFullStatus())) {
//					billStatus = BillStatus.Approved;
//				} else {
//					billStatus = BillStatus.daiBuLu;
//				}
//				billsPublicService.updateApproveStatus(allBillsPublicDTO, billStatus, userInfo.getId(), "");
				//更新最终状态，处理中间表等逻辑
				allBillsPublicService.updateFinalStatus(billId);
			} catch (Exception e) {
				log.error("核心数据处理异常", e);
				throw new EacException(e.getMessage());
				//开始处理下一条数据
//				return;
			}
		} else {
			log.info("存量数据账户性质不为空方法进入..............");
			// 基本户判断是否需要补录
			if (allBillsPublicDTO.getAcctType() == CompanyAcctType.jiben) {
				nullUtil.jibenCheck(allBillsPublicDTO);
			}
			// 一般户判断是否补录
			if (allBillsPublicDTO.getAcctType() == CompanyAcctType.yiban) {
				nullUtil.yibanCheck(allBillsPublicDTO);
			}
			// 预算
			if (allBillsPublicDTO.getAcctType() == CompanyAcctType.yusuan) {
				nullUtil.yusuanCheck(allBillsPublicDTO);
			}
			// 非预算
			if (allBillsPublicDTO.getAcctType() == CompanyAcctType.feiyusuan) {
				nullUtil.feiyusuanCheck(allBillsPublicDTO);
			}
			// 临时
			if (allBillsPublicDTO.getAcctType() == CompanyAcctType.linshi) {
				nullUtil.linshiCheck(allBillsPublicDTO);
			}
			// 非临时
			if (allBillsPublicDTO.getAcctType() == CompanyAcctType.feilinshi) {
				nullUtil.feilinshiCheck(allBillsPublicDTO);
			}
			// 特殊
			if (allBillsPublicDTO.getAcctType() == CompanyAcctType.teshu) {
				nullUtil.teshuCheck(allBillsPublicDTO);
			}

			try {
				// 调用之前先修改全部数据为完成
				setAllStatus(allBillsPublicDTO);
				log.info("存量数据保存数据库..............");
				Long billId = allBillsPublicService.save(allBillsPublicDTO, userInfo, false);
				allBillsPublicDTO.setId(billId);
				// 更新流水最终状态
				try{
					allBillsPublicService.updateFinalStatus(billId);
				}catch (Exception e){
					log.error("更新流水最终状态--错误");
				}
			} catch (Exception e) {
				log.error("核心数据处理异常", e);
				//开始处理下一条数据
				throw new EacException(e.getMessage());
//				return;
			}
		}

	}
	

	/**
	 * 设置所有状态为完成
	 * @param allBillsPublicDTO
	 */
	private void setAllStatus(AllBillsPublicDTO allBillsPublicDTO) {
		allBillsPublicDTO.setStatus(BillStatus.APPROVED);

		Long time = System.currentTimeMillis();
		String dateStr = DateFormatUtils.ISO_DATE_FORMAT.format(time);
		String timeStr = DateFormatUtils.ISO_DATETIME_FORMAT.format(time);

		allBillsPublicDTO.setPbcSyncTime(timeStr);
		allBillsPublicDTO.setPbcSyncCheck(SyncCheckStatus.checkPass);
		allBillsPublicDTO.setPbcSyncStatus(CompanySyncStatus.tongBuChengGong);

		if (allBillsPublicDTO.getAcctType() == CompanyAcctType.jiben
				|| allBillsPublicDTO.getAcctBigType() == AcctBigType.linshi) {
			allBillsPublicDTO.setPbcCheckDate(dateStr);
			allBillsPublicDTO.setPbcCheckStatus(CompanyAmsCheckStatus.CheckPass);
		}

		if (allBillsPublicDTO.getAcctType() == CompanyAcctType.jiben) {
			allBillsPublicDTO.setEccsSyncTime(timeStr);
			allBillsPublicDTO.setEccsSyncCheck(SyncCheckStatus.checkPass);
			allBillsPublicDTO.setEccsSyncStatus(CompanySyncStatus.tongBuChengGong);
		}
	}


	private static void setOrgCode(AllBillsPublicDTO allBillsPublic){
		allBillsPublic.setOrgCode(StringUtils.replace(allBillsPublic.getOrgCode(), "-", ""));
	}

	 // 设置账户名称
   public static void setAcctName(AllBillsPublicDTO entity) {
     // 判断账户名称
     if (CompanyAcctType.yusuan.equals(entity.getAcctType())
         || CompanyAcctType.feiyusuan.equals(entity.getAcctType())) {// 预算
       // 账户名称内设部门
       if (StringUtils.isNotEmpty(entity.getAccountNameFrom())
           && entity.getAccountNameFrom().equals("1")) {
         if (StringUtils.isEmpty(entity.getInsideDeptName())) {
           entity.setInsideDeptName("");
         }
         if (StringUtils.isNotEmpty(entity.getDepositorName())) {
         	if (StringUtils.isEmpty(entity.getAcctName()))
				entity.setAcctName(entity.getDepositorName() + entity.getInsideDeptName());
         } else {
			 if (StringUtils.isEmpty(entity.getAcctName()))
           		entity.setAcctName(entity.getInsideDeptName());
         }
       }
       // 账户名称加上前缀后缀
       else if (StringUtils.isNotEmpty(entity.getAccountNameFrom())
           && entity.getAccountNameFrom().equals("2")) {
         if (StringUtils.isEmpty(entity.getSaccpostfix())) {
           entity.setSaccpostfix("");
         }
         if (StringUtils.isEmpty(entity.getSaccprefix())) {
           entity.setSaccprefix("");
         }
         if (StringUtils.isEmpty(entity.getDepositorName())) {
           entity.setDepositorName("");
         }
         if (entity.getAcctType().equals(CompanyAcctType.yusuan)
             || entity.getAcctType().equals(CompanyAcctType.feiyusuan)) {
			 if (StringUtils.isEmpty(entity.getAcctName()))
           		entity.setAcctName(entity.getSaccprefix() + entity.getDepositorName() + entity.getSaccpostfix());
         }
       } else {
         if (StringUtils.isNotEmpty(entity.getDepositorName())) {
			 if (StringUtils.isEmpty(entity.getAcctName()))
           		entity.setAcctName(entity.getDepositorName());
         } else {
			 if (StringUtils.isEmpty(entity.getAcctName()))
           		entity.setAcctName("");
         }
       }
     } else if (CompanyAcctType.feilinshi.equals(entity.getAcctType())) {// 非临时
       // 开户原因 建设部门
       if (StringUtils.isNotEmpty(entity.getAcctCreateReason())
           && entity.getAcctCreateReason().equals("1")) {
         if (StringUtils.isEmpty(entity.getNontmpProjectName())) {
           entity.setNontmpProjectName("");
         }
         if (StringUtils.isNotEmpty(entity.getDepositorName())) {
			 if (StringUtils.isEmpty(entity.getAcctName()))
           		entity.setAcctName(entity.getDepositorName() + entity.getNontmpProjectName());
         } else {
			 if (StringUtils.isEmpty(entity.getAcctName()))
           		entity.setAcctName("");
         }
       } else {
         if (StringUtils.isNotEmpty(entity.getDepositorName())) {
			 if (StringUtils.isEmpty(entity.getAcctName()))
           		entity.setAcctName(entity.getDepositorName());
         } else {
			 if (StringUtils.isEmpty(entity.getAcctName()))
           		entity.setAcctName("");
         }
       }
     } else {
       if (StringUtils.isNotEmpty(entity.getDepositorName())) {
		   if (StringUtils.isEmpty(entity.getAcctName()))
         		entity.setAcctName(entity.getDepositorName());
       } else {
		   if (StringUtils.isEmpty(entity.getAcctName()))
         		entity.setAcctName("");
       }
     }
   }


	public void setDate(AllBillsPublicDTO dataAccount){
    	try {
    		dataAccount.setAcctCreateDate(DateUtils.setDateColumnFormat(dataAccount.getAcctCreateDate(),"yyyy-MM-dd"));//账户开户日期
    		dataAccount.setFileDue(DateUtils.setDateColumnFormat(dataAccount.getFileDue(),"yyyy-MM-dd"));//证明文件1到期日
    		dataAccount.setFileDue2(DateUtils.setDateColumnFormat(dataAccount.getFileDue2(),"yyyy-MM-dd"));//证明文件2到期日
    		dataAccount.setFileSetupDate(DateUtils.setDateColumnFormat(dataAccount.getFileSetupDate(),"yyyy-MM-dd"));//证明文件1设立日期
    		dataAccount.setFileSetupDate2(DateUtils.setDateColumnFormat(dataAccount.getFileSetupDate2(),"yyyy-MM-dd"));//证明文件2设立日期
    		dataAccount.setSetupDate(DateUtils.setDateColumnFormat(dataAccount.getSetupDate(),"yyyy-MM-dd"));//营业执照成立日期
    		dataAccount.setBusinessLicenseDue(DateUtils.setDateColumnFormat(dataAccount.getBusinessLicenseDue(),"yyyy-MM-dd"));//到期日
    		dataAccount.setLegalIdcardDue(DateUtils.setDateColumnFormat(dataAccount.getLegalIdcardDue(), "yyyy-MM-dd"));//法人证件到期日
			dataAccount.setOrgCodeDue(DateUtils.setDateColumnFormat(dataAccount.getOrgCodeDue(), "yyyy-MM-dd"));//组织机构代码证
			dataAccount.setEffectiveDate(DateUtils.setDateColumnFormat(dataAccount.getEffectiveDate(), "yyyy-MM-dd"));//有效日期
		} catch (Exception e) {
    		throw new EacException("日期转换错误："+e);
		}
    }

    private void initDics() {
		if (CollectionUtils.isEmpty(core2pbcDictionaryList)) {
			core2pbcDictionaryList = dictionaryService.getDictionaryLikeName("core2pbc-");
		}
		if (CollectionUtils.isEmpty(core2pbcFileTypeDictionaryList)) {
			core2pbcFileTypeDictionaryList = dictionaryService.getDictionaryLikeName("core2pbc-fileType-");
			core2pbcFileTypeDictionaryList.addAll(dictionaryService.getDictionaryLikeName("core2pbc-fileType2-"));
			core2pbcFileTypeDictionaryList.addAll(dictionaryService.getDictionaryLikeName("core2pbc-acctFileType-"));
			core2pbcFileTypeDictionaryList.addAll(dictionaryService.getDictionaryLikeName("core2pbc-acctFileType2-"));
		}

	}

    @Override
	public AllBillsPublicDTO convertDictionary(Map<String, String> map) {
		initDics();
		log.info("字典转换对象中map中的数据{}", Arrays.asList(map));
		if(core2pbcMap == null){
			core2pbcMap = new HashMap<>();
		}
		for (DictionaryDto dictionaryDto : core2pbcDictionaryList) {
			log.debug("字典名称{}", dictionaryDto.getName());
			// 找到属性名称
			String[] name = dictionaryDto.getName().split("-", -1);
			String dictionaryName = name[1];

			log.debug("字段名{}", StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + dictionaryName));
			if (!StringUtils.startsWithAny(dictionaryName, new String[]{"acctFileType", "fileType"}) && !StringUtils.endsWithAny(dictionaryName,"IdcardType")) {
				// 属性名称转大写
				String fieldName = StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + dictionaryName);
//				changeColumnToDataInCache(str)
				// 在map里面找到值
				String value = map.get(fieldName);
				String newvalue = "";
				// 字典转换
				if (StringUtils.isNotBlank(value)) {
					newvalue = transalteInCache(dictionaryDto.getName(), value);
				}

				log.debug("处理前的值{}", value);
				log.debug("处理后的值{}", newvalue);
				// 将转换的值赋给map里面
				map.put(fieldName, newvalue);

				String acctTypeValue = map.get(StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + "acctType"));
				//账户小类无值才需要转换
				if (StringUtils.isBlank(acctTypeValue) && "acctBigType".equals(dictionaryName)) {
					String bigType = StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + dictionaryName);
					String bigTypeValue = map.get(bigType);
					if ("jiben".equals(bigTypeValue)) {
						map.put("yd_acct_type", CompanyAcctType.jiben.toString());
					} else if ("yiban".equals(bigTypeValue)) {
						map.put("yd_acct_type", CompanyAcctType.yiban.toString());
					} else if ("linshi".equals(bigTypeValue)) {
						map.put("yd_acct_type", CompanyAcctType.tempAcct.toString());
					} else if ("zhuanyong".equals(bigTypeValue)) {
						map.put("yd_acct_type", CompanyAcctType.specialAcct.toString());
					}else if ("yanzi".equals(bigTypeValue)) {
						map.put("yd_acct_type", CompanyAcctType.yanzi.toString());
					}else if ("zengzi".equals(bigTypeValue)) {
						map.put("yd_acct_type", CompanyAcctType.zengzi.toString());
					}else{
						throw new RuntimeException("T+1--map转AllBillsPublicDTO，大类转小类异常，大类项值为：jiben，yiban，linshi，zhunyong，yanzi，zengzi，当前acctBigType值：" + bigTypeValue);
					}
				}

				if ("regType".equals(dictionaryName)) {
					String parRegTypeField = StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + "parRegType");
					String value7 = map.get(parRegTypeField);
					String newvalue7 = "";
					if(StringUtils.isNotBlank(value7)){
						newvalue7 = transalteInCache(dictionaryDto.getName(), value7);
					}
					log.debug("处理前的值{}", value7);
					log.debug("处理后的值{}", newvalue7);
					map.put(parRegTypeField, newvalue7);
				}
			}

			// 处理一个字典多个属性转换问题
			//下列信息在一个字典中转化，没有单独的字典
			if ("personType".equals(dictionaryName)) {

				String legalIdcardTypeField = StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + "legalIdcardType");
				String value2 = map.get(legalIdcardTypeField);
				String newvalue2 = "";
				if(StringUtils.isNotBlank(value2)){
					newvalue2 = transalteInCache(dictionaryDto.getName(), value2);
				}
				log.debug("处理前的值{}", value2);
				log.debug("处理后的值{}", newvalue2);
				map.put(legalIdcardTypeField, newvalue2);

				String parLegalIdcardTypeField = StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + "parLegalIdcardType");
				String value3 = map.get(parLegalIdcardTypeField);
				String newvalue3 = "";
				if(StringUtils.isNotBlank(value3)){
					newvalue3 = transalteInCache(dictionaryDto.getName(), value3);
				}
				log.debug("处理前的值{}", value3);
				log.debug("处理后的值{}", newvalue3);
				map.put(parLegalIdcardTypeField, newvalue3);

				String fundManagerIdcardTypeField = StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + "fundManagerIdcardType");
				String value4 = map.get(fundManagerIdcardTypeField);
				String newvalue4 = "";
				if(StringUtils.isNotBlank(value4)){
					newvalue4 = transalteInCache(dictionaryDto.getName(), value4);
				}
				log.debug("处理前的值{}", value4);
				log.debug("处理后的值{}", newvalue4);
				map.put(fundManagerIdcardTypeField, newvalue4);

				String insideLeadIdcardTypeField = StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + "insideLeadIdcardType");
				String value5 = map.get(insideLeadIdcardTypeField);
				String newvalue5 = "";
				if(StringUtils.isNotBlank(value4)){
					newvalue5 = transalteInCache(dictionaryDto.getName(), value5);
				}
				log.debug("处理前的值{}", value5);
				log.debug("处理后的值{}", newvalue5);
				map.put(insideLeadIdcardTypeField, newvalue5);

				String nontmpLegalIdcardTypeField = StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + "nontmpLegalIdcardType");
				String value6 = map.get(nontmpLegalIdcardTypeField);
				String newvalue6 = "";
				if(StringUtils.isNotBlank(value4)){
					newvalue6 = transalteInCache(dictionaryDto.getName(), value6);
				}
				log.debug("处理前的值{}", value6);
				log.debug("处理后的值{}", newvalue6);
				map.put(nontmpLegalIdcardTypeField, newvalue6);
			}

		}
		// 处理fileType的字典转换
		for (DictionaryDto info : core2pbcFileTypeDictionaryList) {
			log.debug("字典名称{}", info.getName());
			String[] name = info.getName().split("-");

			// 得到账户类型
			String acctTypeStr = map.get(StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + "acctType"));

			if (StringUtils.isBlank(acctTypeStr)) {
				acctTypeStr = map.get(StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + "acctBigType"));
			}

			if (StringUtils.equals(acctTypeStr, name[2])) {
				if ("fileType".equals(name[1])) {
					String fileTypeField = StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + "fileType");
					String fileTypevalue = map.get(fileTypeField);
					String newvaluefileType = "";
					if (StringUtils.isNotBlank(fileTypevalue)) {
						newvaluefileType = transalteInCache(info.getName(), fileTypevalue);
					}
					log.debug("处理前的值{}", fileTypevalue);
					log.debug("处理后的值{}", newvaluefileType);
					map.put(fileTypeField, newvaluefileType);
				} else if ("fileType2".equals(name[1])) {
					String fileType2Field = StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + "fileType") + "2";
					String fileTypevalue2 = map.get(fileType2Field);
					String newvaluefileType2 = "";
					if (StringUtils.isNotBlank(fileTypevalue2)) {
						newvaluefileType2 = transalteInCache(info.getName(), fileTypevalue2);
					}
					log.debug("处理前的值{}", fileTypevalue2);
					log.debug("处理后的值{}", newvaluefileType2);
					map.put(fileType2Field, newvaluefileType2);
				} else if ("acctFileType".equals(name[1])) {
					String fileType2Field = StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + "acctFileType");
					String fileTypevalue2 = map.get(fileType2Field);
					String newvaluefileType2 = "";
					if (StringUtils.isNotBlank(fileTypevalue2)) {
						newvaluefileType2 = transalteInCache(info.getName(), fileTypevalue2);
					}
					log.debug("处理前的值{}", fileTypevalue2);
					log.debug("处理后的值{}", newvaluefileType2);
					map.put(fileType2Field, newvaluefileType2);
				} else if ("acctFileType2".equals(name[1])) {
					String fileType2Field = StringUtil.changeColumnToDataInCache(IdeaNamingStrategy.PREFIX + "acctFileType") + "2";
					String fileTypevalue2 = map.get(fileType2Field);
					String newvaluefileType2 = "";
					if (StringUtils.isNotBlank(fileTypevalue2)) {
						newvaluefileType2 = transalteInCache(info.getName(), fileTypevalue2);
					}
					log.debug("处理前的值{}", fileTypevalue2);
					log.debug("处理后的值{}", newvaluefileType2);
					map.put(fileType2Field, newvaluefileType2);
				}
			}
		}

		try {
			return (AllBillsPublicDTO) map2DomainService.converter(map, AllBillsPublicDTO.class);
		} catch (Exception e) {
			log.error("map转换为AllBillsPublicDTO异常", e);
			return null;
		}
	}

	/**
	*@Description: 转化使用cache
	*@Param: [name, value]
	*@return: java.lang.String
	*@Author: wanghongjie
	*@date: 2018/9/17
	*/
	private String transalteInCache(String name,String value){
		String key = name+"-"+value;
		if(core2pbcMap.containsKey(key)){
			return core2pbcMap.get(key);
		}else{
			String valueInDb = dictionaryService.transalteNull2Empty(name, value);
			core2pbcMap.put(key,valueInDb);
			return valueInDb;
		}
	}
}

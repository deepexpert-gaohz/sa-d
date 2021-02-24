package com.ideatech.ams.account.service.core;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.account.dao.core.CorePublicAccountErrorDao;
import com.ideatech.ams.account.dao.core.CorePublicAccountFinishDao;
import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.CorePublicAccountDto;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.entity.CorePublicAccountError;
import com.ideatech.ams.account.entity.CorePublicAccountFinish;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.*;
import com.ideatech.ams.account.service.AccountPublicService;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.account.validate.AllPublicAccountValidate;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsCheckResultInfo;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.system.configuration.dto.AccountConfigureDto;
import com.ideatech.ams.system.configuration.service.AccountConfigureService;
import com.ideatech.ams.system.dict.dto.OptionDto;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganRegisterDto;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganRegisterService;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.BeanValueUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.PrintUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vantoo
 * @date 2018/10/10 11:04 AM
 */
@Service
@Slf4j
public class TpoServiceImpl implements TpoService {

    private final static String TMP_EXTENSION = ".tmp";

    private final static String FINISH_EXTENSION = ".finish";

    private final static String TPO_AUTO_SYNC = "tpoAutoSyncEnabled";

    private static final String[] COMPARE_FIELDS = {"legalIdcardType", "legalIdcardNo", "legalIdcardDue", "legalTelephone", "orgType", "orgTypeDetail", "bankCardNo", "setupDate", "economyType",
            "industryCode", "regOffice", "economyIndustryCode", "economyIndustryName", "orgEccsNo", "orgStatus", "stateTaxRegNo", "stateTaxDue", "taxDue", "regType", "fileNo", "fileType",
            "fileSetupDate", "fileDue", "fileNo2", "fileType2", "fileSetupDate2", "fileDue2", "regCurrencyType", "businessScope", "corpScale", "taxRegNo", "regNo", "orgCode", "orgCodeDue",
            "regFullAddress", "zipcode", "workProvinceChname", "workCityChname", "workAreaChname", "workAddress", "workFullAddress", "telephone", "parAccountKey", "parOrgCode", "parOrgCodeDue",
            "parCorpName", "parOrgEccsNo", "parRegType", "parRegNo", "parLegalIdcardNo", "parLegalIdcardType", "parLegalIdcardDue", "parLegalType", "parLegalName", "parLegalTelephone", "acctNo",
            "accountName", "acctBigType", "acctType", "accountStatus", "remark", "effectiveDate", "bankCode", "bankName", "acctCreateReason", "capitalProperty", "enchashmentType", "currencyType",
            "fundManager", "fundManagerIdcardType", "fundManagerIdcardNo", "fundManagerIdcardDue", "fundManagerTelephone", "insideDeptName", "insideLeadName", "insideLeadIdcardType",
            "insideLeadIdcardNo", "insideLeadIdcardDue", "insideTelephone", "insideZipcode", "nontmpProjectName", "nontmpLegalName", "nontmpTelephone", "nontmpZipcode", "nontmpAddress",
            "nontmpLegalIdcardType", "nontmpLegalIdcardNo", "nontmpLegalIdcardDue", "acctFileType", "acctFileNo", "acctFileType2", "acctFileNo2", "accountNameFrom", "saccprefix", "saccpostfix",
            "accountKey", "basicAccountStatus", "basicBankCode", "basicBankName", "credentialType", "credentialNo", "credentialDue", "customerNo", "depositorName", "depositorType", "orgEnName",
            "regProvinceChname", "regCityChname", "regArea", "regAreaChname", "legalType", "legalName", "customerCategory"};

    @Value("${import.file.tpo.extension}")
    private String extension;

    @Value("${import.file.tpo.folder.begin}")
    private String beginFolder;

    @Value("${import.file.tpo.folder.finish}")
    private String finishFolder;

    @Value("${import.file.tpo.fields-always-full:true}")
    private boolean fieldsAlwaysFull;

    @Value("${import.file.tpo.split}")
    private String split;

    @Value("${import.file.tpo.charset}")
    private String charset;

    @Autowired
    private CoreFileBatchService coreFileBatchService;

    @Autowired
    private AmsCoreAccountService amsCoreAccountService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private CorePublicAccountFinishDao corePublicAccountFinishDao;

    @Autowired
    private CorePublicAccountErrorDao corePublicAccountErrorDao;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private UserService userService;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AccountPublicService accountPublicService;

    @Autowired
    private CustomerPublicService customerPublicService;

    @Autowired
    private PbcAmsService pbcAmsService;

    @Autowired
    private TransactionUtils transactionUtils;

    @Autowired
    private ConfigService configService;

    private Map<String, Integer> headMap;

    private Map<String, String> fieldMap;

    private Map<String, String> billTypeDicMap;

    private Map<String, String> accountStatusDicMap;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private OrganRegisterService organRegisterService;

    @Autowired
    private AccountConfigureService accountConfigureService;

    @Autowired
    private Map<String, AllPublicAccountValidate> validateMap;

    private Map<String, String> acctTypeDicMap;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private AmsMainService amsMainService;

    //是否上报信用代码证
    @Value("${ams.company.pbc.eccs}")
    private Boolean syncEccs;

    //修改无需上报字段继续上报人行 做标记  一般，非预算
    @Value("${ams.company.noChangeToSync:false}")
    private Boolean noChangeToSync;

    /**
     * 是否基于存量数据。否则变更、久悬、销户 对于无存量数据的情况下，直接新建客户账户信息
     */
    @Value("${ams.company.datenbestand:true}")
    private Boolean datenbestand;

    public TpoServiceImpl() {

        this.fieldMap = new CaseInsensitiveMap();
        ReflectionUtils.doWithFields(CorePublicAccountDto.class, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                fieldMap.put(field.getName(), field.getName());
            }
        });
    }

    @Override
    public void processTxtFile() {
        log.info("T+1文件读取路径：" + beginFolder);
        File beginFolderFile = new File(beginFolder);
        if (!beginFolderFile.exists()) {
            beginFolderFile.mkdirs();
            return;
        }

        for (File file : beginFolderFile.listFiles(new TpoFileFilter())) {
            //开始处理修改文件
            Long batchId = coreFileBatchService.create(file);
            File tmpFile = changeFile2Tmp(file);
            List<CorePublicAccountDto> corePublicAccountDtoList = batchSave(tmpFile, batchId);
            //处理核心文件原始数据
            processCoreData(corePublicAccountDtoList);
            //移除文件
            removeFile(tmpFile);
        }
    }


    @Transactional(rollbackFor = IOException.class)
    public List<CorePublicAccountDto> batchSave(File file, Long batchId) {
        log.info("清空map状态数据");
        clearDictionaryMap();
        log.info("设置map状态数据");
        putDictionaryMap();
        if (MapUtils.isNotEmpty(headMap)) {
            headMap.clear();
        }
        LineIterator iterator = null;
        CorePublicAccountDto corePublicAccountDto = null;
        List<CorePublicAccountDto> corePublicAccountDtoList = new ArrayList<>(16);
        int lineNum = 0;
        try {
            iterator = FileUtils.lineIterator(file, charset);
            while (iterator.hasNext()) {
                lineNum++;
                String line = iterator.nextLine();
                log.info("正在处理数据：" + line);
                String[] data = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, split);
                //读取表头
                if (MapUtils.isEmpty(headMap)) {
                    headMap = new HashMap<>(16);
                    for (int i = 0; i < data.length; i++) {
                        String field = StringUtils.trimToEmpty(data[i]);
                        headMap.put(field, i);
                    }
                    continue;
                } else {
                    //读取文件
                    try {
                        log.info("data数据转换corePublicAccountDto开始");
                        corePublicAccountDto = readLine2Bean(data);
                    } catch (Exception e) {
                        log.error("数据文件" + file.getName() + "第" + (lineNum) + "行内容转换出错，未保存！", e);
                        continue;
                    }
                    corePublicAccountDto.setBatchId(batchId);
                    corePublicAccountDto.setHandleStatus(CompanyIfType.No);

                    //开始处理逐条数据
                    corePublicAccountDtoList.add(corePublicAccountDto);
                }
            }
        } catch (IOException e) {
            log.error("文件处理异常", e);
        } finally {
            iterator.close();
        }

        return corePublicAccountDtoList;
    }


    private CorePublicAccountDto readLine2Bean(String[] data) throws InvocationTargetException, IllegalAccessException {
        CorePublicAccountDto corePublicAccountDto = new CorePublicAccountDto();
        for (Map.Entry<String, Integer> headEntry : headMap.entrySet()) {
            String field = StringUtils.trimToEmpty(headEntry.getKey());
            int index = headEntry.getValue();
            String value = data[index];
            Object obj = new Object();
            try{
                if (StringUtils.isNotBlank(value)) {
                    //去除空格
                    value = StringUtils.trimToEmpty(value);
                    //特殊字段转换
                    if (StringUtils.equalsIgnoreCase(field, "registeredCapital")) {
                        if (NumberUtils.isNumber(value)) {
                            obj = new BigDecimal(value);
                        }
                    } else {
                        obj = value;
                    }
                    //该字段模板中为cancelData
                    if (StringUtils.equalsIgnoreCase(field, "cancelDate")) {
                        BeanUtils.setProperty(corePublicAccountDto, "acctCancelReason", obj);
                    } else {
                        BeanUtils.setProperty(corePublicAccountDto, fieldMap.get(field), obj);
                    }
                }
            }catch (Exception e) {
                throw new BizServiceException(EErrorCode.SYSTEM_ERROR,"下标" + index + "字段：" + field + "，值："+ value +"，转换异常。。。");
            }
        }
        //新老数据的机构转换，此处为核心新数据格式，需要根据organFullId给organCode赋值
        String organCode = corePublicAccountDto.getOrganCode();
        if (StringUtils.isBlank(organCode)) {
            organCode = corePublicAccountDto.getOrganFullId();
        }
        corePublicAccountDto.setOrganCode(organCode);
        corePublicAccountDto.setOrganFullId(organCode);

        return corePublicAccountDto;

    }

    private void processCoreData(List<CorePublicAccountDto> corePublicAccountDtoList) {
        AllBillsPublicDTO billsPublic = null;
        putDictionaryMap();

        for (CorePublicAccountDto corePublicAccountDto : corePublicAccountDtoList) {
            billsPublic = new AllBillsPublicDTO();

            //校验
            log.info("T+1数据进行校验...");
            if (!validate(corePublicAccountDto)) {
                continue;
            }

            //操作类型
            BillType billType = null;
            AccountStatus accountStatus = null;
            try {
                if (MapUtils.isNotEmpty(accountStatusDicMap)) {
                    String accountStatusStr = accountStatusDicMap.get(corePublicAccountDto.getAccountStatus());
                    if (StringUtils.isNotBlank(accountStatusStr)) {
                        accountStatus = AccountStatus.valueOf(accountStatusStr);
                    }

                }
                if (MapUtils.isNotEmpty(billTypeDicMap)) {
                    String billTypeStr = billTypeDicMap.get(corePublicAccountDto.getBillType());
                    if (StringUtils.isNotBlank(billTypeStr)) {
                        billType = BillType.valueOf(billTypeStr);
                    }
                }

            } catch (IllegalArgumentException e) {
                //ignore
                log.info("账号{}转换账户状态或操作类型时发生错误...",corePublicAccountDto.getAcctNo());
            }

            //1.不包含操作类型
            if (billType == null) {
                if (!fieldsAlwaysFull) {
                    log.info("账号{}未提供操作类型的数据，必须包含所有字段！",corePublicAccountDto.getAcctNo());
                    corePublicAccountDto.setErrorReason("未提供操作类型的数据，必须包含所有字段！");
                    saveErrorData(corePublicAccountDto);
                    continue;
                }

                //2.比对分析出操作类型
                if (accountStatus != null) {
                    if (accountStatus == AccountStatus.suspend) {
                        billType = BillType.ACCT_SUSPEND;
                    } else if (accountStatus == AccountStatus.revoke) {
                        billType = BillType.ACCT_REVOKE;
                    } else if (accountStatus == AccountStatus.normal) {
                        billType = getBillTypeByDbData(corePublicAccountDto);
                    }
                } else {
                    //如果没有操作类型时，账户状态必填
                    log.info("账号{}没有操作类型时，必须提供账户状态字段！",corePublicAccountDto.getAcctNo());
                    corePublicAccountDto.setErrorReason("没有操作类型时，必须提供账户状态字段！");
                    saveErrorData(corePublicAccountDto);
                    continue;
                }
            }

            //无需处理的数据
            if (billType == BillType.ACCT_INIT) {
                continue;
            }
            if (billType == null) {
                corePublicAccountDto.setErrorReason("必须提供操作类型字段！");
                saveErrorData(corePublicAccountDto);
                continue;
            }

            if(StringUtils.isBlank(corePublicAccountDto.getAcctNo())){
                corePublicAccountDto.setErrorReason("必须提供账号！");
                saveErrorData(corePublicAccountDto);
                continue;
            }

            if (billType == BillType.ACCT_OPEN) {
                if(StringUtils.isBlank(corePublicAccountDto.getDepositorName())){
                    log.info("账号{}开户操作时，必须提供存款人名称！",corePublicAccountDto.getAcctNo());
                    corePublicAccountDto.setErrorReason("必须提供存款人名称！");
                    saveErrorData(corePublicAccountDto);
                    continue;
                }
            }

            //判断本地业务表有无数据
            AccountsAllInfo dbAccountsAllInfo = accountsAllService.findByAcctNo(corePublicAccountDto.getAcctNo());
            if (dbAccountsAllInfo != null) {
                if (billType == BillType.ACCT_OPEN && dbAccountsAllInfo.getAccountStatus() != AccountStatus.revoke) {
                    log.info("T+1数据账号{}本地已存在相同账号数据，请勿重复开户！",corePublicAccountDto.getAcctNo());
                    corePublicAccountDto.setErrorReason("本地已存在相同账号数据，请勿重复开户！");
                    saveErrorData(corePublicAccountDto);
                    continue;
                }
                //即使存在数据，也需要判断历史数据的状态
                if (billType == BillType.ACCT_SUSPEND) {
                    //增加久悬转正常的判断
                    if (dbAccountsAllInfo.getAccountStatus() != AccountStatus.normal) {
                        log.info("T+1数据账号{}本地该数据状态非正常，无法处理久悬操作！",corePublicAccountDto.getAcctNo());
                        corePublicAccountDto.setErrorReason("本地该数据状态非正常，无法处理久悬操作！");
                        saveErrorData(corePublicAccountDto);
                        continue;
                    }
                }

                //变更需要状态是正常或（未激活[仅核准类]）
                if (billType == BillType.ACCT_CHANGE) {
                    //增加久悬转正常的判断
                    if (dbAccountsAllInfo.getAccountStatus() != AccountStatus.normal && !(dbAccountsAllInfo.getAccountStatus() == AccountStatus.suspend && accountStatus == AccountStatus.normal)) {
                        log.info("T+1数据账号{}本地该数据状态非正常，无法处理变更操作！",corePublicAccountDto.getAcctNo());
                        corePublicAccountDto.setErrorReason("本地该数据状态非正常，无法处理变更操作！");
                        saveErrorData(corePublicAccountDto);
                        continue;
                    }
                }



                if (billType == BillType.ACCT_REVOKE) {
                    if (dbAccountsAllInfo.getAccountStatus() == AccountStatus.revoke) {
                        log.info("T+1数据账号{}本地次账号已销户，请勿重复销户！！",corePublicAccountDto.getAcctNo());
                        corePublicAccountDto.setErrorReason("本地次账号已销户，请勿重复销户！");
                        saveErrorData(corePublicAccountDto);
                        continue;
                    }
                }
            } else {
                if (billType == BillType.ACCT_CHANGE || billType == BillType.ACCT_SUSPEND || billType == BillType.ACCT_REVOKE) {
                    log.info("T+1数据账号{}本地不存在此账号数据，无法做其他操作！",corePublicAccountDto.getAcctNo());
                    corePublicAccountDto.setErrorReason("本地不存在此账号数据，无法做其他操作！");
                    saveErrorData(corePublicAccountDto);
                    continue;
                }
            }

            //数据调用具体服务
            try {
                log.info("T+1数据账号{}开始调用上报方法。",corePublicAccountDto.getAcctNo());
                sync(corePublicAccountDto, billType);
            } catch (Exception e) {
                log.error("核心数据处理异常", e);

                //如果是上报失败，则更新数据
                if (e instanceof BizServiceException) {
                    if (((BizServiceException) e).getError() == EErrorCode.PBC_SYNC_FAILURE) {
                        //更新核心表数据
                        updateCoreData(corePublicAccountDto);
                        continue;
                    }
                }
                corePublicAccountDto.setErrorReason(e.getMessage());
                saveErrorData(corePublicAccountDto);
                continue;
            }
        }
    }

    private void printSystemConfigure(){
        //打印系统配置项
        log.info("是否上报信用代码证:" + syncEccs);
        log.info("是否基于存量数据：" + datenbestand);
    }

    private String beanName2DbName(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        if (!name.startsWith("yd_")) {
            name = "yd_" + name;
        }
        StringBuilder builder = new StringBuilder(name);
        for (int i = 1; i < builder.length() - 1; i++) {
            if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i),
                    builder.charAt(i + 1))) {
                builder.insert(i++, '_');
            }
        }
        return builder.toString().toLowerCase();
    }

    private boolean isUnderscoreRequired(char before, char current, char after) {
        return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after);
    }

    private void sync(CorePublicAccountDto corePublicAccountDto, BillType billType) {

        printSystemConfigure();

        AllBillsPublicDTO waitingSupplementDto = new AllBillsPublicDTO();
        //接口方式数据直接获取虚拟用户
        final UserDto userDto = userService.findVirtualUser();
        try {
            //获取是否自动上报
            boolean tpoAutoSync = true;

            //数据是否需要报备开关
            boolean realSync = true;
            //销户是否上报
            boolean hezhunRovike = true;

            List<ConfigDto> tpoAutoSyncConfig = configService.findByKey(TPO_AUTO_SYNC);
            if (CollectionUtils.isNotEmpty(tpoAutoSyncConfig)) {
                tpoAutoSync = Boolean.valueOf(tpoAutoSyncConfig.get(0).getConfigValue()).booleanValue();
            }
            log.info("T+1数据是否自动上报：" + tpoAutoSync);

            Map<String, String> map = BeanUtils.describe(corePublicAccountDto);

            Map<String, String> convertMap = new HashMap<>(map.size());
            //此处map的key加上yd_
            for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                convertMap.put(beanName2DbName(stringStringEntry.getKey()), stringStringEntry.getValue());
            }

            //转换数据为接口处理对象
            log.info("T+1：map转AllBillsPublicDTO对象...");
            final AllBillsPublicDTO billsPublic = amsCoreAccountService.convertDictionary(convertMap);

            billsPublic.setBillType(com.ideatech.ams.account.enums.bill.BillType.valueOf(billType.name()));

            OrganizationDto organizationDto = null;
            String organCode = billsPublic.getOrganCode();

            // 处理账户名称
            log.info("T+1：处理账户名称...");
            allBillsPublicService.setAcctName(billsPublic);

            //判断机构，账户是否取消开户核准
            log.info("T+1开始查询是否取消核准类上报账户......");
            //判断该机构是否走取消核准接口
            try{
                allBillsPublicService.setCancelHeZhun(billsPublic);
            }catch (RuntimeException e){
                throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, e.getMessage());
            }


            //保存
            organizationDto = validateOrgan(organCode);
            AccountsAllInfo accountsAllInfo = null;
            if (billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_CHANGE) {
                accountsAllInfo = accountsAllService.findByAcctNo(billsPublic.getAcctNo());
                //验资户或者增资户的变正常
                if (accountsAllInfo != null && billsPublic.getAcctType() == CompanyAcctType.jiben && (accountsAllInfo.getAcctType() == CompanyAcctType.zengzi || accountsAllInfo.getAcctType() == CompanyAcctType.yanzi)) {
                    //增资验资变更时判断基本户是否存在
                    if (!allBillsPublicService.checkJibenByCustomerNoOrDepositorName(billsPublic.getCustomerNo(), billsPublic.getDepositorName())) {
                        throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "该客户已经存在基本户账户，无法变更为基本户");
                    } else {
                        billsPublic.setBillType(com.ideatech.ams.account.enums.bill.BillType.ACCT_OPEN);
                    }
                } else if (accountsAllInfo != null && accountsAllInfo.getAccountStatus() == AccountStatus.suspend && billsPublic.getAccountStatus() == AccountStatus.normal) {
                    //久悬转正常
                    log.info("T+1：久悬转正常...");
                    billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
                    billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
                    realSync = false;
                } else {
                    //核准类判断当前开户是否有未完成流水,有责表示人行未核准，此处需要覆盖上笔流水并将流水类型修改为开户，且人行上报状态重置为未同步
                    //（注意：此处只有当字段为全量时才生效，即 fieldsAlwaysFull = true）
                    //取消核准后  基本户  非临时 不算核准类账户
                    if (billsPublic.getAcctType().isHeZhun() && (billsPublic.getCancelHeZhun() == null || !billsPublic.getCancelHeZhun()) && fieldsAlwaysFull) {
                        //查询人行不存在
                        try{
                            AmsAccountInfo amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organizationDto.getId(), billsPublic.getAcctNo());
                            if (amsAccountInfo == null) {
                                AccountBillsAllInfo latestUnfinishedByAcctNo = accountBillsAllService.findLatestUnfinishedByAcctNo(billsPublic.getAcctNo(), com.ideatech.ams.account.enums.bill.BillType.ACCT_OPEN);
                                if (latestUnfinishedByAcctNo != null) {
                                    billsPublic.setId(latestUnfinishedByAcctNo.getId());
                                    //流水数据初始化
                                    allBillsPublicService.setSyncStatusByOldBill(latestUnfinishedByAcctNo, billsPublic);
                                    //流水类型变为开户
                                    billsPublic.setBillType(com.ideatech.ams.account.enums.bill.BillType.ACCT_OPEN);
                                    billsPublic.setPbcSyncStatus(CompanySyncStatus.weiTongBu);

                                    AccountsAllInfo dbAccountInfo = accountsAllService.findByRefBillId(latestUnfinishedByAcctNo.getId());
                                    if (dbAccountInfo != null) {
                                        billsPublic.setAccountStatus(dbAccountInfo.getAccountStatus());
                                    }
                                }
                            }
                        }catch (Exception e){
                            log.error("查询人行或未完成流水报错",e);
                        }
                    }
                }
            }

            log.info("T+1:初始化billsPublic默认字段...");
            allBillsPublicService.initBillsPublic(billsPublic, organizationDto, BillFromSource.CORE);
            transactionUtils.executeInNewTransaction(new TransactionCallback() {
                @Override
                public void execute() throws Exception {
                    AllBillsPublicDTO originalBills = new AllBillsPublicDTO();
                    if (billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_CHANGE) {
                        originalBills = allBillsPublicService.changeCompareWithOriginal(billsPublic);
                    }
                    //如果是销户，cancelDate字段没有值的话  默认赋值当天日期
                    if(billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_REVOKE){
                        if(StringUtils.isBlank(billsPublic.getCancelDate())){
                            billsPublic.setCancelDate(DateUtils.DateToStr(new Date(),"yyyy-MM-dd"));
                        }
                    }
                    Long billId = allBillsPublicService.save(billsPublic, userDto, true);
                    billsPublic.setId(billId);
                    if (billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_CHANGE) {
                        List<String> changeColumnList = allBillsPublicService.changeCompareWithOld(originalBills, billsPublic);
                        PrintUtils.printObjectColumn(billsPublic);
                    }
                }
            });

            if(billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_REVOKE){
                try {
                    if(billsPublic.getAcctType()==CompanyAcctType.tempAcct || billsPublic.getAcctType()==CompanyAcctType.specialAcct){
                        AmsAccountInfo amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organizationDto.getId(), billsPublic.getAcctNo());
                        if(amsAccountInfo!=null){
                            log.info("A、{}账号销户时账户性质小类输入为空，人行查询账户性质:{}",billsPublic.getAcctNo(),amsAccountInfo.getAcctType().getFullName());
                            if(amsAccountInfo.getAcctType()!=AccountType.feiyusuan && amsAccountInfo.getAcctType()!=AccountType.feilinshi){
                                billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
                                billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
                                billsPublic.setPbcCheckStatus(CompanyAmsCheckStatus.NoCheck);
                                realSync = false;
                                hezhunRovike = false;
                            }
                        }else{
                            billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
                            billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
                            billsPublic.setPbcCheckStatus(CompanyAmsCheckStatus.NoCheck);
                            realSync = false;
                            hezhunRovike = false;
                            log.info("B、{}查询内容为空，默认该账号是核准类，销户不上报",billsPublic.getAcctNo());
                        }
                    }
                }catch (Exception e){
                    log.error("{}销户处理异常:{}",billsPublic.getAcctNo(),e);
                }

            }
//            //校验上报对象是否符合上报规则
//            try {
//                log.info("开户前校验是否满足人行上报条件");
//                AllPublicAccountValidate allPublicAccountValidate = validateMap.get(billsPublic.takeValidateName());
//                allPublicAccountValidate.validate(billsPublic);
//            }catch (Exception e) {
//                log.error("数据校验异常：" + e.getMessage() + ",数据默认进入待补录");
//                allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
//                return;
//            }

            log.info("T+1：修改状态为：APPROVED");
            allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVED, userDto.getId(), "");

            //赋值给dto
            BeanCopierUtils.copyProperties(billsPublic, waitingSupplementDto);

            //本地账户数据存在时账户性质小类赋值
            if (billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_CHANGE || billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_SUSPEND
                    || billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_REVOKE) {
                if (StringUtils.isNotBlank(billsPublic.getAcctNo())) {
                    AccountPublicInfo info = accountPublicService.findByAcctNo(billsPublic.getAcctNo());
                    if (info != null && billsPublic.getAcctType() == null) {
                        billsPublic.setAcctType(info.getAcctType());
                    } else {
                        if (StringUtils.isBlank(billsPublic.getAcctName())) {
                            billsPublic.setAcctName(billsPublic.getDepositorName());
                        }
                    }
                }
            }
            //久悬转正常进上报成功并结束流水
            boolean suspend2normal = false;
            //当久悬转正常时，在保存流水时人行审核状态等会置为 未同步，流水完成后再次去判断
            if (accountsAllInfo != null && accountsAllInfo.getAccountStatus() == AccountStatus.suspend && billsPublic.getAccountStatus() == AccountStatus.normal) {
                log.info("T+1：久悬转正常，在保存流水时人行审核状态等会置为 未同步，流水完成后再次去判断...");
                //久悬转正常  人行，信用代码无需上报   人行无需审核
                billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
                billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
                billsPublic.setPbcCheckStatus(CompanyAmsCheckStatus.NoCheck);
                realSync = false;
                suspend2normal = true;
            }
            if (billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_OPEN && (billsPublic.getAcctType() == CompanyAcctType.yanzi || billsPublic.getAcctType() == CompanyAcctType.zengzi)) {
                billsPublic.setEccsSyncStatus(CompanySyncStatus.buTongBu);
                billsPublic.setPbcSyncStatus(CompanySyncStatus.buTongBu);
                billsPublic.setAccountStatus(AccountStatus.normal);
                realSync = false;
            }

            //核准类销户无需上报
            if (billsPublic.getAcctType().isHeZhun() && billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_REVOKE) {
                log.info("T+1：销户判断是否上报...");
                if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun() && (billsPublic.getAcctType() == CompanyAcctType.jiben || billsPublic.getAcctType() == CompanyAcctType.feilinshi)){
                    realSync = true;
                }else{
                    realSync = false;
                }
            }

            log.info("T+1查询接口上报模式");
            //判断接口上报模式
//            List<ConfigDto> configDtoList = configService.findByKey("syncStatus");
            //修改接口上报模式，根据业务的操作类型进行区分
            List<ConfigDto> configDtoList = null;
            if(billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_OPEN){
                configDtoList = configService.findByKey("openSyncStatus");
            }
            if(billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_CHANGE){
                configDtoList = configService.findByKey("changeSyncStatus");
            }
            if(billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_SUSPEND){
                configDtoList = configService.findByKey("suspendSyncStatus");
            }
            if(billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_REVOKE){
                configDtoList = configService.findByKey("revokeSyncStatus");
            }
            if(billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_EXTENSION){
                configDtoList = configService.findByKey("ExtensionSyncStatus");
            }
            //如果上报接口模式是空或者上报模式是autoSync  维持原有上报
            if(CollectionUtils.isEmpty(configDtoList) || (CollectionUtils.isNotEmpty(configDtoList) && "autoSync".equals(configDtoList.get(0).getConfigValue()))){
                log.info("T+1维持原有接口上报模式");
//                allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVED, userDto.getId(), "");
            } else {
                //所有接口进来的数据先进去待审核列表
                //autoSync：维持原来的上报模式   beforeCheckSync ： 所以数据进去待审核 ；  cancelHeZhunSync：取消核准数据进去待审核
                ConfigDto configDto = configDtoList.get(0);
                log.info("T+1接口上报模式为" + configDto.getConfigValue());
                if("beforeCheckSync".equals(configDto.getConfigValue())){
                    if(!suspend2normal && hezhunRovike){
                        allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVING, userDto.getId(), "");
                    }
                    realSync = false;
                }
                if("cancelHeZhunSync".equals(configDto.getConfigValue())){
                    log.info("T+1判断取消核准进入待审核列表。。。");
                    if(billsPublic.getCancelHeZhun() != null && billsPublic.getCancelHeZhun()){
                        log.info(billsPublic.getDepositorName() + "是取消核准制数据，进入待审核列表");
                        if(!suspend2normal){
                            allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVING, userDto.getId(), "");
                        }
                        realSync = false;
                    }else{
                        log.info(billsPublic.getDepositorName() + "不是取消核准制数据，维持原有接口上报模式");
                        allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.APPROVED, userDto.getId(), "");
                        realSync = true;
                    }
                }
            }

            log.info("人行上报状态：" + billsPublic.getPbcSyncStatus());
            log.info("信用上报状态：" + billsPublic.getEccsSyncStatus());
            if (billsPublic.getPbcSyncStatus() == CompanySyncStatus.buTongBu && billsPublic.getEccsSyncStatus() == CompanySyncStatus.buTongBu) {
                realSync = false;
            }
            try {
                //开户原因校验
                if (billsPublic.getBillType() == com.ideatech.ams.account.enums.bill.BillType.ACCT_OPEN) {
                    if (StringUtils.isNotBlank(billsPublic.getAcctCreateReason()) && StringUtils.isNotBlank(billsPublic.getAcctFileType()) && billsPublic.getAcctType() == CompanyAcctType.feilinshi) {
                        if ("1".equals(billsPublic.getAcctCreateReason())) {
                            if ("14".equals(billsPublic.getAcctFileType()) || "15".equals(billsPublic.getAcctFileType())) {
                            } else {
                                log.info("开户原因与证明文件不对应");
                                throw new BizServiceException(EErrorCode.PBC_SYNC_ACCTTYPE_ERROR, "开户原因与证明文件不对应！");
                            }
                        } else if ("2".equals(billsPublic.getAcctCreateReason())) {
                            if (!"16".equals(billsPublic.getAcctFileType())) {
                                log.info("开户原因与证明文件不对应");
                                throw new BizServiceException(EErrorCode.PBC_SYNC_ACCTTYPE_ERROR, "开户原因与证明文件不对应！");
                            }
                        }
                    }
                    //非预算开户证明文件1种类和编号必输
                    if (billsPublic.getAcctType() == CompanyAcctType.feiyusuan) {
                        if (StringUtils.isBlank(billsPublic.getAcctFileType())) {
                            log.info("开户证明文件种类1不能为空");
                            throw new BizServiceException(EErrorCode.PBC_SYNC_ACCTTYPE_ERROR, "开户证明文件种类1不能为空");
                        }
                        if (StringUtils.isBlank(billsPublic.getAcctFileNo())) {
                            log.info("开户证明文件编号1不能为空");
                            throw new BizServiceException(EErrorCode.PBC_SYNC_ACCTTYPE_ERROR, "开户证明文件编号1不能为空");
                        }
                        if (StringUtils.isBlank(billsPublic.getZipcode())) {
                            log.info("邮政编码不能为空");
                            throw new BizServiceException(EErrorCode.PBC_SYNC_ACCTTYPE_ERROR, "邮政编码不能为空");
                        }
                        if (!isPhone(billsPublic.getTelephone())) {
                            log.info("企业联系电话为空或者格式不对");
                            throw new BizServiceException(EErrorCode.PBC_SYNC_ACCTTYPE_ERROR, "企业联系电话为空或者格式不对");
                        }
                    }
                }
            } catch (Exception e) {
                log.info("T+1处理上报异常，数据存放至待补录", e);
                allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
                return;
            }
            if (realSync) {
                //根据上报状态来判断是否需要上报
                boolean isSyncAms = allBillsPublicService.getSyncStatus(EAccountType.AMS, billsPublic);
                boolean isSyncEccs = allBillsPublicService.getSyncStatus(EAccountType.ECCS, billsPublic);
                log.info("T+1：数据需要上报状态：isSyncAms" + isSyncAms + ";isSyncEccs：" + isSyncEccs);
                if ((isSyncAms || isSyncEccs) && tpoAutoSync) {
                    log.info("人行上报开始...");
                    allBillsPublicService.syncAndUpdateStaus(isSyncAms, isSyncEccs, billsPublic, userDto, CompanySyncOperateType.autoSyncType);
                } else {
                    allBillsPublicService.updateApproveStatus(billsPublic, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
                }
            } else {
                if(noChangeToSync && (billsPublic.getAcctType() == CompanyAcctType.yiban)){// || billsPublic.getAcctType() == CompanyAcctType.feiyusuan
                    //备案类
                    pbcAmsService.amsAccountSyncChangeAgain(organizationDto,billsPublic.getAcctNo());
                    billsPublic.setPbcSyncStatus(CompanySyncStatus.tongBuChengGong);
                    allBillsPublicService.updateSyncStatusAndFinishBill(billsPublic, userDto.getId());
                }else{
                    //数据无需报送，直接结束
                    allBillsPublicService.updateSyncStatusAndFinishBill(billsPublic, userDto.getId());
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            if(e.getMessage().contains("大类转小类异常")){
                corePublicAccountDto.setErrorReason(e.getMessage());
                saveErrorData(corePublicAccountDto);
            }else if(e.getMessage().contains("人行查询异常")){
                corePublicAccountDto.setErrorReason(e.getMessage());
                saveErrorData(corePublicAccountDto);
            }else{
                log.info("T+1处理上报异常，数据存放至待补录", e);
                allBillsPublicService.updateApproveStatus(waitingSupplementDto, BillStatus.WAITING_SUPPLEMENT, userDto.getId(), "");
            }
        } finally {
            //更新核心表数据
            updateCoreData(corePublicAccountDto);
        }
    }

    private  boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            log.info("手机号应为11位数");
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (!isMatch) {
                log.info("请填入正确的手机号");
            }
            return isMatch;
        }
    }
    /**
     * 校验机构信息
     *
     * @param organCode
     * @throws BizServiceException
     */
    private OrganizationDto validateOrgan(String organCode) throws BizServiceException {
        OrganizationDto organizationDto = organizationService.findByCode(organCode);
        if (organizationDto == null) {
            throw new BizServiceException(EErrorCode.ORGAN_NOTCONFIG, "未配置机构");
        }
        return organizationDto;
    }


    private boolean validate(CorePublicAccountDto corePublicAccountDto) {
        if (StringUtils.isBlank(corePublicAccountDto.getAcctNo())) {
            corePublicAccountDto.setErrorReason("必须提供账号字段！");
            saveErrorData(corePublicAccountDto);
            return false;
        }

        if (StringUtils.isBlank(corePublicAccountDto.getOrganCode())) {
            corePublicAccountDto.setErrorReason("必须提供机构字段！");
            saveErrorData(corePublicAccountDto);
            return false;
        }

        if (StringUtils.isBlank(corePublicAccountDto.getCustomerNo())) {
            corePublicAccountDto.setErrorReason("必须提供客户号字段！");
            saveErrorData(corePublicAccountDto);
            return false;
        }

        return true;
    }

    /**
     * 数据比对查看字段是否有变动
     *
     * @param corePublicAccountDto
     * @return
     */
    private BillType getBillTypeByDbData(CorePublicAccountDto corePublicAccountDto) {
        //从finish表中查询数据是否存在
        CorePublicAccountFinish corePublicAccountFinish = corePublicAccountFinishDao.findByAcctNo(corePublicAccountDto.getAcctNo());
        if (corePublicAccountFinish == null) {
            return BillType.ACCT_OPEN;
        } else {
            //判断数据是否有变动，否则为存量垃圾数据
            CorePublicAccountDto dbData = ConverterService.convert(corePublicAccountFinish, CorePublicAccountDto.class);
            String[] notMatchFields = compare(dbData, corePublicAccountDto);
            if (notMatchFields.length > 0) {
                List<String> notMatchFieldList = Arrays.asList(notMatchFields);
                // 继续处理，插入核心表
                corePublicAccountDto.setChangeFieldsStr(Arrays.toString(notMatchFields));
                //机构变更、久悬激活、存款人类别、这三种不支持变更，需要先销户再开户
                //这类数据保存至核心表但是不操作，保存该数据下次比对使用
                if (notMatchFieldList.contains("organCode")) {
                    updateCoreData(corePublicAccountDto);
                    corePublicAccountDto.setErrorReason("机构变更数据需要先销户后开户，不支持自动处理");
                    saveErrorData(corePublicAccountDto);
                    return BillType.ACCT_INIT;
                }
                if (notMatchFieldList.contains("depositorType")) {
                    updateCoreData(corePublicAccountDto);
                    corePublicAccountDto.setErrorReason("存款人类别变更数据需要先销户后开户，不支持自动处理");
                    saveErrorData(corePublicAccountDto);
                    return BillType.ACCT_INIT;
                }
                if (notMatchFieldList.contains("accountStatus")) {
                    //久悬激活
                    updateCoreData(corePublicAccountDto);
                    corePublicAccountDto.setErrorReason("久悬激活需要先销户后开户，不支持自动处理");
                    saveErrorData(corePublicAccountDto);
                    return BillType.ACCT_INIT;
                }
                return BillType.ACCT_CHANGE;
            }
            log.info("数据未做任何变化，不作处理");
            return BillType.ACCT_INIT;
        }
    }


    private String[] compare(CorePublicAccountDto dto1, CorePublicAccountDto dto2) {
        Set<String> list = new HashSet<>(16);
        // BigDecimal,另做处理，如果不一致体现在返回参数中
        if (dto1.getRegisteredCapital() != null && dto2.getRegisteredCapital() != null) {
            BigDecimal c1bd = dto1.getRegisteredCapital();
            BigDecimal c2bd = dto2.getRegisteredCapital();
            if (c1bd.compareTo(c2bd) != 0) {
                list.add("registeredCapital");
            }
        } else {
            list.add("registeredCapital");
        }

        for (String field : COMPARE_FIELDS) {
            Object object1 = BeanValueUtils.getValue(dto1, field);
            Object object2 = BeanValueUtils.getValue(dto2, field);
            object1 = ObjectUtils.defaultIfNull(object1, "");
            object2 = ObjectUtils.defaultIfNull(object2, "");
            if (!ObjectUtils.equals(object1, object2)) {
                list.add(field);
            }
        }
        String[] result = new String[list.size()];
        return list.toArray(result);
    }

    private void saveErrorData(CorePublicAccountDto corePublicAccountDto) {
        log.error(corePublicAccountDto.getErrorReason());
        log.info("T+1数据{}处理异常，保存错误信息表；",corePublicAccountDto.toString());
        CorePublicAccountError corePublicAccountError = ConverterService.convert(corePublicAccountDto, CorePublicAccountError.class);
        corePublicAccountErrorDao.save(corePublicAccountError);
    }


    /**
     * 更新核心数据
     *
     * @param corePublicAccountDto
     */
    private void updateCoreData(CorePublicAccountDto corePublicAccountDto) {
        //TODO 此处字段可能会被置为null
        CorePublicAccountFinish corePublicAccountFinish = ConverterService.convert(corePublicAccountDto, CorePublicAccountFinish.class);
        corePublicAccountFinishDao.save(corePublicAccountFinish);
    }

    private void clearDictionaryMap() {
        if (MapUtils.isNotEmpty(billTypeDicMap)) {
            billTypeDicMap.clear();
        }
        if (MapUtils.isNotEmpty(accountStatusDicMap)) {
            accountStatusDicMap.clear();
        }
        if (MapUtils.isNotEmpty(acctTypeDicMap)) {
            acctTypeDicMap.clear();
        }

    }

    private void putDictionaryMap() {
        if (MapUtils.isEmpty(billTypeDicMap)) {
            billTypeDicMap = new HashMap<>(16);
            //获取操作类型字典
            List<OptionDto> options = dictionaryService.listOptionByDictName("core2pbc-billType");
            for (OptionDto option : options) {
                billTypeDicMap.put(option.getName(), option.getValue());
            }
        }
        if (MapUtils.isEmpty(accountStatusDicMap)) {
            accountStatusDicMap = new HashMap<>(16);
            //获取操作类型字典
            List<OptionDto> options = dictionaryService.listOptionByDictName("core2pbc-accountStatus");
            for (OptionDto option : options) {
                accountStatusDicMap.put(option.getName(), option.getValue());
            }
        }
        if (MapUtils.isEmpty(acctTypeDicMap)) {
            acctTypeDicMap = new HashMap<>(16);
            //获取操作类型字典
            List<OptionDto> options = dictionaryService.listOptionByDictName("core2pbc-acctType");
            for (OptionDto option : options) {
                acctTypeDicMap.put(option.getName(), option.getValue());
            }
        }
    }


    private File changeFile2Tmp(File file) {
        File tmpFile = new File(file.getParent(), FilenameUtils.getBaseName(file.getName()) + TMP_EXTENSION);
        file.renameTo(tmpFile);
        return tmpFile;
    }

    private void removeFile(File file) {
        File finishFile = new File(file.getParent(), FilenameUtils.getBaseName(file.getName()) + "-" + System.currentTimeMillis() + FINISH_EXTENSION);
        file.renameTo(finishFile);
        try {
            FileUtils.moveFileToDirectory(finishFile, new File(finishFolder), true);
        } catch (IOException e) {
            log.error("文件移除异常", e);
        }
    }

    //备案类账户人行补全字段
//    public void pbc2ConverDto(AllBillsPublicDTO billsPublic,List<String> changeFile,String organCode){
//       OrganizationDto organizationDto = validateOrgan(organCode);
//        //备案类账户进行人行查询  覆盖除变更字段外其余字段  备案类账户全字段上传
//        try{
//            //找出变更字段，人行覆盖其他字段
//            List<String> changeAmsSyncFieldList = null;
////            if(fieldsAlwaysFull){
//                //查询人行信息
//                AmsAccountInfo amsAccountInfo = null;
//                try {
//                    amsAccountInfo = pbcAmsService.getAmsAccountInfoByAcctNo(organizationDto.getId(), billsPublic.getAcctNo());
//                }catch (Exception e){
//                    log.info("账号：" + billsPublic.getAcctNo() + "查询人行详情失败！" + e.getMessage());
//                    throw new RuntimeException("账号：" + billsPublic.getAcctNo() + "查询人行详情失败！");
//                }
////                AmsAccountInfo amsAccountInfo = JSON.parseObject("{\"accountKey\":\"J3910017538803\",\"accountLicenseNo\":\"J3910017538803\",\"accountStatus\":\"normal\",\"acctCreateDate\":\"2013-07-15\",\"acctName\":\"福州合众人力资源服务有限公司\",\"acctNo\":\"100050803350010001\",\"acctType\":\"jiben\",\"bankCode\":\"313391080015\",\"bankName\":\"福建海峡银行股份有限公司营业部\",\"businessScope\":\"人力资源服务；劳务派遣（不含涉外业务）；人力资源信息咨询；室内保洁服务；企业事务代理；家政服务（不含职业中介）；劳务分包；劳动政策咨询；劳务事务代理；装卸和搬运；货物运输代理服务；物业管理；仓储服务；快递服务；寄递服务（信件和其它具有信件性质的物品除外）；会议及展览服务；企业管理信息咨询；市场调查；财税信息咨询；企业营销策划；教育信息咨询；通信工程的设计、施工；计算机信息技术服务；通讯设备的批发、代购代销、维修；第二类增值电信业务中的信息服务业；提供法律信息咨询。（依法须经批准的项目，经相关部门批准后方可开展经营活动）\",\"cancelDate\":\"-\",\"depositorName\":\"福州合众人力资源服务有限公司\",\"depositorType\":\"企业法人\",\"fileNo\":\"91350102MA2XNJQH25\",\"fileNo2\":\"\",\"fileType\":\"工商营业执照\",\"fileType2\":\"\",\"industryCode\":\"居民服务和其他服务业\",\"legalIdcardNo\":\"350102198507051937\",\"legalIdcardType\":\"身份证\",\"legalName\":\"周云峰\",\"noTaxProve\":\"\",\"orgCode\":\"MA2XNJQH2\",\"parAccountKey\":\"\",\"parCorpName\":\"\",\"parLegalIdcardNo\":\"\",\"parLegalIdcardType\":\"\",\"parLegalName\":\"\",\"parOrgCode\":\"\",\"regAddress\":\"福建省福州市鼓楼区温泉街道东大路36号花开富贵1#A座19层14E室\",\"regAreaCode\":\"391000\",\"regCurrencyType\":\"人民币\",\"registeredCapital\":\"2000000.00\",\"stateTaxRegNo\":\"91350102MA2XNJQH25\",\"taxRegNo\":\"91350102MA2XNJQH25\",\"telephone\":\"13625028113\",\"zipCode\":\"350000\"}",AmsAccountInfo.class);
//                //人行数据补t+1文件除变更字段外的字段
//                if(amsAccountInfo != null){
//                    //人行obj转map  方便拿值
//                    Map<String, String>  amsMap = org.apache.commons.beanutils.BeanUtils.describe(amsAccountInfo);
//                    //根据账户性质获取变更上报字段
//                    changeAmsSyncFieldList = allBillsPublicService.getChangeFieldsNeedPbcSync(billsPublic);
//                    try{
//                        //去掉变更字段  覆盖其余人行字段
//                        changeAmsSyncFieldList.removeAll(changeFile);
//                        for(String converField : changeAmsSyncFieldList){
//                            String pbcValue = amsMap.get(converField) == null ? "" : amsMap.get(converField).toString();
//                            if(StringUtils.isNotBlank(pbcValue)){
//                                if("acctFileNo".equals(converField)){
//                                    pbcValue = amsMap.get(converField) == null ? "" : amsMap.get("accountFileNo").toString();
//                                }else if("acctFileNo2".equals(converField)){
//                                    pbcValue = amsMap.get(converField) == null ? "" : amsMap.get("accountFileNo2").toString();
//                                }else if("regFullAddress".equals(converField)){
//                                    //人行字段为regAddress
//                                    pbcValue = amsMap.get("regAddress") == null ? "" : amsMap.get("regAddress").toString();
//                                }else{
//                                    //下拉框字段转义
//                                    pbcValue = getPbc2DtoValue(pbcValue,billsPublic,converField);
//                                }
//                            }
//                            //人行字段赋值给DTO
//                            if(StringUtils.isNotBlank(pbcValue)){
//                                BeanValueUtils.setValue(billsPublic,converField,pbcValue);
//                            }
//                        }
//                    }catch (Exception e){
//                        throw new RuntimeException("备案类账户人行字段覆盖DTO字段错误");
//                    }
//
//                }
////            }
//        }catch (Exception e){
//            log.error("变更备案类账户，账号：" + billsPublic.getAcctNo() ,e.getMessage());
//            throw new BizServiceException(EErrorCode.PBC_SYNC_ACCTTYPE_ERROR, "变更备案类账户失败，账号：" + billsPublic.getAcctNo() + "," + e.getMessage());
//        }
//    }

//    public String getPbc2DtoValue(String pbcValue,AllBillsPublicDTO dto,String converField){
//        if(dto.getAcctType() == CompanyAcctType.yiban) {
//            if("acctFileType".equals(converField)){
//                pbcValue = dictionaryService.transalteLike("证明文件1种类(一般户)",pbcValue);
//            }
//        }
//        if(dto.getAcctType() == CompanyAcctType.feiyusuan) {
//            if("acctFileType".equals(converField)){
//                pbcValue = dictionaryService.transalteLike("证明文件1种类(专用户)",pbcValue);
//            }
//            if("acctFileType2".equals(converField)){
//                pbcValue = dictionaryService.transalteLike("证明文件2种类(专用户)",pbcValue);
//            }
//        }
//        if(dto.getAcctType() == CompanyAcctType.feilinshi) {
//            if("acctFileType".equals(converField)){
//                pbcValue = dictionaryService.transalteLike("证明文件1种类(非临时)",pbcValue);
//            }
//        }
//        if("fileType".equals(converField)){
//            pbcValue = dictionaryService.transalteLike("证明文件1种类(基本户)",pbcValue);
//        }
//        if("fileType2".equals(converField)){
//            pbcValue = dictionaryService.transalteLike("证明文件2种类(基本户)",pbcValue);
//        }
//        //法人证件类型
//        if("legalIdcardType".equals(converField)){
//            pbcValue = dictionaryService.transalteLike("法人身份证件类型",pbcValue);
//        }
//        //币种
//        if("regCurrencyType".equals(converField)){
//            pbcValue = dictionaryService.transalteLike("注册币种",pbcValue);
//        }
//        //行业归属
//        if("industryCode".equals(converField)){
//            pbcValue = dictionaryService.transalteLike("行业归属",pbcValue);
//        }
//        //账户名称构成方式
//        if("accountNameFrom".equals(converField)){
//            pbcValue = dictionaryService.transalteLike("账户构成方式",pbcValue);
//        }
//        //资金性质
//        if("capitalProperty".equals(converField)){
//            pbcValue = dictionaryService.transalteLike("资金性质",pbcValue);
//        }
//        //上级法人证件类型
//        if("parLegalIdcardType".equals(converField)){
//            pbcValue = dictionaryService.transalteLike("法人身份证件类型",pbcValue);
//        }
//        return pbcValue;
//    }

    class TpoFileFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return (FilenameUtils.isExtension(name, extension));
        }
    }
}

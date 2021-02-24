package com.ideatech.ams.account.spi.processor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.*;
import com.ideatech.ams.account.service.AccountChangeSummaryService;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.bill.BillNoSeqService;
import com.ideatech.ams.account.util.Map2DomainUtils;
import com.ideatech.ams.customer.dto.CompanyPartnerInfo;
import com.ideatech.ams.customer.dto.RelateCompanyInfo;
import com.ideatech.ams.system.area.service.AreaService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.whitelist.dto.WhiteListDto;
import com.ideatech.ams.system.whitelist.service.WhiteListService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

public class BasicAllPublicAccountFormDataProcessor extends AbstractAllPublicAccountFormProcessor {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private AllBillsPublicService billsPublicService;

    @Autowired
    private BillNoSeqService billNoSeqService;

    @Value("${ams.company.check:true}")
    private Boolean isCheck;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private ConfigService configService;

    @Autowired
    protected AccountChangeSummaryService accountChangeSummaryService;

    @Autowired
    private WhiteListService whiteListService;

    @Value("${ams.company.pbc.eccs:true}")
    private boolean eccsSyncEnabled;

    /**
     * 预约新接口模式是否启用：默认false不启用
     */
    @Value("${apply.newRule.flag:false}")
    private Boolean applyNewRuleFlag;

    @Override
    protected AllBillsPublicDTO doProcess(UserDto userInfo, Map<String, String> formData) {
        Boolean isValidate = true; //是否校验必填字段
        Long accountId = null;
        AllBillsPublicDTO dataAccount = new AllBillsPublicDTO();
        Long orgId = userInfo.getOrgId();
        OrganizationDto organizationDto = organizationService.findById(orgId);

        if (StringUtils.isBlank(organizationDto.getFullId())) {
            throw new RuntimeException("账号对应机构fullId不能为空");
        }

        try {
            logger.info("===============formMap    start======================");
            Iterator<Map.Entry<String, String>> entries = formData.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                logger.info( entry.getKey() + " = " + entry.getValue());
            }
            logger.info("===============formMap    end======================");

            //取账户信息
            dataAccount = (AllBillsPublicDTO) Map2DomainUtils.converter(formData, AllBillsPublicDTO.class);
            //取股东信息
            String name;
            Set set = new HashSet<CompanyPartnerInfo>();
            for (int i = 0 ;i<10;i++){
                name = formData.get("companyPartnerInfoSet["+i+"].name");
                if (StringUtils.isBlank(name)){
                    break;
                }
                CompanyPartnerInfo companyPartnerInfo1 = new CompanyPartnerInfo();
                companyPartnerInfo1.setName(name);
                companyPartnerInfo1.setPartnerType(formData.get("companyPartnerInfoSet["+i+"].partnerType"));
                companyPartnerInfo1.setRoleType(formData.get("companyPartnerInfoSet["+i+"].roleType"));
                companyPartnerInfo1.setIdcardType(formData.get("companyPartnerInfoSet["+i+"].idcardType"));
                companyPartnerInfo1.setIdcardNo(formData.get("companyPartnerInfoSet["+i+"].idcardNo"));
                companyPartnerInfo1.setPartnerTelephone(formData.get("companyPartnerInfoSet["+i+"].partnerTelephone"));
                companyPartnerInfo1.setString001(i+"");
                set.add(companyPartnerInfo1);
            }

            dataAccount.setCompanyPartners(set);


            //获取关联企业信息
            String relateCompanyName;
            Set relateCompanyInfoSet = new HashSet<RelateCompanyInfo>();
            for (int i = 0; i < 10; i++) {
                //如果名称是空的，则表示无数据，不用添加
                relateCompanyName = formData.get("relateCompanyInfoSet[" + i + "].relateCompanyName");
                if (StringUtils.isBlank(relateCompanyName)) {
                    break;
                }

                RelateCompanyInfo relateCompanyInfo = new RelateCompanyInfo();
                relateCompanyInfo.setRelateCompanyName(relateCompanyName);
                relateCompanyInfo.setCompanyCertificateType(formData.get("relateCompanyInfoSet[" + i + "].companyCertificateType"));
                relateCompanyInfo.setCompanyCertificateNo(formData.get("relateCompanyInfoSet[" + i + "].companyCertificateNo"));
                //relateCompanyInfo.setCompanyCertificateName(formData.get("RelateCompanyInfoSet[" + i + "].companyCertificateType"));
                //relateCompanyInfo.setCompanyOrgCode(formData.get("RelateCompanyInfoSet[" + i + "].companyCertificateType"));
                relateCompanyInfo.setCompanyLegalName(formData.get("relateCompanyInfoSet[" + i + "].companyLegalName"));
                relateCompanyInfo.setCompanyOrgEccsNo(formData.get("relateCompanyInfoSet[" + i + "].companyOrgEccsNo"));
                relateCompanyInfo.setString001(i + "");
                relateCompanyInfoSet.add(relateCompanyInfo);
            }
            dataAccount.setRelateCompanys(relateCompanyInfoSet);


            if (dataAccount.getId() != null && dataAccount.getId() == 0) {
                dataAccount.setId(null);
            }
        } catch (Exception e) {
            logger.error("表单数据转为对象失败", e);
            throw new RuntimeException("表单数据转为对象失败，失败原因：" + e.getMessage());
        }

        setOrganInfo(organizationDto, dataAccount); // 机构信息
        setAcctName(dataAccount); // 账户名称
        setEnchashmentType(dataAccount); // 取现标识

        doBeforeSave(dataAccount, formData, userInfo); //保存前的状态操作

        //保存取消核准账户变更基本户核准编号时的日期
        saveString006(dataAccount,formData);

        //客户的证明文件默认为证明文件1的值
        dataAccount.setCredentialType(dataAccount.getFileType());
        dataAccount.setCredentialNo(dataAccount.getFileNo());

        dataAccount.setCredentialDue(dataAccount.getFileDue());

        if (StringUtils.equals(formData.get("action"), "keepForm")) { //保持时不校验
            isValidate = false;
        }

        // 编辑
        if (StringUtils.isNotBlank(formData.get("id"))) {
            // AmsAccount amsAccount = null; accountId
            accountId = new Long(formData.get("id"));
            dataAccount.setId(accountId);
            dataAccount.setAcctId(new Long(formData.get("accountId")));
            if (StringUtils.equals(formData.get("action"), "verifyForm")) {
                //审核上报判断账号是否重复
                AccountsAllInfo byAcctNo = accountsAllService.findByAcctNoAndIdNot(dataAccount.getAcctNo(),new Long(formData.get("accountId")));
                if (byAcctNo != null) {
                    //开户操作切本地已销户
                    if(dataAccount.getBillType() == BillType.ACCT_OPEN && byAcctNo.getAccountStatus() == AccountStatus.revoke) {

                    } else {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "账号" + dataAccount.getAcctNo() + "在系统中已经存在,无需重复添加");
                    }
                }
            }

            try {
                billsPublicService.save(dataAccount, userInfo, isValidate);
            } catch (Exception e) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,e.getMessage());
            }
        } else {// 新增
            if (StringUtils.isNotBlank(dataAccount.getAcctNo())) {
                if (dataAccount.getBillType() == BillType.ACCT_OPEN) {
                    AccountsAllInfo byAcctNo = accountsAllService.findByAcctNo(dataAccount.getAcctNo());
                    if (byAcctNo != null && byAcctNo.getAccountStatus() != AccountStatus.revoke) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "账号" + dataAccount.getAcctNo() + "在系统中已经存在,无需重复添加");
//                        throw new RuntimeException("账号" + dataAccount.getAcctNo() + "在系统中已经存在,无需重复添加");
                    }
                }
                if (dataAccount.getBillType() == BillType.ACCT_CHANGE) {
                    String changeFields = formData.get("changeFields");
                    if (StringUtils.isNotBlank(changeFields)) {
                        dataAccount.setChangeFields(changeFields);
                    }
                }
            }

            if (StringUtils.isBlank(dataAccount.getBillNo())) {
                // 单据编号
                String billNo = billNoSeqService.getBillNo(DateUtils.DateToStr(new Date(), "yyyyMMdd"),
                        organizationDto.getCode(), BillTypeNo.valueOf(dataAccount.getBillType().name()));
                dataAccount.setBillNo(billNo);
            }
            try {
                Long id = billsPublicService.save(dataAccount, userInfo, isValidate);
                dataAccount.setId(id);
            } catch (Exception e) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, e.getMessage());
            }

        }

        if (StringUtils.equals(formData.get("action"), "verifyForm")) {
                if (StringUtils.isNotBlank(formData.get("isSyncAms")) && formData.get("isSyncAms").equals("true")
                        || StringUtils.isNotBlank(formData.get("isSyncEccs")) && formData.get("isSyncEccs").equals("true")) {
                    formData.put("action", "syncForm");// 设置按钮为上报
                } else { //审核通过未上报操作
                    if(StringUtils.isNotBlank(dataAccount.getPreOpenAcctId()) && dataAccount.getBillType() == BillType.ACCT_OPEN && !applyNewRuleFlag) {
                        billsPublicService.sendModifyStatus(dataAccount, "BANK_SUCCESS");
                    }
                }
                dataAccount.setStatus(BillStatus.APPROVED);
        }

        return dataAccount;
    }

    private void setOrganInfo(OrganizationDto organ, AllBillsPublicDTO account) {
        if (StringUtils.isBlank(account.getOrganFullId())) {
            account.setOrganFullId(organ.getFullId());
        }
        if (StringUtils.isBlank(account.getCustOrganFullId())) {
            account.setCustOrganFullId(organ.getFullId());
        }
        if (StringUtils.isBlank(account.getAcctOrgFullid())) {
            account.setAcctOrgFullid(organ.getFullId());
        }
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
                    entity.setAcctName(entity.getDepositorName() + entity.getInsideDeptName());
                } else {
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
                    entity.setAcctName(entity.getSaccprefix() + entity.getDepositorName()
                            + entity.getSaccpostfix());
                }
            } else {
                if (StringUtils.isNotEmpty(entity.getDepositorName())) {
                    entity.setAcctName(entity.getDepositorName());
                } else {
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
                    entity.setAcctName(entity.getDepositorName() + entity.getNontmpProjectName());
                } else {
                    entity.setAcctName("");
                }
            } else {
                if (StringUtils.isNotEmpty(entity.getDepositorName())) {
                    entity.setAcctName(entity.getDepositorName());
                } else {
                    entity.setAcctName("");
                }
            }
        } else {
            if (StringUtils.isNotEmpty(entity.getDepositorName())) {
                entity.setAcctName(entity.getDepositorName());
            } else {
                entity.setAcctName("");
            }
        }
    }

    // 根据资金性质设置取现标识
    public static void setEnchashmentType(AllBillsPublicDTO entity) {
        if (StringUtils.isNotBlank(entity.getCapitalProperty())) {
            if (entity.getCapitalProperty().equals("14") || entity.getCapitalProperty().equals("04")
                    || entity.getCapitalProperty().equals("11") || entity.getCapitalProperty().equals("13")
                    || entity.getCapitalProperty().equals("10")) {
                entity.setEnchashmentType("1");
            } else {
                entity.setEnchashmentType("0");
            }
        }
    }

    protected void doBeforeSave(AllBillsPublicDTO account, Map<String, String> formData, UserDto user) {
        if (StringUtils.isBlank(formData.get("action"))) {
            throw new RuntimeException("按钮操作为空,请联系系统管理员");
        }
        setRegCHName(account);// 设置中文名称
        if (StringUtils.equals(formData.get("action"), "rejectForm")) {
            //account.setDenyReason("");// 退回原因
            account.setStatus(BillStatus.REJECT);
        }/*
         * else if (StringUtils.equals(formData.get("action"), "addInfoForm")) {
         * account.setStatus(BillStatus.Reject); // account.set("");// 自动上报失败原因为空 }
         */else if (StringUtils.equals(formData.get("action"), "verifyForm")) {
            account.setStatus(BillStatus.APPROVED);
            account.setApproveDate(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()));
            account.setApprover(user.getId());
            account.setApproveDesc(formData.get("approveDesc"));
        } else if (StringUtils.equals(formData.get("action"), "denyForm")) {
            account.setStatus(BillStatus.REJECT);
        } else if (StringUtils.equals(formData.get("action"), "keepForm")) {
            account.setStatus(BillStatus.NEW);
        }

        /*
         * if (StringUtils.isBlank(formData.get("syncCheckStatus")) &&
         * StringUtils.equals(formData.get("action"), "keepForm")) { account.setStatus(BillStatus.New);
         * } else if(StringUtils.isBlank(formData.get("syncCheckStatus")) &&
         * !StringUtils.equals(formData.get("action"), "keepForm")) {
         * account.setStatus(BillStatus.Approving); }
         */
        if (StringUtils.isBlank(formData.get("syncCheckStatus"))
                && StringUtils.equals(formData.get("action"), "saveForm")) {
            //提交审批
            if (StringUtils.equals(formData.get("status"), "Reject")
                    && StringUtils.equals(formData.get("billType"), "ACCT_CHANGE")) {
                //变更驳回提交时逻辑处理需要判断状态，直接提交
                account.setSubmitApproveFlag("yes");
            } else {
                if(!StringUtils.equals(formData.get("status"), "WAITING_SUPPLEMENT")) {
                    if(formData.get("method") != null){
                        account.setStatus(BillStatus.APPROVED);
                    }else{
                        account.setStatus(BillStatus.APPROVING);
                    }
                }else{
                    //纯接口上报模式  待补录状态提交  如果上报方式需要审核后进行上报则把状态修改为审核中
                    List<ConfigDto> configDtoList = null;
                    if(StringUtils.equals(formData.get("billType"), "ACCT_OPEN")){
                        configDtoList = configService.findByKey("openSyncStatus");
                    }
                    if(StringUtils.equals(formData.get("billType"), "ACCT_CHANGE")){
                        configDtoList = configService.findByKey("changeSyncStatus");
                    }
                    if(StringUtils.equals(formData.get("billType"), "ACCT_SUSPEND")){
                        configDtoList = configService.findByKey("suspendSyncStatus");
                    }
                    if(StringUtils.equals(formData.get("billType"), "ACCT_REVOKE")){
                        configDtoList = configService.findByKey("revokeSyncStatus");
                    }
                    if(StringUtils.equals(formData.get("billType"), "ACCT_EXTENSION")){
                        configDtoList = configService.findByKey("ExtensionSyncStatus");
                    }

                    if(CollectionUtils.isNotEmpty(configDtoList)){
                        ConfigDto configDto = configDtoList.get(0);
                        if(!"autoSync".equals(configDto.getConfigValue())){
                            account.setStatus(BillStatus.APPROVING);
                        }
                    }

                }
            }

            if(!isCheck) {   //无审核版本提交后状态为审核通过
                account.setStatus(BillStatus.APPROVED);
            }
        }

//        if (StringUtils.isBlank(formData.get("pbcCheckStatus"))
//                || StringUtils.equals(formData.get("pbcCheckStatus"), "null")) { // 账户在人行审核通过前，如果经浏览器改后，会在浏览器显示为null,从而导致amsCheckStatus为空
//            account.setPbcCheckStatus(CompanyAmsCheckStatus.WaitCheck);
//        }

        if ("yiban".equals(formData.get("acctType")) || "feiyusuan".equals(formData.get("acctType"))
                || "ACCT_SUSPEND".equals(formData.get("billType"))
                || "ACCT_REVOKE".equals(formData.get("billType"))) {
            account.setPbcCheckStatus(CompanyAmsCheckStatus.NoCheck);
        }

        if (StringUtils.equals(formData.get("billType"), "ACCT_CHANGE")
                || StringUtils.equals(formData.get("billType"), "ACCT_SUSPEND")
                || StringUtils.equals(formData.get("billType"), "ACCT_REVOKE")) {
//            if (BussUtils.isHeZhunAccount(SyncAcctType.str2enum(formData.get("acctType")))
//                    && account.getPbcSyncStatus() == null && "ACCT_REVOKE".equals(formData.get("billType"))) {
//                account.setPbcSyncStatus(CompanySyncStatus.buTongBu);
//                account.setEccsSyncStatus(CompanySyncStatus.buTongBu);
//            } else
            if (account.getPbcSyncStatus() == null) {
                account.setPbcSyncStatus(CompanySyncStatus.weiTongBu);
            }
//            if(StringUtils.equals(formData.get("billType"), "ACCT_REVOKE") && account.getAcctType().isHeZhun()){
//                account.setPbcSyncStatus(CompanySyncStatus.buTongBu);
//            }

            if (account.getPbcSyncMethod() == null) {
                account.setPbcSyncMethod(CompanySyncOperateType.personSyncType);
            }

            if (account.getAcctType() == CompanyAcctType.jiben && eccsSyncEnabled == true) {  //信用机构上报模式
                if (account.getEccsSyncStatus() == null) {
                    account.setEccsSyncStatus(CompanySyncStatus.weiTongBu);
                }
            } else {
                account.setEccsSyncStatus(CompanySyncStatus.buTongBu);
            }
        } else if (StringUtils.equals(formData.get("billType"), "ACCT_OPEN")) {
            if (account.getAcctType() != CompanyAcctType.jiben ||
                    (account.getAcctType() == CompanyAcctType.jiben && eccsSyncEnabled == false)) {   //信用机构无需上报模式
                account.setEccsSyncStatus(CompanySyncStatus.buTongBu);
            }
        }
        //提交时，如果人行上报状态和信用代码上报状态为上报失败，，重置为位上报
        if(StringUtils.equals(formData.get("action"), "saveForm")){
            if(StringUtils.equals(formData.get("pbcSyncStatus"), "tongBuShiBai")){
                account.setPbcSyncStatus(CompanySyncStatus.weiTongBu);
            }
            if(StringUtils.equals(formData.get("eccsSyncStatus"), "tongBuShiBai")){
                account.setEccsSyncStatus(CompanySyncStatus.weiTongBu);
            }
        }
        //白名单人行不审核
        WhiteListDto whiteListDto = whiteListService.getByEntnameAndOrgId(account.getDepositorName(), user.getOrgId());
        if(whiteListDto != null && !"delete".equals(whiteListDto.getStatus())){
            account.setPbcCheckStatus(CompanyAmsCheckStatus.NoCheck);
            account.setPbcSyncStatus(CompanySyncStatus.buTongBu);
            account.setEccsSyncStatus(CompanySyncStatus.buTongBu);
            account.setWhiteList("1");
        }

        account.setCoreDataCompleted(CompanyIfType.Yes);// 数据是否完整 0：否 1：是
    }

    private void setRegCHName(AllBillsPublicDTO account) {
        if (StringUtils.isNotBlank(account.getRegProvince())) {
            account.setRegProvinceChname(areaService.getAreaNameByAreaCode(account.getRegProvince()));
        }
        if (StringUtils.isNotBlank(account.getRegCity())) {
            account.setRegCityChname(areaService.getAreaNameByAreaCode(account.getRegCity()));
        }
        if (StringUtils.isNotBlank(account.getRegArea())) {
            account.setRegAreaChname(areaService.getAreaNameByAreaCode(account.getRegArea()));
        }
    }

    protected void saveString006(AllBillsPublicDTO account, Map<String, String> formData) {
        String[] cancelHeZhunType = {"企业法人", "非法人企业", "有字号的个体工商户", "无字号的个体工商户", "01", "02", "13", "14"};
        //当变更的字段中有核准号的时候，保存变更日期（取消核准）
        String changeFields = formData.get("changeFields");
        //判断时候有accountKey的字段变更
        if(StringUtils.isNotBlank(changeFields)){
            if(changeFields.contains("accountKey") && Arrays.asList(cancelHeZhunType).contains(account.getDepositorType())){
                if (StringUtils.isNotBlank(changeFields)) {
                    JSONObject jsonObject = JSON.parseObject(changeFields);
                    String oldValue = jsonObject.get("accountKey").toString();
                    String newValue = account.getAccountKey();
                    if(!StringUtils.equals(oldValue,newValue)){
                        account.setString006(DateUtils.DateToStr(new Date(),"yyyy-MM-dd"));
                    }
                }
            }
        }
    }
}

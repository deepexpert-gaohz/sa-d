package com.ideatech.ams.account.enums.bill;

import org.apache.commons.lang.StringUtils;

public enum EnameToCnameEnum {
    operatorIdcardNo("业务经办人证件号码"),
    operatorIdcardDue("业务经办人证件有效日期"),
    operatorTelephone("业务经办人联系电话"),
    remark("备注"),
    refBillId("关联单据ID"),
    string001("扩展字段1"),
    string002("扩展字段2"),
    string003("扩展字段3"),
    string004("扩展字段4"),
    string005("扩展字段5"),
    string006("扩展字段6"),
    string007("扩展字段7"),
    string008("扩展字段8"),
    string009("扩展字段9"),
    string010("扩展字段10"),
    preLogId("前一笔日志记录ID"),
    accountId("账户ID"),
    acctNo("账号"),
    acctName("账户名称"),
    acctShortName("存款人简称"),
    accountLicenseNo("账号核准号"),
    accountClass("账户分类"),
    accountStatus("账户状态"),
    currencyType("币种"),
    acctCreateDate("账户开户日期"),
    acctActiveDate("账户激活日期"),
    effectiveDate("账户有效期"),
    acctCreateReason("开户原因"),
    cancelDate("账户销户日期"),
    acctCancelReason("销户原因"),
    acctSuspenDate("久悬日期"),
    bankCode("开户银行金融机构编码"),
    bankName("开户银行名称"),
    customerId("客户ID"),
    customerNo("客户号"),
    organFullId("完整机构ID"),
    acctBigType("账户性质大类"),
    acctType("非预算单位专用存款账户"),
    depositorType("存款人类别"),
    acctFileType("开户证明文件种类1"),
    acctFileNo("开户证明文件编号1"),
    acctFileType2("开户证明文件种类2"),
    acctFileNo2("开户证明文件编号2"),
    accountNameFrom("账户名称构成方式"),
    saccprefix("账户前缀"),
    saccpostfix("账户后缀"),
    capitalProperty("资金性质"),
    enchashmentType("取现标识"),
    fundManager("资金管理人姓名"),
    fundManagerIdcardType("资金管理人身份证种类"),
    fundManagerIdcardNo("资金管理人身份证编号"),
    fundManagerIdcardDue("资金管理人证件到期日"),
    fundManagerTelephone("资金管理人联系电话"),
    insideDeptName("内设部门名称"),
    insideLeadName("内设部门负责人名称"),
    insideLeadIdcardType("内设部门负责人身份种类"),
    insideLeadIdcardNo("内设部门负责人身份编号"),
    insideLeadIdcardDue("负责人证件到期日"),
    insideTelephone("内设部门联系电话"),
    insideZipcode("内设部门邮编"),
    insideAddress("内设部门地址"),
    nontmpProjectName("非临时项目部名称"),
    nontmpLegalName("非临时负责人姓名"),
    nontmpTelephone("非临时联系电话"),
    nontmpZipcode("非临时邮政编码"),
    nontmpAddress("非临时地址"),
    nontmpLegalIdcardType("非临时身份证件种类"),
    nontmpLegalIdcardNo("非临时身份证件编号"),
    nontmpLegalIdcardDue("非临时身份证件到期日"),
    operatorName("业务经办人姓名"),
    operatorIdcardType("业务经办人证件类型"),
    sequence("序号"),
    status("更新状态"),
    description("更新描述"),
    depositorName("存款人名称"),
    customerClass("客户类型"),
    credentialType("证件类型"),
    credentialNo("证件号码"),
    credentialDue("证件到期日"),
    sealName("客户公章名称"),
    orgEnName("机构英文名称"),
    regCountry("注册国家代码"),
    regProvince("注册省份"),
    regProvinceChname("注册地区省份中文名"),
    regCity("注册城市"),
    regCityChname("注册城市中文名"),
    regArea("注册地区"),
    regAreaChname("注册地区中文名"),
    regAreaCode("注册地地区代码"),
    regAddress("注册详细地址"),
    regFullAddress("完整注册地址"),
    industryCode("行业归属"),
    regOffice("登记部门"),
    regType("工商注册类型"),
    regNo("工商注册编号"),
    fileNo("证明文件1编号"),
    fileType("证明文件1种类"),
    fileSetupDate("证明文件1设立日期"),
    fileDue("证明文件1到期日"),
    fileNo2("证明文件2编号"),
    fileType2("证明文件2种类"),
    fileSetupDate2("证明文件2设立日期"),
    fileDue2("证明文件2到期日"),
    setupDate("成立日期"),
    businessLicenseNo("营业执照号码"),
    businessLicenseDue("营业执照到期日"),
    isIdentification("未标明注册资金"),
    regCurrencyType("注册资本币种"),
    registeredCapital("注册资金（元）"),
    businessScope("经营（业务）范围"),
    businessScopeEccs("经营（业务）范围"),
    legalType("法人类型"),
    legalName("法人姓名"),
    legalIdcardType("法人证件类型"),
    legalIdcardNo("法人证件编号"),
    legalIdcardDue("法人证件到期日"),
    legalTelephone("法人联系电话"),
    orgCode("组织机构代码"),
    orgCodeDue("组织机构代码证件到期日"),
    orgEccsNo("机构信用代码"),
    orgStatus("机构状态"),
    orgType("组织机构类别"),
    orgTypeDetail("组织机构类别细分"),
    interbankNo("同业金融机构编码"),
    noTaxProve("无需办理税务登记证的文件或税务机关出具的证明"),
    stateTaxRegNo("纳税人识别号（国税）"),
    stateTaxDue("国税证件到期日"),
    taxRegNo("纳税人识别号（地税）"),
    taxDue("地税证件到期日"),
    workCountry("办公国家代码"),
    workProvince("办公省份"),
    workProvinceChname("办公地区省份中文名"),
    workCity("办公城市"),
    workCityChname("办公城市中文名"),
    workArea("办公地区"),
    workAreaChname("办公地区中文名"),
    workAddress("办公详细地址"),
    workFullAddress("完整办公地址(与营业执照一致)"),
    isSameRegistArea("是否与与注册地址一致"),
    telephone("联系电话"),
    zipcode("邮政编码"),
    financeName("财务主管姓名"),
    financeTelephone("财务部联系电话"),
    financeIdcardNo("财务主管身份证号"),
    economyType("经济类型"),
    economyIndustryCode("经济行业分类Code"),
    economyIndustryName("经济行业分类"),
    accountKey("基本开户许可核准号"),
    basicAccountStatus("基本户状态"),
    basicAcctRegArea("基本户注册地地区代码"),
    basicBankCode("基本户开户银行金融机构编码"),
    basicBankName("基本户开户银行名称"),
    bankCardNo("贷款卡编码"),
    corpScale("企业规模"),
    parAccountKey("上级基本户开户许可核准号"),
    parCorpName("上级机构名称"),
    parLegalIdcardNo("上级法人证件号码"),
    parLegalIdcardType("上级法人证件类型"),
    parLegalIdcardDue("上级法人证件到期日"),
    parLegalName("上级法人姓名"),
    parLegalType("上级法人类型"),
    parLegalTelephone("上级法人联系电话"),
    parOrgCode("上级组织机构代码"),
    parOrgCodeDue("上级组织机构代码证到期日"),
    parOrgEccsNo("上级机构信用代码"),
    parOrgEccsDue("上级机构信用代码证到期日"),
    parRegType("上级登记注册号类型"),
    parRegNo("上级登记注册号码"),

    companyPartnerInfoSet0roleType("股东1关系人类型"),
    companyPartnerInfoSet0partnerType("股东1高管/股东"),
    companyPartnerInfoSet0idcardType("股东1证件类型"),
    companyPartnerInfoSet0name("股东1姓名"),
    companyPartnerInfoSet0idcardNo("股东1证件号码"),
    companyPartnerInfoSet0partnerTelephone("股东1联系电话"),

    companyPartnerInfoSet1roleType("股东2关系人类型"),
    companyPartnerInfoSet1partnerType("股东2高管/股东"),
    companyPartnerInfoSet1idcardType("股东2证件类型"),
    companyPartnerInfoSet1name("股东2姓名"),
    companyPartnerInfoSet1idcardNo("股东2证件号码"),
    companyPartnerInfoSet1partnerTelephone("股东2联系电话"),

    companyPartnerInfoSet2roleType("股东3关系人类型"),
    companyPartnerInfoSet2partnerType("股东3高管/股东"),
    companyPartnerInfoSet2idcardType("股东3证件类型"),
    companyPartnerInfoSet2name("股东3姓名"),
    companyPartnerInfoSet2idcardNo("股东3证件号码"),
    companyPartnerInfoSet2partnerTelephone("股东3联系电话"),

    companyPartnerInfoSet3roleType("股东4关系人类型"),
    companyPartnerInfoSet3partnerType("股东4高管/股东"),
    companyPartnerInfoSet3idcardType("股东4证件类型"),
    companyPartnerInfoSet3name("股东4姓名"),
    companyPartnerInfoSet3idcardNo("股东4证件号码"),
    companyPartnerInfoSet3partnerTelephone("股东4联系电话"),

    companyPartnerInfoSet4roleType("股东5关系人类型"),
    companyPartnerInfoSet4partnerType("股东5高管/股东"),
    companyPartnerInfoSet4idcardType("股东5证件类型"),
    companyPartnerInfoSet4name("股东5姓名"),
    companyPartnerInfoSet4idcardNo("股东5证件号码"),
    companyPartnerInfoSet4partnerTelephone("股东5联系电话"),

    companyPartnerInfoSet5roleType("股东6关系人类型"),
    companyPartnerInfoSet5partnerType("股东6高管/股东"),
    companyPartnerInfoSet5idcardType("股东6证件类型"),
    companyPartnerInfoSet5name("股东6姓名"),
    companyPartnerInfoSet5idcardNo("股东6证件号码"),
    companyPartnerInfoSet5partnerTelephone("股东6联系电话"),

    companyPartnerInfoSet6roleType("股东7关系人类型"),
    companyPartnerInfoSet6partnerType("股东7高管/股东"),
    companyPartnerInfoSet6idcardType("股东7证件类型"),
    companyPartnerInfoSet6name("股东7姓名"),
    companyPartnerInfoSet6idcardNo("股东7证件号码"),
    companyPartnerInfoSet6partnerTelephone("股东7联系电话"),

    companyPartnerInfoSet7roleType("股东8关系人类型"),
    companyPartnerInfoSet7partnerType("股东8高管/股东"),
    companyPartnerInfoSet7idcardType("股东8证件类型"),
    companyPartnerInfoSet7name("股东8姓名"),
    companyPartnerInfoSet7idcardNo("股东8证件号码"),
    companyPartnerInfoSet7partnerTelephone("股东8联系电话"),

    companyPartnerInfoSet8roleType("股东9关系人类型"),
    companyPartnerInfoSet8partnerType("股东9高管/股东"),
    companyPartnerInfoSet8idcardType("股东9证件类型"),
    companyPartnerInfoSet8name("股东9姓名"),
    companyPartnerInfoSet8idcardNo("股东9证件号码"),
    companyPartnerInfoSet8partnerTelephone("股东9联系电话"),

    companyPartnerInfoSet9roleType("股东10关系人类型"),
    companyPartnerInfoSet9partnerType("股东10高管/股东"),
    companyPartnerInfoSet9idcardType("股东10证件类型"),
    companyPartnerInfoSet9name("股东10姓名"),
    companyPartnerInfoSet9idcardNo("股东10证件号码"),
    companyPartnerInfoSet9partnerTelephone("股东10联系电话"),

    relateCompanyName("关联企业名称"),
    companyCertificateType("关联企业证件类型"),
    companyOrgEccsNo("关联企业社会信用代码"),
    companyLegalName("关联企业法人"),
    companyCertificateNo("关联企业证件号码"),

    refCustomerBillId("关联客户单据编号"),
    organCode("内部机构号"),
    openAccountSiteType("本异地"),
    imageBatchNo("影像批次号"),
    oldAccountKey("原基本存款账户编号"),
    nationality("国家或地区"),
    openKey("开户许可证号(新)");

    EnameToCnameEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static EnameToCnameEnum str2enum(String sync) {
        if (StringUtils.isBlank(sync)) {
            return null;
        }
        if (sync.equals("operatorIdcardNo")) {
            return EnameToCnameEnum.operatorIdcardNo;
        } else if (sync.equals("operatorIdcardDue")) {
            return EnameToCnameEnum.operatorIdcardDue;
        } else if (sync.equals("operatorTelephone")) {
            return EnameToCnameEnum.operatorTelephone;
        } else if (sync.equals("remark")) {
            return EnameToCnameEnum.remark;
        } else if (sync.equals("refBillId")) {
            return EnameToCnameEnum.refBillId;
        } else if (sync.equals("string001")) {
            return EnameToCnameEnum.string001;
        } else if (sync.equals("string002")) {
            return EnameToCnameEnum.string002;
        } else if (sync.equals("string003")) {
            return EnameToCnameEnum.string003;
        } else if (sync.equals("string004")) {
            return EnameToCnameEnum.string004;
        } else if (sync.equals("string005")) {
            return EnameToCnameEnum.string005;
        } else if (sync.equals("string006")) {
            return EnameToCnameEnum.string006;
        } else if (sync.equals("string007")) {
            return EnameToCnameEnum.string007;
        } else if (sync.equals("string008")) {
            return EnameToCnameEnum.string008;
        } else if (sync.equals("string009")) {
            return EnameToCnameEnum.string009;
        } else if (sync.equals("string010")) {
            return EnameToCnameEnum.string010;
        } else if (sync.equals("preLogId")) {
            return EnameToCnameEnum.preLogId;
        } else if (sync.equals("accountId")) {
            return EnameToCnameEnum.accountId;
        } else if (sync.equals("acctNo")) {
            return EnameToCnameEnum.acctNo;
        } else if (sync.equals("acctName")) {
            return EnameToCnameEnum.acctName;
        } else if (sync.equals("acctShortName")) {
            return EnameToCnameEnum.acctShortName;
        } else if (sync.equals("accountLicenseNo")) {
            return EnameToCnameEnum.accountLicenseNo;
        } else if (sync.equals("accountClass")) {
            return EnameToCnameEnum.accountClass;
        } else if (sync.equals("accountStatus")) {
            return EnameToCnameEnum.accountStatus;
        } else if (sync.equals("currencyType")) {
            return EnameToCnameEnum.currencyType;
        } else if (sync.equals("acctCreateDate")) {
            return EnameToCnameEnum.acctCreateDate;
        } else if (sync.equals("acctActiveDate")) {
            return EnameToCnameEnum.acctActiveDate;
        } else if (sync.equals("effectiveDate")) {
            return EnameToCnameEnum.effectiveDate;
        } else if (sync.equals("acctCreateReason")) {
            return EnameToCnameEnum.acctCreateReason;
        } else if (sync.equals("cancelDate")) {
            return EnameToCnameEnum.cancelDate;
        } else if (sync.equals("acctCancelReason")) {
            return EnameToCnameEnum.acctCancelReason;
        } else if (sync.equals("acctSuspenDate")) {
            return EnameToCnameEnum.acctSuspenDate;
        } else if (sync.equals("bankCode")) {
            return EnameToCnameEnum.bankCode;
        } else if (sync.equals("bankName")) {
            return EnameToCnameEnum.bankName;
        } else if (sync.equals("customerId")) {
            return EnameToCnameEnum.customerId;
        } else if (sync.equals("customerNo")) {
            return EnameToCnameEnum.customerNo;
        } else if (sync.equals("organFullId")) {
            return EnameToCnameEnum.organFullId;
        } else if (sync.equals("acctBigType")) {
            return EnameToCnameEnum.acctBigType;
        } else if (sync.equals("acctType")) {
            return EnameToCnameEnum.acctType;
        } else if (sync.equals("depositorType")) {
            return EnameToCnameEnum.depositorType;
        } else if (sync.equals("acctFileType")) {
            return EnameToCnameEnum.acctFileType;
        } else if (sync.equals("acctFileNo")) {
            return EnameToCnameEnum.acctFileNo;
        } else if (sync.equals("acctFileType2")) {
            return EnameToCnameEnum.acctFileType2;
        } else if (sync.equals("acctFileNo2")) {
            return EnameToCnameEnum.acctFileNo2;
        } else if (sync.equals("accountNameFrom")) {
            return EnameToCnameEnum.accountNameFrom;
        } else if (sync.equals("saccprefix")) {
            return EnameToCnameEnum.saccprefix;
        } else if (sync.equals("saccpostfix")) {
            return EnameToCnameEnum.saccpostfix;
        } else if (sync.equals("capitalProperty")) {
            return EnameToCnameEnum.capitalProperty;
        } else if (sync.equals("enchashmentType")) {
            return EnameToCnameEnum.enchashmentType;
        } else if (sync.equals("fundManager")) {
            return EnameToCnameEnum.fundManager;
        } else if (sync.equals("fundManagerIdcardType")) {
            return EnameToCnameEnum.fundManagerIdcardType;
        } else if (sync.equals("fundManagerIdcardNo")) {
            return EnameToCnameEnum.fundManagerIdcardNo;
        } else if (sync.equals("fundManagerIdcardDue")) {
            return EnameToCnameEnum.fundManagerIdcardDue;
        } else if (sync.equals("fundManagerTelephone")) {
            return EnameToCnameEnum.fundManagerTelephone;
        } else if (sync.equals("insideDeptName")) {
            return EnameToCnameEnum.insideDeptName;
        } else if (sync.equals("insideLeadName")) {
            return EnameToCnameEnum.insideLeadName;
        } else if (sync.equals("insideLeadIdcardType")) {
            return EnameToCnameEnum.insideLeadIdcardType;
        } else if (sync.equals("insideLeadIdcardNo")) {
            return EnameToCnameEnum.insideLeadIdcardNo;
        } else if (sync.equals("insideLeadIdcardDue")) {
            return EnameToCnameEnum.insideLeadIdcardDue;
        } else if (sync.equals("insideTelephone")) {
            return EnameToCnameEnum.insideTelephone;
        } else if (sync.equals("insideZipcode")) {
            return EnameToCnameEnum.insideZipcode;
        } else if (sync.equals("insideAddress")) {
            return EnameToCnameEnum.insideAddress;
        } else if (sync.equals("nontmpProjectName")) {
            return EnameToCnameEnum.nontmpProjectName;
        } else if (sync.equals("nontmpLegalName")) {
            return EnameToCnameEnum.nontmpLegalName;
        } else if (sync.equals("nontmpTelephone")) {
            return EnameToCnameEnum.nontmpTelephone;
        } else if (sync.equals("nontmpZipcode")) {
            return EnameToCnameEnum.nontmpZipcode;
        } else if (sync.equals("nontmpAddress")) {
            return EnameToCnameEnum.nontmpAddress;
        } else if (sync.equals("nontmpLegalIdcardType")) {
            return EnameToCnameEnum.nontmpLegalIdcardType;
        } else if (sync.equals("nontmpLegalIdcardNo")) {
            return EnameToCnameEnum.nontmpLegalIdcardNo;
        } else if (sync.equals("nontmpLegalIdcardDue")) {
            return EnameToCnameEnum.nontmpLegalIdcardDue;
        } else if (sync.equals("operatorName")) {
            return EnameToCnameEnum.operatorName;
        } else if (sync.equals("operatorIdcardType")) {
            return EnameToCnameEnum.operatorIdcardType;
        } else if (sync.equals("sequence")) {
            return EnameToCnameEnum.sequence;
        } else if (sync.equals("string006")) {
            return EnameToCnameEnum.string006;
        } else if (sync.equals("string007")) {
            return EnameToCnameEnum.string007;
        } else if (sync.equals("string008")) {
            return EnameToCnameEnum.string008;
        } else if (sync.equals("string009")) {
            return EnameToCnameEnum.string009;
        } else if (sync.equals("string010")) {
            return EnameToCnameEnum.string010;
        } else if (sync.equals("status")) {
            return EnameToCnameEnum.status;
        } else if (sync.equals("description")) {
            return EnameToCnameEnum.description;
        } else if (sync.equals("customerId")) {
            return EnameToCnameEnum.customerId;
        } else if (sync.equals("customerNo")) {
            return EnameToCnameEnum.customerNo;
        } else if (sync.equals("depositorName")) {
            return EnameToCnameEnum.depositorName;
        } else if (sync.equals("customerClass")) {
            return EnameToCnameEnum.customerClass;
        } else if (sync.equals("credentialType")) {
            return EnameToCnameEnum.credentialType;
        } else if (sync.equals("credentialNo")) {
            return EnameToCnameEnum.credentialNo;
        } else if (sync.equals("credentialDue")) {
            return EnameToCnameEnum.credentialDue;
        } else if (sync.equals("organFullId")) {
            return EnameToCnameEnum.organFullId;
        } else if (sync.equals("sealName")) {
            return EnameToCnameEnum.sealName;
        } else if (sync.equals("orgEnName")) {
            return EnameToCnameEnum.orgEnName;
        } else if (sync.equals("regCountry")) {
            return EnameToCnameEnum.regCountry;
        } else if (sync.equals("regProvince")) {
            return EnameToCnameEnum.regProvince;
        } else if (sync.equals("regProvinceChname")) {
            return EnameToCnameEnum.regProvinceChname;
        } else if (sync.equals("regCity")) {
            return EnameToCnameEnum.regCity;
        } else if (sync.equals("regCityChname")) {
            return EnameToCnameEnum.regCityChname;
        } else if (sync.equals("regArea")) {
            return EnameToCnameEnum.regArea;
        } else if (sync.equals("regAreaChname")) {
            return EnameToCnameEnum.regAreaChname;
        } else if (sync.equals("regAreaCode")) {
            return EnameToCnameEnum.regAreaCode;
        } else if (sync.equals("regAddress")) {
            return EnameToCnameEnum.regAddress;
        } else if (sync.equals("regFullAddress")) {
            return EnameToCnameEnum.regFullAddress;
        } else if (sync.equals("industryCode")) {
            return EnameToCnameEnum.industryCode;
        } else if (sync.equals("regOffice")) {
            return EnameToCnameEnum.regOffice;
        } else if (sync.equals("regType")) {
            return EnameToCnameEnum.regType;
        } else if (sync.equals("regNo")) {
            return EnameToCnameEnum.regNo;
        } else if (sync.equals("fileNo")) {
            return EnameToCnameEnum.fileNo;
        } else if (sync.equals("fileType")) {
            return EnameToCnameEnum.fileType;
        } else if (sync.equals("fileSetupDate")) {
            return EnameToCnameEnum.fileSetupDate;
        } else if (sync.equals("fileDue")) {
            return EnameToCnameEnum.fileDue;
        } else if (sync.equals("fileNo2")) {
            return EnameToCnameEnum.fileNo2;
        } else if (sync.equals("fileType2")) {
            return EnameToCnameEnum.fileType2;
        } else if (sync.equals("fileSetupDate2")) {
            return EnameToCnameEnum.fileSetupDate2;
        } else if (sync.equals("fileDue2")) {
            return EnameToCnameEnum.fileDue2;
        } else if (sync.equals("setupDate")) {
            return EnameToCnameEnum.setupDate;
        } else if (sync.equals("businessLicenseNo")) {
            return EnameToCnameEnum.businessLicenseNo;
        } else if (sync.equals("businessLicenseDue")) {
            return EnameToCnameEnum.businessLicenseDue;
        } else if (sync.equals("isIdentification")) {
            return EnameToCnameEnum.isIdentification;
        } else if (sync.equals("regCurrencyType")) {
            return EnameToCnameEnum.regCurrencyType;
        } else if (sync.equals("registeredCapital")) {
            return EnameToCnameEnum.registeredCapital;
        } else if (sync.equals("businessScope")) {
            return EnameToCnameEnum.businessScope;
        } else if (sync.equals("businessScopeEccs")) {
            return EnameToCnameEnum.businessScopeEccs;
        } else if (sync.equals("legalType")) {
            return EnameToCnameEnum.legalType;
        } else if (sync.equals("legalName")) {
            return EnameToCnameEnum.legalName;
        } else if (sync.equals("legalIdcardType")) {
            return EnameToCnameEnum.legalIdcardType;
        } else if (sync.equals("legalIdcardNo")) {
            return EnameToCnameEnum.legalIdcardNo;
        } else if (sync.equals("legalIdcardDue")) {
            return EnameToCnameEnum.legalIdcardDue;
        } else if (sync.equals("legalTelephone")) {
            return EnameToCnameEnum.legalTelephone;
        } else if (sync.equals("orgCode")) {
            return EnameToCnameEnum.orgCode;
        } else if (sync.equals("orgCodeDue")) {
            return EnameToCnameEnum.orgCodeDue;
        } else if (sync.equals("orgEccsNo")) {
            return EnameToCnameEnum.orgEccsNo;
        } else if (sync.equals("orgStatus")) {
            return EnameToCnameEnum.orgStatus;
        } else if (sync.equals("orgType")) {
            return EnameToCnameEnum.orgType;
        } else if (sync.equals("orgTypeDetail")) {
            return EnameToCnameEnum.orgTypeDetail;
        } else if (sync.equals("interbankNo")) {
            return EnameToCnameEnum.interbankNo;
        } else if (sync.equals("noTaxProve")) {
            return EnameToCnameEnum.noTaxProve;
        } else if (sync.equals("stateTaxRegNo")) {
            return EnameToCnameEnum.stateTaxRegNo;
        } else if (sync.equals("stateTaxDue")) {
            return EnameToCnameEnum.stateTaxDue;
        } else if (sync.equals("taxRegNo")) {
            return EnameToCnameEnum.taxRegNo;
        } else if (sync.equals("taxDue")) {
            return EnameToCnameEnum.taxDue;
        } else if (sync.equals("workCountry")) {
            return EnameToCnameEnum.workCountry;
        } else if (sync.equals("workProvince")) {
            return EnameToCnameEnum.workProvince;
        } else if (sync.equals("workProvinceChname")) {
            return EnameToCnameEnum.workProvinceChname;
        } else if (sync.equals("workCity")) {
            return EnameToCnameEnum.workCity;
        } else if (sync.equals("workCityChname")) {
            return EnameToCnameEnum.workCityChname;
        } else if (sync.equals("workArea")) {
            return EnameToCnameEnum.workArea;
        } else if (sync.equals("workAreaChname")) {
            return EnameToCnameEnum.workAreaChname;
        } else if (sync.equals("workAddress")) {
            return EnameToCnameEnum.workAddress;
        } else if (sync.equals("workFullAddress")) {
            return EnameToCnameEnum.workFullAddress;
        } else if (sync.equals("isSameRegistArea")) {
            return EnameToCnameEnum.isSameRegistArea;
        } else if (sync.equals("telephone")) {
            return EnameToCnameEnum.telephone;
        } else if (sync.equals("zipcode")) {
            return EnameToCnameEnum.zipcode;
        } else if (sync.equals("financeName")) {
            return EnameToCnameEnum.financeName;
        } else if (sync.equals("financeTelephone")) {
            return EnameToCnameEnum.financeTelephone;
        } else if (sync.equals("financeIdcardNo")) {
            return EnameToCnameEnum.financeIdcardNo;
        } else if (sync.equals("economyType")) {
            return EnameToCnameEnum.economyType;
        } else if (sync.equals("economyIndustryCode")) {
            return EnameToCnameEnum.economyIndustryCode;
        } else if (sync.equals("economyIndustryName")) {
            return EnameToCnameEnum.economyIndustryName;
        } else if (sync.equals("accountKey")) {
            return EnameToCnameEnum.accountKey;
        } else if (sync.equals("basicAccountStatus")) {
            return EnameToCnameEnum.basicAccountStatus;
        } else if (sync.equals("basicAcctRegArea")) {
            return EnameToCnameEnum.basicAcctRegArea;
        } else if (sync.equals("basicBankCode")) {
            return EnameToCnameEnum.basicBankCode;
        } else if (sync.equals("basicBankName")) {
            return EnameToCnameEnum.basicBankName;
        } else if (sync.equals("bankCardNo")) {
            return EnameToCnameEnum.bankCardNo;
        } else if (sync.equals("corpScale")) {
            return EnameToCnameEnum.corpScale;
        } else if (sync.equals("parAccountKey")) {
            return EnameToCnameEnum.parAccountKey;
        } else if (sync.equals("parCorpName")) {
            return EnameToCnameEnum.parCorpName;
        } else if (sync.equals("parLegalIdcardNo")) {
            return EnameToCnameEnum.parLegalIdcardNo;
        } else if (sync.equals("parLegalIdcardType")) {
            return EnameToCnameEnum.parLegalIdcardType;
        } else if (sync.equals("parLegalIdcardDue")) {
            return EnameToCnameEnum.parLegalIdcardDue;
        } else if (sync.equals("parLegalName")) {
            return EnameToCnameEnum.parLegalName;
        } else if (sync.equals("parLegalType")) {
            return EnameToCnameEnum.parLegalType;
        } else if (sync.equals("parLegalTelephone")) {
            return EnameToCnameEnum.parLegalTelephone;
        } else if (sync.equals("parOrgCode")) {
            return EnameToCnameEnum.parOrgCode;
        } else if (sync.equals("parOrgCodeDue")) {
            return EnameToCnameEnum.parOrgCodeDue;
        } else if (sync.equals("parOrgEccsNo")) {
            return EnameToCnameEnum.parOrgEccsNo;
        } else if (sync.equals("parOrgEccsDue")) {
            return EnameToCnameEnum.parOrgEccsDue;
        } else if (sync.equals("parRegType")) {
            return EnameToCnameEnum.parRegType;
        } else if (sync.equals("parRegNo")) {
            return EnameToCnameEnum.parRegNo;
        } else if (sync.equals("refBillId")) {
            return EnameToCnameEnum.refBillId;
        } else if (sync.equals("refCustomerBillId")) {
            return EnameToCnameEnum.refCustomerBillId;
        } else if (sync.equals("string001")) {
            return EnameToCnameEnum.string001;
        } else if (sync.equals("string002")) {
            return EnameToCnameEnum.string002;
        } else if (sync.equals("string003")) {
            return EnameToCnameEnum.string003;
        } else if (sync.equals("string004")) {
            return EnameToCnameEnum.string004;
        } else if (sync.equals("string005")) {
            return EnameToCnameEnum.string005;
        } else if (sync.equals("preLogId")) {
            return EnameToCnameEnum.preLogId;
        } else if (sync.equals("sequence")) {
            return EnameToCnameEnum.sequence;
        }
        else if (sync.equals("companyPartnerInfoSet9roleType")) {
            return EnameToCnameEnum.companyPartnerInfoSet9roleType;
        }
        else if (sync.equals("companyPartnerInfoSet9partnerType")) {
            return EnameToCnameEnum.companyPartnerInfoSet9partnerType;
        }
        else if (sync.equals("companyPartnerInfoSet9idcardType")) {
            return EnameToCnameEnum.companyPartnerInfoSet9idcardType;
        }
        else if (sync.equals("companyPartnerInfoSet9name")) {
            return EnameToCnameEnum.companyPartnerInfoSet9name;
        }
        else if (sync.equals("companyPartnerInfoSet9idcardNo")) {
            return EnameToCnameEnum.companyPartnerInfoSet9idcardNo;
        }
        else if (sync.equals("companyPartnerInfoSet9partnerTelephone")) {
            return EnameToCnameEnum.companyPartnerInfoSet9partnerTelephone;
        }

        else if (sync.equals("companyPartnerInfoSet8roleType")) {
            return EnameToCnameEnum.companyPartnerInfoSet8roleType;
        }
        else if (sync.equals("companyPartnerInfoSet8partnerType")) {
            return EnameToCnameEnum.companyPartnerInfoSet8partnerType;
        }
        else if (sync.equals("companyPartnerInfoSet8idcardType")) {
            return EnameToCnameEnum.companyPartnerInfoSet8idcardType;
        }
        else if (sync.equals("companyPartnerInfoSet8name")) {
            return EnameToCnameEnum.companyPartnerInfoSet8name;
        }
        else if (sync.equals("companyPartnerInfoSet8idcardNo")) {
            return EnameToCnameEnum.companyPartnerInfoSet8idcardNo;
        }
        else if (sync.equals("companyPartnerInfoSet8partnerTelephone")) {
            return EnameToCnameEnum.companyPartnerInfoSet8partnerTelephone;
        }

        else if (sync.equals("companyPartnerInfoSet7roleType")) {
            return EnameToCnameEnum.companyPartnerInfoSet7roleType;
        }
        else if (sync.equals("companyPartnerInfoSet7partnerType")) {
            return EnameToCnameEnum.companyPartnerInfoSet7partnerType;
        }
        else if (sync.equals("companyPartnerInfoSet7idcardType")) {
            return EnameToCnameEnum.companyPartnerInfoSet7idcardType;
        }
        else if (sync.equals("companyPartnerInfoSet7name")) {
            return EnameToCnameEnum.companyPartnerInfoSet7name;
        }
        else if (sync.equals("companyPartnerInfoSet7idcardNo")) {
            return EnameToCnameEnum.companyPartnerInfoSet7idcardNo;
        }
        else if (sync.equals("companyPartnerInfoSet7partnerTelephone")) {
            return EnameToCnameEnum.companyPartnerInfoSet7partnerTelephone;
        }

        else if (sync.equals("companyPartnerInfoSet6roleType")) {
            return EnameToCnameEnum.companyPartnerInfoSet6roleType;
        }
        else if (sync.equals("companyPartnerInfoSet6partnerType")) {
            return EnameToCnameEnum.companyPartnerInfoSet6partnerType;
        }
        else if (sync.equals("companyPartnerInfoSet6idcardType")) {
            return EnameToCnameEnum.companyPartnerInfoSet6idcardType;
        }
        else if (sync.equals("companyPartnerInfoSet6name")) {
            return EnameToCnameEnum.companyPartnerInfoSet6name;
        }
        else if (sync.equals("companyPartnerInfoSet6idcardNo")) {
            return EnameToCnameEnum.companyPartnerInfoSet6idcardNo;
        }
        else if (sync.equals("companyPartnerInfoSet6partnerTelephone")) {
            return EnameToCnameEnum.companyPartnerInfoSet6partnerTelephone;
        }

        else if (sync.equals("companyPartnerInfoSet5roleType")) {
            return EnameToCnameEnum.companyPartnerInfoSet5roleType;
        }
        else if (sync.equals("companyPartnerInfoSet5partnerType")) {
            return EnameToCnameEnum.companyPartnerInfoSet5partnerType;
        }
        else if (sync.equals("companyPartnerInfoSet5idcardType")) {
            return EnameToCnameEnum.companyPartnerInfoSet5idcardType;
        }
        else if (sync.equals("companyPartnerInfoSet5name")) {
            return EnameToCnameEnum.companyPartnerInfoSet5name;
        }
        else if (sync.equals("companyPartnerInfoSet5idcardNo")) {
            return EnameToCnameEnum.companyPartnerInfoSet5idcardNo;
        }
        else if (sync.equals("companyPartnerInfoSet5partnerTelephone")) {
            return EnameToCnameEnum.companyPartnerInfoSet5partnerTelephone;
        }

        else if (sync.equals("companyPartnerInfoSet4roleType")) {
            return EnameToCnameEnum.companyPartnerInfoSet4roleType;
        }
        else if (sync.equals("companyPartnerInfoSet4partnerType")) {
            return EnameToCnameEnum.companyPartnerInfoSet4partnerType;
        }
        else if (sync.equals("companyPartnerInfoSet4idcardType")) {
            return EnameToCnameEnum.companyPartnerInfoSet4idcardType;
        }
        else if (sync.equals("companyPartnerInfoSet4name")) {
            return EnameToCnameEnum.companyPartnerInfoSet4name;
        }
        else if (sync.equals("companyPartnerInfoSet4idcardNo")) {
            return EnameToCnameEnum.companyPartnerInfoSet4idcardNo;
        }
        else if (sync.equals("companyPartnerInfoSet4partnerTelephone")) {
            return EnameToCnameEnum.companyPartnerInfoSet4partnerTelephone;
        }

        else if (sync.equals("companyPartnerInfoSet3roleType")) {
            return EnameToCnameEnum.companyPartnerInfoSet3roleType;
        }
        else if (sync.equals("companyPartnerInfoSet3partnerType")) {
            return EnameToCnameEnum.companyPartnerInfoSet3partnerType;
        }
        else if (sync.equals("companyPartnerInfoSet3idcardType")) {
            return EnameToCnameEnum.companyPartnerInfoSet3idcardType;
        }
        else if (sync.equals("companyPartnerInfoSet3name")) {
            return EnameToCnameEnum.companyPartnerInfoSet3name;
        }
        else if (sync.equals("companyPartnerInfoSet3idcardNo")) {
            return EnameToCnameEnum.companyPartnerInfoSet3idcardNo;
        }
        else if (sync.equals("companyPartnerInfoSet3partnerTelephone")) {
            return EnameToCnameEnum.companyPartnerInfoSet3partnerTelephone;
        }

        else if (sync.equals("companyPartnerInfoSet2roleType")) {
            return EnameToCnameEnum.companyPartnerInfoSet2roleType;
        }
        else if (sync.equals("companyPartnerInfoSet2partnerType")) {
            return EnameToCnameEnum.companyPartnerInfoSet2partnerType;
        }
        else if (sync.equals("companyPartnerInfoSet2idcardType")) {
            return EnameToCnameEnum.companyPartnerInfoSet2idcardType;
        }
        else if (sync.equals("companyPartnerInfoSet2name")) {
            return EnameToCnameEnum.companyPartnerInfoSet2name;
        }
        else if (sync.equals("companyPartnerInfoSet2idcardNo")) {
            return EnameToCnameEnum.companyPartnerInfoSet2idcardNo;
        }
        else if (sync.equals("companyPartnerInfoSet2partnerTelephone")) {
            return EnameToCnameEnum.companyPartnerInfoSet2partnerTelephone;
        }

        else if (sync.equals("companyPartnerInfoSet1roleType")) {
            return EnameToCnameEnum.companyPartnerInfoSet1roleType;
        }
        else if (sync.equals("companyPartnerInfoSet1partnerType")) {
            return EnameToCnameEnum.companyPartnerInfoSet1partnerType;
        }
        else if (sync.equals("companyPartnerInfoSet1idcardType")) {
            return EnameToCnameEnum.companyPartnerInfoSet1idcardType;
        }
        else if (sync.equals("companyPartnerInfoSet1name")) {
            return EnameToCnameEnum.companyPartnerInfoSet1name;
        }
        else if (sync.equals("companyPartnerInfoSet1idcardNo")) {
            return EnameToCnameEnum.companyPartnerInfoSet1idcardNo;
        }
        else if (sync.equals("companyPartnerInfoSet1partnerTelephone")) {
            return EnameToCnameEnum.companyPartnerInfoSet1partnerTelephone;
        }

        else if (sync.equals("companyPartnerInfoSet0roleType")) {
            return EnameToCnameEnum.companyPartnerInfoSet0roleType;
        }
        else if (sync.equals("companyPartnerInfoSet0partnerType")) {
            return EnameToCnameEnum.companyPartnerInfoSet0partnerType;
        }
        else if (sync.equals("companyPartnerInfoSet0idcardType")) {
            return EnameToCnameEnum.companyPartnerInfoSet0idcardType;
        }
        else if (sync.equals("companyPartnerInfoSet0name")) {
            return EnameToCnameEnum.companyPartnerInfoSet0name;
        }
        else if (sync.equals("companyPartnerInfoSet0idcardNo")) {
            return EnameToCnameEnum.companyPartnerInfoSet0idcardNo;
        }
        else if (sync.equals("companyPartnerInfoSet0partnerTelephone")) {
            return EnameToCnameEnum.companyPartnerInfoSet0partnerTelephone;
        }

        else if (sync.contains("relateCompanyName")) {
            return EnameToCnameEnum.relateCompanyName;
        }
        else if (sync.equals("companyCertificateType")) {
            return EnameToCnameEnum.companyCertificateType;
        }
        else if (sync.equals("companyOrgEccsNo")) {
            return EnameToCnameEnum.companyOrgEccsNo;
        }
        else if (sync.equals("companyLegalName")) {
            return EnameToCnameEnum.companyLegalName;
        }
        else if (sync.equals("companyCertificateNo")) {
            return EnameToCnameEnum.companyCertificateNo;
        }
        else if (sync.equals("organCode")) {
            return EnameToCnameEnum.organCode;
        }
        else if(sync.equals("openAccountSiteType")){
            return  EnameToCnameEnum.openAccountSiteType;
        }
        else if(sync.equals("imageBatchNo")){
            return  EnameToCnameEnum.imageBatchNo;
        }
        else if(sync.equals("oldAccountKey")){
            return  EnameToCnameEnum.oldAccountKey;
        }
        else if(sync.equals("openKey")){
            return  EnameToCnameEnum.openKey;
        }else if(sync.equals("nationality")){
            return  EnameToCnameEnum.nationality;
        }
        return null;
    }
}

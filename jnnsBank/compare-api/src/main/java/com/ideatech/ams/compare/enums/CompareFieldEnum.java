package com.ideatech.ams.compare.enums;

import lombok.Getter;

@Getter
public enum CompareFieldEnum {

    ACCT_NO("账号", "acctNo"),

    DEPOSITOR_NAME("存款人名称", "depositorName"),

    ORGAN_CODE("机构代码", "organCode"),

    LEGAL_NAME("法人姓名", "legalName"),

    REG_NO("工商注册编号", "regNo"),

    BUSINESS_SCOPE("经营范围", "businessScope"),

    REG_ADDRESS("注册地址", "regAddress"),

    REG_CAPITAL("注册资金", "registeredCapital"),

    ACCOUNT_STATUS("账户状态", "accountStatus"),

    STATE_TAXREG_NO("国税登记证号", "stateTaxRegNo"),

    TAXREG_NO("地税登记证号", "taxRegNo"),

    BANK_NAME("开户银行名称", "bankName"),

    ACCT_TYPE("账户性质", "acctType"),

    ACCT_CREATE_DATE("开户日期", "acctCreateDate"),

    ACCOUNT_KEY("基本户开户许可核准号", "accountKey"),

    ACCOUNT_LICENSE_NO("账户开户许可核准号", "accountLicenseNo"),

    FILE_TYPE("证明文件种类", "fileType"),

    FILE_NO("证明文件编号", "fileNo"),

    TELEPHONE("联系电话", "telephone"),

    ZIP_CODE("邮政编码", "zipcode"),

    DEPOSITOR_TYPE("存款人类别", "depositorType"),

    LEGAL_IDCARD_TYPE("证件种类", "legalIdcardType"),

    LEGAL_IDCARD_NO("法人证件号码", "legalIdcardNo"),

    INDUSTRY_CODE("行业归属", "industryCode"),

    /**
     * 来自信用代码系统
     */
    ECONOMY_INDUSTRY("产业分类", "economyIndustry"),

    REG_AREA_CODE("注册地地区代码", "regAreaCode"),

    REG_OFFICE("登记部门", "regOffice"),

    CANCEL_DATE("销户日期", "cancelDate"),

    ORG_CODE("组织机构代码", "orgCode"),

    OPENDATE("成立日期","opendate"),

    ENDDATE("营业执照到期日","enddate");

    private String value;

    private String field;

    CompareFieldEnum(String value, String field) {
        this.value = value;
        this.field = field;
    }

}

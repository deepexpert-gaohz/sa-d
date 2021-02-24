package com.ideatech.ams.apply.entity;


import com.ideatech.ams.apply.enums.CustomerType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;


/**
 * 预约客户更新中间表
 *
 * @author RJQ
 */

@Table(name = "apply_customer_public_mid")
@Data
@Entity
public class ApplyCustomerPublicMid extends BaseMaintainablePo {

    /**
     * 账户id
     */
    @Column(name = "open_account_log_id")
    private Long openAccoutLogId;

    /**
    * 序列化ID,缓存需要
    */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "CUSTOMER_PUBLIC_MID";

    /**
    * 扩展字段6
    */
    
    @Column(length = 100)
    private String string006;
    /**
    * 扩展字段7
    */
    
    @Column(length = 100)
    private String string007;
    /**
    * 扩展字段8
    */
    
    @Column(length = 100)
    private String string008;
    /**
    * 扩展字段9
    */
    
    @Column(length = 100)
    private String string009;
    /**
    * 扩展字段10
    */
    
    @Column(length = 100)
    private String string010;
    /**
    * 更新状态(New新增记录/Error更新异常/Hold暂挂)
    */
    
    @Column(length = 10)
    private String status;
    /**
    * 更新描述
    */
    
    @Column(length = 100)
    private String description;
    /**
    * 客户ID
    */
    
    @Column(name = "customer_id", length = 22)
    private Long customerId;
    /**
    * 客户号
    */
    
    @Column(name = "customer_no", length = 50)
    private String customerNo;
    /**
    * 存款人名称
    */
    
    @Column(name = "depositor_name", length = 200)
    private String depositorName;

    /**
     * 存款人类别
     */
    
    @Column(name = "depositor_type", length = 30)
    private String depositorType;

    /**
    * 客户类型(1个人2对公3金融)
    */
    
    @Column(name = "customer_class", length = 10)
    @Enumerated(EnumType.STRING)
    private CustomerType customerClass;
    /**
    * 证件类型
    */
    
    @Column(name = "credential_type", length = 10)
    private String credentialType;
    /**
    * 证件号码
    */
    
    @Column(name = "credential_no", length = 50)
    private String credentialNo;
    /**
    * 证件到期日
    */
    
    @Column(name = "credential_due", length = 10)
    private String credentialDue;
    /**
    * 机构fullId
    */
    
    @Column(name = "organ_full_id", length = 100)
    private String organFullId;
    /**
    * 客户公章名称
    */
    
    @Column(name = "seal_name", length = 200)
    private String sealName;
    /**
    * 客户类别(01企业法人、02非法人企业、03机关、04预算事业单位、05非预算事业单位、06团（含）以上军队、07团（含）以上武警部队、08社
    * 会团体、09宗教组织、10民办非企业组织、11异地常设机构、12外国驻华机构、13有字号个体工商户、14无字号个体工商户、15居委会/村委会/社
    * 区委、16单位独立核算附属机构、17其他组织、18QFII、19临时机构、20境外机构、21港澳台机构、99其他、30金融同业)
    */
    
    @Column(name = "customer_category", length = 100)
    private String customerCategory;
    /**
    * 机构英文名称
    */
    
    @Column(name = "org_en_name", length = 100)
    private String orgEnName;
    /**
    * 注册国家代码
    */
    
    @Column(name = "reg_country", length = 30)
    private String regCountry;
    /**
    * 注册省份
    */
    
    @Column(name = "reg_province", length = 30)
    private String regProvince;
    /**
    * 注册地区省份中文名
    */
    
    @Column(name = "reg_province_chname", length = 100)
    private String regProvinceChname;
    /**
    * 注册城市
    */
    
    @Column(name = "reg_city", length = 30)
    private String regCity;
    /**
    * 注册城市中文名
    */
    
    @Column(name = "reg_city_chname", length = 100)
    private String regCityChname;
    /**
    * 注册地区
    */
    
    @Column(name = "reg_area", length = 30)
    private String regArea;
    /**
    * 注册地区中文名
    */
    
    @Column(name = "reg_area_chname", length = 100)
    private String regAreaChname;
    /**
    * 基本户注册地地区代码
    */
    
    @Column(name = "reg_area_code", length = 50)
    private String regAreaCode;

    /**
    * 注册详细地址
    */
    
    @Column(name = "reg_address", length = 255)
    private String regAddress;
    /**
    * 完整注册地址(与营业执照一致)
    */
    
    @Column(name = "reg_full_address", length = 500)
    private String regFullAddress;
    /**
    * 行业归属
    */
    
    @Column(name = "industry_code", length = 150)
    private String industryCode;
    /**
    * 登记部门
    */
    
    @Column(name = "reg_office", length = 30)
    private String regOffice;
    /**
    * 工商注册类型
    */
    
    @Column(name = "reg_type", length = 30)
    private String regType;
    /**
    * 工商注册编号
    */
    
    @Column(name = "reg_no", length = 100)
    private String regNo;
    /**
    * 证明文件1编号(工商注册号)
    */
    
    @Column(name = "file_no", length = 100)
    private String fileNo;
    /**
    * 证明文件1种类(工商注册类型)
    */
    
    @Column(name = "file_type", length = 50)
    private String fileType;
    /**
    * 证明文件1设立日期
    */
    
    @Column(name = "file_setup_date", length = 10)
    private String fileSetupDate;
    /**
    * 证明文件1到期日
    */
    
    @Column(name = "file_due", length = 10)
    private String fileDue;
    /**
    * 证明文件2编号
    */
    
    @Column(name = "file_no2", length = 100)
    private String fileNo2;
    /**
    * 证明文件2种类
    */
    
    @Column(name = "file_type2", length = 50)
    private String fileType2;
    /**
    * 证明文件2设立日期
    */
    
    @Column(name = "file_setup_date2", length = 10)
    private String fileSetupDate2;
    /**
    * 证明文件2到期日
    */
    
    @Column(name = "file_due2", length = 10)
    private String fileDue2;
    /**
    * 成立日期
    */
    
    @Column(name = "setup_date", length = 10)
    private String setupDate;
    /**
    * 营业执照号码
    */
    
    @Column(name = "business_license_no", length = 50)
    private String businessLicenseNo;
    /**
    * 营业执照到期日
    */
    
    @Column(name = "business_license_due", length = 10)
    private String businessLicenseDue;
    /**
    * 未标明注册资金
    */
    
    @Column(name = "is_identification", length = 100)
    private String isIdentification;
    /**
    * 注册资本币种
    */
    
    @Column(name = "reg_currency_type", length = 10)
    private String regCurrencyType;
    /**
    * 注册资金（元）
    */
    
    @Column(name = "registered_capital", length = 22)
    private BigDecimal registeredCapital;
    /**
    * 经营（业务）范围
    */
    
    @Column(name = "business_scope", length = 2000)
    private String businessScope;
    /**
    * 经营（业务）范围(信用代码证)
    */
    
    @Column(name = "business_scope_eccs", length = 2000)
    private String businessScopeEccs;
    /**
    * 法人类型（法定代表人、单位负责人）
    */
    
    @Column(name = "legal_type", length = 50)
    private String legalType;
    /**
    * 法人姓名
    */
    
    @Column(name = "legal_name", length = 20)
    private String legalName;
    /**
    * 法人证件类型
    */
    
    @Column(name = "legal_idcard_type", length = 20)
    private String legalIdcardType;
    /**
    * 法人证件编号
    */
    
    @Column(name = "legal_idcard_no", length = 50)
    private String legalIdcardNo;
    /**
    * 法人证件到期日
    */
    
    @Column(name = "legal_idcard_due", length = 10)
    private String legalIdcardDue;
    /**
    * 法人联系电话
    */
    
    @Column(name = "legal_telephone", length = 50)
    private String legalTelephone;
    /**
    * 组织机构代码
    */
    
    @Column(name = "org_code", length = 100)
    private String orgCode;
    /**
    * 组织机构代码证件到期日
    */
    
    @Column(name = "org_code_due", length = 10)
    private String orgCodeDue;
    /**
    * 机构信用代码
    */
    
    @Column(name = "org_eccs_no", length = 100)
    private String orgEccsNo;
    /**
    * 机构状态
    */
    
    @Column(name = "org_status", length = 30)
    private String orgStatus;
    /**
    * 组织机构类别
    */
    
    @Column(name = "org_type", length = 30)
    private String orgType;
    /**
    * 组织机构类别细分
    */
    
    @Column(name = "org_type_detail", length = 30)
    private String orgTypeDetail;
    /**
    * 同业金融机构编码
    */
    
    @Column(name = "interbank_no", length = 50)
    private String interbankNo;
    /**
    * 无需办理税务登记证的文件或税务机关出具的证明
    */
    
    @Column(name = "no_tax_prove", length = 100)
    private String noTaxProve;
    /**
    * 纳税人识别号（国税）
    */
    
    @Column(name = "state_tax_reg_no", length = 100)
    private String stateTaxRegNo;
    /**
    * 国税证件到期日
    */
    
    @Column(name = "state_tax_due", length = 10)
    private String stateTaxDue;
    /**
    * 纳税人识别号（地税）
    */
    
    @Column(name = "tax_reg_no", length = 100)
    private String taxRegNo;
    /**
    * 地税证件到期日
    */
    
    @Column(name = "tax_due", length = 10)
    private String taxDue;
    /**
    * 办公国家代码
    */
    
    @Column(name = "work_country", length = 30)
    private String workCountry;
    /**
    * 办公省份
    */
    
    @Column(name = "work_province", length = 30)
    private String workProvince;
    /**
    * 办公地区省份中文名
    */
    
    @Column(name = "work_province_chname", length = 100)
    private String workProvinceChname;
    /**
    * 办公城市
    */
    
    @Column(name = "work_city", length = 30)
    private String workCity;
    /**
    * 办公城市中文名
    */
    
    @Column(name = "work_city_chname", length = 100)
    private String workCityChname;
    /**
    * 办公地区
    */
    
    @Column(name = "work_area", length = 30)
    private String workArea;
    /**
    * 办公地区中文名
    */
    
    @Column(name = "work_area_chname", length = 100)
    private String workAreaChname;
    /**
    * 办公详细地址
    */
    
    @Column(name = "work_address", length = 255)
    private String workAddress;
    /**
    * 完整办公地址(与营业执照一致)
    */
    
    @Column(name = "work_full_address", length = 500)
    private String workFullAddress;
    /**
    * 是否与与注册地址一致
    */
    
    @Column(name = "is_same_regist_area", length = 30)
    private String isSameRegistArea;

    /**
    * 联系电话
    */
    
    @Column(length = 50)
    private String telephone;
    /**
    * 邮政编码
    */
    
    @Column(length = 6)
    private String zipcode;
    /**
    * 财务主管姓名
    */
    
    @Column(name = "finance_name", length = 30)
    private String financeName;
    /**
    * 财务部联系电话
    */
    
    @Column(name = "finance_telephone", length = 50)
    private String financeTelephone;
    /**
    * 财务主管身份证号
    */
    
    @Column(name = "finance_idcard_no", length = 50)
    private String financeIdcardNo;
    /**
    * 经济类型
    */
    
    @Column(name = "economy_type", length = 30)
    private String economyType;
    /**
    * 经济行业分类Code
    */
    
    @Column(name = "economy_industry_code", length = 30)
    private String economyIndustryCode;
    /**
    * 经济行业分类
    */
    
    @Column(name = "economy_industry_name", length = 100)
    private String economyIndustryName;
    /**
    * 基本开户许可核准号
    */
    
    @Column(name = "account_key", length = 50)
    private String accountKey;
    /**
    * 基本户状态
    */
    
    @Column(name = "basic_account_status", length = 30)
    private String basicAccountStatus;
    /**
    * 基本户注册地地区代码
    */
    
    @Column(name = "basic_acct_reg_area", length = 100)
    private String basicAcctRegArea;
    /**
    * 基本户开户银行金融机构编码
    */
    
    @Column(name = "basic_bank_code", length = 14)
    private String basicBankCode;
    /**
    * 基本户开户银行名称
    */
    
    @Column(name = "basic_bank_name", length = 100)
    private String basicBankName;
    /**
    * 贷款卡编码
    */
    
    @Column(name = "bank_card_no", length = 100)
    private String bankCardNo;
    /**
    * 企业规模
    */
    
    @Column(name = "corp_scale", length = 30)
    private String corpScale;
    /**
    * 上级基本户开户许可核准号
    */
    
    @Column(name = "par_account_key", length = 50)
    private String parAccountKey;
    /**
    * 上级机构名称
    */
    
    @Column(name = "par_corp_name", length = 100)
    private String parCorpName;
    /**
    * 上级法人证件号码
    */
    
    @Column(name = "par_legal_idcard_no", length = 50)
    private String parLegalIdcardNo;
    /**
    * 上级法人证件类型
    */
    
    @Column(name = "par_legal_idcard_type", length = 30)
    private String parLegalIdcardType;
    /**
    * 上级法人证件到期日
    */
    
    @Column(name = "par_legal_idcard_due", length = 10)
    private String parLegalIdcardDue;
    /**
    * 上级法人姓名
    */
    
    @Column(name = "par_legal_name", length = 50)
    private String parLegalName;
    /**
    * 上级法人类型
    */
    
    @Column(name = "par_legal_type", length = 30)
    private String parLegalType;
    /**
    * 上级法人联系电话
    */
    
    @Column(name = "par_legal_telephone", length = 50)
    private String parLegalTelephone;
    /**
    * 上级组织机构代码
    */
    
    @Column(name = "par_org_code", length = 100)
    private String parOrgCode;
    /**
    * 上级组织机构代码证到期日
    */
    
    @Column(name = "par_org_code_due", length = 10)
    private String parOrgCodeDue;
    /**
    * 上级机构信用代码
    */
    
    @Column(name = "par_org_eccs_no", length = 100)
    private String parOrgEccsNo;
    /**
    * 上级机构信用代码证到期日
    */
    
    @Column(name = "par_org_eccs_due", length = 10)
    private String parOrgEccsDue;
    /**
    * 上级登记注册号类型
    */
    
    @Column(name = "par_reg_type", length = 30)
    private String parRegType;
    /**
    * 上级登记注册号码
    */
    
    @Column(name = "par_reg_no", length = 100)
    private String parRegNo;
    /**
    * 关联单据ID
    */
    
    @Column(name = "ref_bill_id", length = 22)
    private Long refBillId;
    /**
    * 扩展字段1
    */
    
    @Column(length = 100)
    private String string001;
    /**
    * 扩展字段2
    */
    
    @Column(length = 100)
    private String string002;
    /**
    * 扩展字段3
    */
    
    @Column(length = 100)
    private String string003;
    /**
    * 扩展字段4
    */
    
    @Column(length = 100)
    private String string004;
    /**
    * 扩展字段5
    */
    
    @Column(length = 100)
    private String string005;

    /**
    * 关联企业
    */
    //  @Getter
    //  @Setter
    //  @OneToMany(mappedBy = "customerPublicMid")
    //  private Set<RelateCompanyMid> relateCompanysMid;

    /**
    * 企业股东/高管信息
    */
    //  @Getter
    //  @Setter
    //  @OneToMany(mappedBy = "customerPublicMid")
    //  private Set<CompanyPartnerMid> companyPartnersMid;

    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
    return "";
    }

    /**************** 新增补录字段 ******************************/

    /**
     * 注册信息中的邮政编码
     */
    @Column(length = 30)
    private String regZipcode;
}

package com.ideatech.ams.domain;

import com.ideatech.ams.account.enums.AccountClass;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@Entity
public class CustCoreInfo extends BaseMaintainablePo implements Serializable {
    private static final long serialVersionUID = 4214870702711170454L;
    /**
     *客户号
     */
    private String customerNo;
    /**
     * 注册地区
     */
    private String regArea;
    /**
     * 注册地区中文名
     */
    private String regAreaChname;
    /**
     * 注册地地区代码
     */
    private String regAreaCode;
    /**
     * 注册详细地址
     */
    private String regAddress;
    /**
     * 完整注册地址(与营业执照一致)
     */
    private String regFullAddress;
    /**
     * 行业归属
     */
    private String industryCode;
    /**
     * 工商注册类型
     */
    private String regType;
    /**
     * 工商注册编号
     */
    private String regNo;
    /**
     * 证明文件1编号(工商注册号)
     */
    private String fileNo;
    /**
     * 证明文件1种类(工商注册类型)
     */
    private String fileType;
    /**
     * 证明文件1到期日
     */
    private String fileDue;
    /**
     * 证明文件2编号
     */
    private String fileNo2;
    /**
     * 证明文件2种类(工商注册类型)
     */
    private String fileType2;
    /**
     * 证明文件2到期日
     */
    private String fileDue2;
    /**
     * 成立日期
     */
    private String setupDate;
    /**
     * 营业执照号码
     */
    private String businessLicenseNo;
    /**
     * 营业执照到期日
     */
    private String businessLicenseDue;
    /**
     * 未标明注册资金
     */
    private String isIdentification;
    /**
     * 注册资本币种
     */
    private String regCurrencyType;
    /**
     * 注册资金（元）
     */
    private String registeredCapital;
    /**
     * 经营（业务）范围
     */
    private String businessScope;
    /**
     * 经营（业务）范围(信用代码证)
     */
    private String businessScopeEccs;
    /**
     * 企业规模
     */
    private String corpScale;
    /**
     * 法人姓名
     */
    private String legalName;
    /**
     * 证件种类
     */
    private String legalIdcardType;
    /**
     * 法人证件编号
     */
    private String legalIdcardNo;
    /**
     * 组织机构代码
     */
    private String orgCode;
    /**
     * 组织机构代码证件到期日
     */
    private String orgCodeDue;
    /**
     * 机构信用代码
     */
    private String orgEccsNo;
    /**
     * 组织机构类别
     */
    private String orgType;
    /**
     * 同业金融机构编码
     */
    private String interbankNo;
    /**
     * 纳税人识别号（国税）
     */
    private String stateTaxRegNo;
    /**
     * 纳税人识别号（地税）
     */
    private String taxRegNo;
    /**
     * 办公详细地址
     */
    private String workAddress;
    /**
     * 企业联系电话
     */
    private String telephone;
    /**
     * 邮政编码
     */
    private String zipcode;
    /**
     * 经济类型
     */
    private String economyType;
    /**
     * 经济行业分类Code
     */
    private String economyIndustryCode;
    /**
     * 经济行业分类
     */
    private String economyIndustryName;
    /**
     * 基本开户许可核准号
     */
    private String accountKey;
    /**
     * 基本户状态
     */
    private String basicAccountStatus;
    /**
     * 基本户开户银行金融机构编码
     */
    private String basicBankCode;
    /**
     * 基本户开户银行名称
     */
    private String basicBankName;
    /**
     * 贷款卡编码
     */
    private String bankCardNo;

    /**
     * 机构英文名称
     */
    private String orgEnName;
    /**
     * 注册国家代码
     */
    private String regCountry;
    /**
     * 注册省份
     */
    private String regProvince;
    /**
     * 注册地区省份中文名
     */
    private String regProvinceChname;
    /**
     * 注册城市
     */
    private String regCity;
    /**
     * 注册城市中文名
     */
    private String regCityChname;
    /**
     * 存款人名称
     */
    private String depositorName;
    /**
     * 客户类型(1个人2对公3金融)
     */
    private String customerClass;
    /**
     * 证件类型
     */
    private String credentialType;
    /**
     * 证件号码
     */
    private String credentialNo;
    /**
     * 证件到期日
     */
    private String credentialDue;
    /**
     * 流水的完整机构ID
     */
    private String organFullId;

    /**
     * 账户id
     */
    private String accountId;
    /**
     * 账户性质大类(1基本户、2一般户、3专用户、4临时户)
     */
    private String acctBigType;
    /**
     * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7
     * 非预算单位专用存款账户)
     */
   @Enumerated(EnumType.STRING)
    private CompanyAcctType acctType;
    /**
     * 开户证明文件种类1
     */
    private String acctFileType;
    /**
     * 开户证明文件编号1
     */
    private String acctFileNo;
    /**
     * 账号
     */
    private String acctNo;
    /**
     * 账户名称
     */
    private String acctName;
    /**
     * 账户简称
     */
    private String acctShortName;
    /**
     * 账户分类(1个人2对公3金融)
     */
    @Enumerated(EnumType.STRING)
    private AccountClass accountClass;
    /**
     * 账户状态
     */
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    /**
     * 币种
     */
    private String currencyType;
    /**
     * 账户开户日期
     */
    private String acctCreateDate;
    /**
     * 账户激活日期
     */
    private String acctActiveDate;
    /**
     * 账户有效期(临时账户)
     */
    private String effectiveDate;
    /**
     * 账户销户日期
     */
    private String cancelDate;
    /**
     * 账户销户日期
     */
    private String acctSuspenDate;
    /**
     * 开户银行金融机构编码
     */
    private String bankCode;
    /**
     * 开户银行名称
     */
    private String bankName;

    /**
     * 人行账号
     */
    private String jhhAcctNo;



}

package com.ideatech.ams.account.dto;

import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CorePublicAccountDto{
    protected Long id;

    private Long batchId;

	/**
	 * 核心机构的代码
	 */
	private String organCode;

    private String errorReason;

    /**
     * 单据类型（01开户申请、02变更申请、03销户申请、04久悬申请）
     */
    
    private String billType;

	/**
	 * 流水来源(预填单、核心T+0、核心T+1、AMS)
	 */
	private String fromSource;
	
	/**
	 * 影像批次号
	 */
	private String imageBatchNo;

	/**
	 * 客户
	 */
	private String customerId;
	
	/**
	 * 注册地区
	 */
	private String regArea;

	/**
	 * 注册地区中文名
	 */
	private String regAreaChname;

	/**
	 * 注册地区代码
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
	 * 登记部门
	 */
	private String regOffice;
	
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
	 * 证明文件1设立日期
	 */
	private String fileSetupDate;
	
	/**
	 * 证明文件1到期日
	 */
	private String fileDue;
	
	/**
	 * 证明文件2编号
	 */
	private String fileNo2;
	
	/**
	 * 证明文件2种类
	 */
	private String fileType2;
	
	/**
	 * 证明文件2设立日期
	 */
	private String fileSetupDate2;
	
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
	private BigDecimal registeredCapital;
	
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
	 * 法人类型（法定代表人、单位负责人）
	 */
	private String legalType;
	
	/**
	 * 法人姓名
	 */
	private String legalName;
	
	/**
	 * 法人证件类型
	 */
	private String legalIdcardType;

	/**
	 * 法人证件编号
	 */
	private String legalIdcardNo;
	
	/**
	 * 法人证件到期日
	 */
	private String legalIdcardDue;
	
	/**
	 * 法人联系电话
	 */
	private String legalTelephone;
	
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
	 * 机构状态
	 */
	private String orgStatus;
	
	/**
	 * 组织机构类别
	 */
	private String orgType;
	
	/**
	 * 组织机构类别细分
	 */
	private String orgTypeDetail;
	
    /**
     * 同业金融机构编码
     */
    private String interbankNo;
    
    /**
     * 无需办理税务登记证的文件或税务机关出具的证明
     */
    private String noTaxProve;

	/**
	 * 纳税人识别号（国税）
	 */
	private String stateTaxRegNo;

	/**
	 * 国税证件到期日
	 */
	private String stateTaxDue;

	/**
	 * 纳税人识别号（地税）
	 */
	private String taxRegNo;
	
	/**
	 * 地税证件到期日
	 */
	private String taxDue;

    /**
     * 办公国家代码
     */
    private String workCountry;
    
    /**
     * 办公省份
     */
    private String workProvince;
    
	/**
	 * 办公地区省份中文名
	 */
	private String workProvinceChname;
	
    /**
     * 办公城市
     */
    private String workCity;
    
	/**
	 * 办公城市中文名
	 */
	private String workCityChname;
	
    /**
     * 办公地区
     */
    private String workArea;
    
	/**
	 * 办公地区中文名
	 */
	private String workAreaChname;
	
	/**
	 * 办公详细地址
	 */
	private String workAddress;
	

	/**
	 * 完整办公地址(单位联系地址)
	 */
	private String workFullAddress;


    /**
     * 是否与与注册地址一致
     */
    private String isSameRegistArea;

	/**
	 * 联系电话
	 */
	private String telephone;
	
	/**
	 * 邮政编码
	 */
	private String zipcode;

    /**
     * 财务主管姓名
     */
    private String financeName;
    

    /**
     * 财务部联系电话
     */
    private String financeTelephone;

    /**
     * 财务主管身份证号
     */
    private String financeIdcardNo;
    

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
     * 基本户注册地地区代码
     */
    private String basicAcctRegArea;
    
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
	 * 上级基本户开户许可核准号
	 */
	private String parAccountKey;

	/**
	 * 上级机构名称
	 */
	private String parCorpName;

	/**
	 * 上级法人证件号码
	 */
	private String parLegalIdcardNo;
	
	/**
	 * 上级法人证件类型
	 */
	private String parLegalIdcardType;
	
	/**
	 * 上级法人证件到期日
	 */
	private String parLegalIdcardDue;

	/**
	 * 上级法人姓名
	 */
	private String parLegalName;

	/**
	 * 上级法人类型
	 */
	private String parLegalType;
	
    /**
     * 上级法人联系电话
     */
    private String parLegalTelephone;

	/**
	 * 上级组织机构代码
	 */
	private String parOrgCode;

	/**
	 * 上级组织机构代码证到期日
	 */
	private String parOrgCodeDue;

	/**
	 * 上级机构信用代码
	 */
	private String parOrgEccsNo;

    /**
     * 上级机构信用代码证到期日
     */
    private String parOrgEccsDue;
    
	/**
	 * 上级登记注册号类型
	 */
	private String parRegType;

	/**
	 * 上级登记注册号码
	 */
	private String parRegNo;

    /**
     * 客户公章名称
     */
    private String sealName;
    
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
	 * 客户号
	 */
	private String customerNo;

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
     * 机构fullId
     */
    private String organFullId;
    
    /**
     * 关联单据ID
     */
    private Long refBillId;

    /**
     * 最新的日志表ID
     */
    private Long refCustomerLogId;

    /**
     * 账户id
     */
    private String accountId;

	/**
	 * 账户性质大类(1基本户、2一般户、3专用户、4临时户)
	 */
	private String acctBigType;

    /**
     * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7 非预算单位专用存款账户)
     */
    private String acctType;

	/**
	 * 存款人类别
	 */
	private String depositorType;

	/**
	 * 开户证明文件种类1
	 */
	private String acctFileType;

	/**
	 * 开户证明文件编号1
	 */
	private String acctFileNo;
	
	/**
	 * 开户证明文件种类2
	 */
	private String acctFileType2;

	/**
	 * 开户证明文件编号2
	 */
	private String acctFileNo2;

	/**
	 * 账户名称构成方式
	 */
	private String accountNameFrom;
	
	/**
	 * 账户前缀
	 */
	private String saccprefix;

	/**
	 * 账户后缀
	 */
	private String saccpostfix;

	/**
	 * 资金性质
	 */
	private String capitalProperty;
	
	/**
	 * 取现标识
	 */
	private String enchashmentType;

	/**
	 * 资金管理人姓名
	 */
	private String fundManager;

	/**
	 * 资金管理人身份证种类
	 */
	private String fundManagerIdcardType;

	/**
	 * 资金管理人身份证编号
	 */
	private String fundManagerIdcardNo;
	
	/**
	 * 资金管理人证件到期日
	 */
	private String fundManagerIdcardDue;
	
	/**
	 * 资金管理人联系电话
	 */
	private String fundManagerTelephone;
	
	/**
	 * 内设部门名称
	 */
	private String insideDeptName;

	/**
	 * 内设部门负责人名称
	 */
	private String insideLeadName;

	/**
	 * 内设部门负责人身份种类
	 */
	private String insideLeadIdcardType;

	/**
	 * 内设部门负责人身份编号
	 */
	private String insideLeadIdcardNo;

	/**
	 * 负责人证件到期日
	 */
	private String insideLeadIdcardDue;

	/**
	 * 内设部门联系电话
	 */
	private String insideTelephone;
	
    /**
     * 内设部门地址
     */
    private String insideAddress;

	/**
	 * 内设部门邮编
	 */
	private String insideZipcode;
	
	/**
	 * 非临时项目部名称
	 */
	private String nontmpProjectName;
	
	/**
	 * 非临时负责人姓名
	 */
	private String nontmpLegalName;
	
	/**
	 * 非临时联系电话
	 */
	private String nontmpTelephone;
	
	/**
	 * 非临时邮政编码
	 */
	private String nontmpZipcode;

	/**
	 * 非临时地址
	 */
	private String nontmpAddress;
	
	/**
	 * 非临时身份证件编号
	 */
	private String nontmpLegalIdcardNo;

    /**
     * 非临时身份证件种类
     */
    private String nontmpLegalIdcardType;

	/**
	 * 非临时身份证件到期日
	 */
	private String nontmpLegalIdcardDue;
	
    /**
     * 业务经办人姓名
     */
    private String operatorName;

    /**
     * 业务经办人证件类型
     */
    private String operatorIdcardType;
	
    /**
     * 业务经办人证件号码
     */
    private String operatorIdcardNo;
    
    /**
     * 业务经办人证件有效日期
     */
    private String operatorIdcardDue;

    /**
     * 业务经办人联系电话
     */
    private String operatorTelephone;

	/**
	 * 账户备注信息
	 */
	private String remark;

    /**
     * 客户id
     */
    private String customerLogId;

	/**
	 * 账号
	 */
	private String acctNo;
	
    /**
     * 账户名称
     */
    private String acctName;
    
    /**
     * 账户简名
     */
    private String acctShortName;
    
    /**
     * 账户分类(1个人2对公3金融)
     */
    private String accountClass;
	/**
	 * 账户状态
	 */
	private String accountStatus;

	/**
	 * 账户币种
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
	 * 开户原因
	 */
	private String acctCreateReason;

    /**
     * 账户销户日期
     */

    /**
     * 销户原因
     */
    private String acctCancelReason;

    /**
     * 久悬日期
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
	 * 扩展字段1
	 */
	private String string001;
	
	/**
	 * 扩展字段2
	 */
	private String string002;
	
	/**
	 * 扩展字段3
	 */
	private String string003;
	
	/**
	 * 扩展字段4
	 */
	private String string004;
	
	/**
	 * 扩展字段5
	 */
	private String string005;
	
	/**
	 * 扩展字段6
	 */
	private String string006;
	
	/**
	 * 扩展字段7
	 */
	private String string007;
	
	/**
	 * 扩展字段8
	 */
	
	/**
	 * 扩展字段9
	 */
	private String string009;
	
	/**
	 * 扩展字段10
	 */
	private String string010;

	/**
	 * 数据处理日期
	 */
	private String dataDate;

	/**
	 * 不匹配的字段
	 */
	private String changeFieldsStr;

	/**
	 * 状态
	 */
	private CompanyIfType handleStatus;

}

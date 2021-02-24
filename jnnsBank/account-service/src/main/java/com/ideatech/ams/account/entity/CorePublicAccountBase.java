package com.ideatech.ams.account.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@MappedSuperclass
public class CorePublicAccountBase extends BaseMaintainablePo{

	/**
	 * 文件处理批次ID
	 */
	private Long batchId;

	/**
	 * 操作类型
	 */
	private String billType;

	/**
	 * 核心机构的代码
	 */
	private String organCode;

	/**
	 * 流水来源(预填单、核心T+0、核心T+1、AMS)
     *
	 */
	private String fromSource;
	
	/**
	 * 影像批次号
	 */
	@Column
	@Lob
	private String imageBatchNo;

	/**
	 * 客户
	 */
	@Column
	private String customerId;
	
	/**
	 * 注册地区
	 */
	@Column(length = 30)
	private String regArea;

	/**
	 * 注册地区中文名
	 */
	@Column(length = 100)
	private String regAreaChname;

	/**
	 * 注册地区代码
	 */
	@Column(length = 50)
	private String regAreaCode;

	/**
	 * 注册详细地址
	 */
	@Column(length = 255)
	private String regAddress;
	
	/**
	 * 完整注册地址(与营业执照一致)
	 */
	@Column(length = 500)
	private String regFullAddress;
	
	/**
	 * 行业归属
	 */
	@Column(length = 150)
	private String industryCode;
	
	/**
	 * 登记部门
	 */
	@Column(length = 30)
	private String regOffice;
	
	/**
	 * 工商注册类型
	 */
	@Column(length = 30)
	private String regType;

	/**
	 * 工商注册编号
	 */
	@Column(length = 100)
	private String regNo;

	/**
	 * 证明文件1编号(工商注册号)
	 */
	@Column(length = 100)
	private String fileNo;
	
	/**
	 * 证明文件1种类(工商注册类型)
	 */
	@Column(length = 50)
	private String fileType;
	
	/**
	 * 证明文件1设立日期
	 */
	@Column(length = 10)
	private String fileSetupDate;
	
	/**
	 * 证明文件1到期日
	 */
	@Column(length = 10)
	private String fileDue;
	
	/**
	 * 证明文件2编号
	 */
	@Column(length = 100)
	private String fileNo2;
	
	/**
	 * 证明文件2种类
	 */
	@Column(length = 50)
	private String fileType2;
	
	/**
	 * 证明文件2设立日期
	 */
	@Column(length = 10)
	private String fileSetupDate2;
	
	/**
	 * 证明文件2到期日
	 */
	@Column(length = 10)
	private String fileDue2;
	
	/**
	 * 成立日期
	 */
	@Column(length = 10)
	private String setupDate;
	
    /**
     * 营业执照号码
     */
    @Column(length = 50)
    private String businessLicenseNo;
    
    /**
     * 营业执照到期日
     */
    @Column(length = 10)
    private String businessLicenseDue;
    
	/**
	 * 未标明注册资金
	 */
	@Column(length = 100)
	private String isIdentification;

	/**
	 * 注册资本币种
	 */
	@Column(length = 10)
	private String regCurrencyType;
	
	/**
	 * 注册资金（元）
	 */
	@Column(length = 22)
	private BigDecimal registeredCapital;
	
	/**
	 * 经营（业务）范围
	 */
	@Column
	@Lob
	private String businessScope;

    /**
     * 经营（业务）范围(信用代码证)
     */
    @Column(length = 2000)
    private String businessScopeEccs;

	/**
	 * 企业规模
	 */
	@Column(length = 30)
	private String corpScale;
	
	/**
	 * 法人类型（法定代表人、单位负责人）
	 */
	@Column(length = 50)
	private String legalType;
	
	/**
	 * 法人姓名
	 */
	@Column(length = 50)
	private String legalName;
	
	/**
	 * 法人证件类型
	 */
	@Column(length = 20)
	private String legalIdcardType;

	/**
	 * 法人证件编号
	 */
	@Column(length = 50)
	private String legalIdcardNo;
	
	/**
	 * 法人证件到期日
	 */
	@Column(length = 20)
	private String legalIdcardDue;
	
	/**
	 * 法人联系电话
	 */
	@Column(length = 50)
	private String legalTelephone;
	
	/**
	 * 组织机构代码
	 */
	@Column(length = 100)
	private String orgCode;
	
	/**
	 * 组织机构代码证件到期日
	 */
	@Column(length = 10)
	private String orgCodeDue;
	
	/**
	 * 机构信用代码
	 */
	@Column(length = 100)
	private String orgEccsNo;
	
	/**
	 * 机构状态
	 */
	@Column(length = 30)
	private String orgStatus;
	
	/**
	 * 组织机构类别
	 */
	@Column(length = 30)
	private String orgType;
	
	/**
	 * 组织机构类别细分
	 */
	@Column(length = 30)
	private String orgTypeDetail;
	
    /**
     * 同业金融机构编码
     */
    @Column(length = 50)
    private String interbankNo;
    
    /**
     * 无需办理税务登记证的文件或税务机关出具的证明
     */
    @Column(length = 100)
    private String noTaxProve;

	/**
	 * 纳税人识别号（国税）
	 */
	@Column(length = 100)
	private String stateTaxRegNo;

	/**
	 * 国税证件到期日
	 */
	@Column(length = 10)
	private String stateTaxDue;

	/**
	 * 纳税人识别号（地税）
	 */
	@Column(length = 100)
	private String taxRegNo;
	
	/**
	 * 地税证件到期日
	 */
	@Column(length = 10)
	private String taxDue;

    /**
     * 办公国家代码
     */
    @Column(length = 30)
    private String workCountry;
    
    /**
     * 办公省份
     */
    @Column(length = 30)
    private String workProvince;
    
	/**
	 * 办公地区省份中文名
	 */
	@Column(length = 100)
	private String workProvinceChname;
	
    /**
     * 办公城市
     */
    @Column(length = 30)
    private String workCity;
    
	/**
	 * 办公城市中文名
	 */
	@Column(length = 100)
	private String workCityChname;
	
    /**
     * 办公地区
     */
    @Column(length = 30)
    private String workArea;
    
	/**
	 * 办公地区中文名
	 */
	@Column(length = 100)
	private String workAreaChname;
	
	/**
	 * 办公详细地址
	 */
	@Column(length = 255)
	private String workAddress;
	

	/**
	 * 完整办公地址(单位联系地址)
	 */
	@Column(length = 500)
	private String workFullAddress;


    /**
     * 是否与与注册地址一致
     */
    @Column(length = 30)
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
    @Column(length = 50)
    private String financeName;
    

    /**
     * 财务部联系电话
     */
    @Column(length = 50)
    private String financeTelephone;

    /**
     * 财务主管身份证号
     */
    @Column(length = 50)
    private String financeIdcardNo;
    

	/**
	 * 经济类型
	 */
	@Column(length = 30)
	private String economyType;
    
	/**
	 * 经济行业分类Code
	 */
	@Column(length = 30)
	private String economyIndustryCode;

	/**
	 * 经济行业分类
	 */
	@Column(length = 100)
	private String economyIndustryName;

    /**
     * 基本开户许可核准号
     */

    @Column(length = 50)
    private String accountKey;

	/**
	 * 基本户状态
	 */
	@Column(length = 30)
	private String basicAccountStatus;

    /**
     * 基本户注册地地区代码
     */
    @Column(length = 100)
    private String basicAcctRegArea;
    
	/**
	 * 基本户开户银行金融机构编码
	 */
	@Column(length = 14)
	private String basicBankCode;

    /**
     * 基本户开户银行名称
     */
    @Column(length = 100)
    private String basicBankName;

	/**
	 * 贷款卡编码
	 */
	@Column(length = 100)
	private String bankCardNo;
	
	/**
	 * 上级基本户开户许可核准号
	 */
	@Column(length = 50)
	private String parAccountKey;

	/**
	 * 上级机构名称
	 */
	@Column(length = 100)
	private String parCorpName;

	/**
	 * 上级法人证件号码
	 */
	@Column(length = 50)
	private String parLegalIdcardNo;
	
	/**
	 * 上级法人证件类型
	 */
	@Column(length = 30)
	private String parLegalIdcardType;
	
	/**
	 * 上级法人证件到期日
	 */
	@Column(length = 10)
	private String parLegalIdcardDue;

	/**
	 * 上级法人姓名
	 */
	@Column(length = 50)
	private String parLegalName;

	/**
	 * 上级法人类型
	 */
	@Column(length = 30)
	private String parLegalType;
	
    /**
     * 上级法人联系电话
     */
    @Column(length = 50)
    private String parLegalTelephone;

	/**
	 * 上级组织机构代码
	 */
	@Column(length = 100)
	private String parOrgCode;

	/**
	 * 上级组织机构代码证到期日
	 */
	@Column(length = 10)
	private String parOrgCodeDue;

	/**
	 * 上级机构信用代码
	 */
	@Column(length = 100)
	private String parOrgEccsNo;

    /**
     * 上级机构信用代码证到期日
     */
    @Column(length = 10)
    private String parOrgEccsDue;
    
	/**
	 * 上级登记注册号类型
	 */
	@Column(length = 30)
	private String parRegType;

	/**
	 * 上级登记注册号码
	 */
	@Column(length = 100)
	private String parRegNo;

    /**
     * 客户公章名称
     */
    @Column(length = 200)
    private String sealName;
    
	/**
	 * 机构英文名称
	 */
	@Column(length = 100)
	private String orgEnName;
	
    /**
     * 注册国家代码
     */
    @Column(length = 30)
    private String regCountry;

    /**
     * 注册省份
     */

    @Column(length = 30)
    private String regProvince;
    
	/**
	 * 注册地区省份中文名
	 */
	@Column(length = 100)
	private String regProvinceChname;
	
    /**
     * 注册城市
     */
    @Column(length = 30)
    private String regCity;

	/**
	 * 注册城市中文名
	 */
	@Column(length = 100)
	private String regCityChname;

	/**
	 * 客户号
	 */
	@Column(length = 50)
	private String customerNo;

	/**
	 * 存款人名称
	 */
	@Column(length = 200)
	private String depositorName;
	
    /**
     * 客户类型(1个人2对公3金融)
     */
    @Column(length = 10)
    private String customerClass;

	/**
	 * 证件类型
	 */
	@Column(length = 30)
	private String credentialType;

	/**
	 * 证件号码
	 */
	@Column(length = 50)
	private String credentialNo;
	
	/**
	 * 证件到期日
	 */
	@Column(length = 10)
	private String credentialDue;

    /**
     * 机构fullId
     */
    private String organFullId;
    
    /**
     * 关联单据ID
     */
    @Column(length = 22)
    private Long refBillId;

    /**
     * 最新的日志表ID
     */
    @Column(length = 22)
    private Long refCustomerLogId;

    /**
     * 账户id
     */
    @Column(length = 14)
    private String accountId;

	/**
	 * 账户性质大类(1基本户、2一般户、3专用户、4临时户)
	 */
	@Column(length = 50)
	private String acctBigType;

    /**
     * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7 非预算单位专用存款账户)
     */
    @Column(length = 50)
    private String acctType;

	/**
	 * 存款人类别
	 */
	@Column(length = 30)
	private String depositorType;

	/**
	 * 开户证明文件种类1
	 */
	@Column(length = 30)
	private String acctFileType;

	/**
	 * 开户证明文件编号1
	 */
	@Column(length = 100)
	private String acctFileNo;
	
	/**
	 * 开户证明文件种类2
	 */
	@Column(length = 30)
	private String acctFileType2;

	/**
	 * 开户证明文件编号2
	 */
	@Column(length = 100)
	private String acctFileNo2;

	/**
	 * 账户名称构成方式
	 */
	@Column(length = 50)
	private String accountNameFrom;
	
	/**
	 * 账户前缀
	 */
	@Column(length = 100)
	private String saccprefix;

	/**
	 * 账户后缀
	 */
	@Column(length = 100)
	private String saccpostfix;

	/**
	 * 资金性质
	 */
	@Column(length = 255)
	private String capitalProperty;
	
	/**
	 * 取现标识
	 */
	@Column(length = 30)
	private String enchashmentType;

	/**
	 * 资金管理人姓名
	 */
	@Column(length = 100)
	private String fundManager;

	/**
	 * 资金管理人身份证种类
	 */
	@Column(length = 30)
	private String fundManagerIdcardType;

	/**
	 * 资金管理人身份证编号
	 */
	@Column(length = 50)
	private String fundManagerIdcardNo;
	
	/**
	 * 资金管理人证件到期日
	 */
	@Column(length = 10)
	private String fundManagerIdcardDue;
	
	/**
	 * 资金管理人联系电话
	 */
	@Column(length = 50)
	private String fundManagerTelephone;
	
	/**
	 * 内设部门名称
	 */
	@Column(length = 100)
	private String insideDeptName;

	/**
	 * 内设部门负责人名称
	 */
	@Column(length = 50)
	private String insideLeadName;

	/**
	 * 内设部门负责人身份种类
	 */
	@Column(length = 50)
	private String insideLeadIdcardType;

	/**
	 * 内设部门负责人身份编号
	 */
	@Column(length = 50)
	private String insideLeadIdcardNo;

	/**
	 * 负责人证件到期日
	 */
	@Column(length = 10)
	private String insideLeadIdcardDue;

	/**
	 * 内设部门联系电话
	 */
	@Column(length = 50)
	private String insideTelephone;
	
    /**
     * 内设部门地址
     */
    @Column(length = 100)
    private String insideAddress;

	/**
	 * 内设部门邮编
	 */
	@Column(length = 6)
	private String insideZipcode;
	
	/**
	 * 非临时项目部名称
	 */
	@Column(length = 255)
	private String nontmpProjectName;
	
	/**
	 * 非临时负责人姓名
	 */
	@Column(length = 100)
	private String nontmpLegalName;
	
	/**
	 * 非临时联系电话
	 */
	@Column(length = 50)
	private String nontmpTelephone;
	
	/**
	 * 非临时邮政编码
	 */
	@Column(length = 6)
	private String nontmpZipcode;

	/**
	 * 非临时地址
	 */
	@Column(length = 500)
	private String nontmpAddress;
	
	/**
	 * 非临时身份证件编号
	 */
	@Column(length = 50)
	private String nontmpLegalIdcardNo;

    /**
     * 非临时身份证件种类
     */
    @Column(length = 30)
    private String nontmpLegalIdcardType;

	/**
	 * 非临时身份证件到期日
	 */
	@Column(length = 10)
	private String nontmpLegalIdcardDue;
	
    /**
     * 业务经办人姓名
     */
    @Column(length = 100)
    private String operatorName;

    /**
     * 业务经办人证件类型
     */
    @Column(length = 30)
    private String operatorIdcardType;
	
    /**
     * 业务经办人证件号码
     */
    @Column(length = 50)
    private String operatorIdcardNo;
    
    /**
     * 业务经办人证件有效日期
     */
    @Column(length = 10)
    private String operatorIdcardDue;

    /**
     * 业务经办人联系电话
     */
    @Column(length = 50)
    private String operatorTelephone;

	/**
	 * 账户备注信息
	 */
	@Column(length = 255)
	private String remark;

    /**
     * 客户id
     */
    @Column(length = 14)
    private String customerLogId;

	/**
	 * 账号
	 */
	@Column(length = 32)
	private String acctNo;
	
    /**
     * 账户名称
     */
    @Column(length = 255)
    private String acctName;
    
    /**
     * 账户简名
     */
    @Column(length = 255)
    private String acctShortName;
    
    /**
     * 账户分类(1个人2对公3金融)
     */
    @Column(length = 10)
    private String accountClass;
	/**
	 * 账户状态
	 */
	@Column(length = 10)
	private String accountStatus;

	/**
	 * 账户币种
	 */
	@Column(length = 10)
	private String currencyType;

	/**
	 * 账户开户日期
	 */
	@Column(length = 10)
	private String acctCreateDate;
	
    /**
     * 账户激活日期
     */
    @Column(length = 10)
    private String acctActiveDate;

	/**
	 * 账户有效期(临时账户)
	 */
	@Column(length = 10)
	private String effectiveDate;

	/**
	 * 开户原因
	 */
	@Column(length = 255)
	private String acctCreateReason;

    /**
     * 账户销户日期
     */
    @Column(length = 10)
    private String cancelDate;

    /**
     * 销户原因
     */
    @Column(length = 255)
    private String acctCancelReason;

    /**
     * 久悬日期
     */
    @Column(length = 10)
    private String acctSuspenDate;

	/**
	 * 开户银行金融机构编码
	 */
	@Column(length = 14)
	private String bankCode;

	/**
	 * 开户银行名称
	 */
	@Column(length = 100)
	private String bankName;
	
	/**
	 * 扩展字段1
	 */
	@Column(length = 255)
	private String string001;
	
	/**
	 * 扩展字段2
	 */
	@Column(length = 255)
	private String string002;
	
	/**
	 * 扩展字段3
	 */
	@Column(length = 255)
	private String string003;
	
	/**
	 * 扩展字段4
	 */
	@Column(length = 255)
	private String string004;
	
	/**
	 * 扩展字段5
	 */
	@Column(length = 255)
	private String string005;
	
	/**
	 * 扩展字段6
	 */
	@Column(length = 255)
	private String string006;
	
	/**
	 * 扩展字段7
	 */
	@Column(length = 255)
	private String string007;
	
	/**
	 * 扩展字段8
	 */
	@Column(length = 255)
	private String string008;
	
	/**
	 * 扩展字段9
	 */
	@Column(length = 255)
	private String string009;
	
	/**
	 * 扩展字段10
	 */
	@Column(length = 255)
	private String string010;

	/**
	 * 数据处理日期
	 */
	@Column(length = 10)
	private String dataDate;

	/**
	 * 不匹配的字段
	 */
	@Column
	@Lob
	private String changeFieldsStr;

	/**
	 *
	 */
	@Column(length = 50)
	private String accountLicenseNo;

	/**
	 * 账户许可核准号
	 */
	@Enumerated(EnumType.STRING)
	private CompanyIfType handleStatus;
	
	public void Validate() {
		// [主键]的必填验证
		if (this.getId() == null) {
			throw new RuntimeException("[主键]不能为空");
		}
		/*// [证件类型]的必填验证
		if (this.getCredentialType() == null || this.getCredentialType().equals("")) {
			throw new RuntimeException("[证件类型]不能为空");
		}*/
		// [证件号码]的必填验证
		if (this.getCredentialNo() == null || this.getCredentialNo().equals("")) {
			throw new RuntimeException("[证件号码]不能为空");
		}
		// [存款人名称]的必填验证
		if (this.getDepositorName() == null || this.getDepositorName().equals("")) {
			throw new RuntimeException("[存款人名称]不能为空");
		}
	}
	

}

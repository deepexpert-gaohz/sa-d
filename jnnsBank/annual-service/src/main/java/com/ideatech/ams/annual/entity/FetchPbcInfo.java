package com.ideatech.ams.annual.entity;

import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.common.entity.AmsEntityLength;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 人行账管系统账户全量表
 * @author zoulang
 * @date 2017年5月10日 上午7:39:42
 */
@Entity
@Table(name = "yd_fetch_pbc", indexes = {@Index(name = "fetch_pbc_atid_idx",columnList = "annualTaskId")})
@Data
public class FetchPbcInfo extends AmsEntityLength implements Serializable {

	private static final long serialVersionUID = -959862524478883119L;
	/**
	 * 采集日期 yyyyMMdd hh:mm:ss
	 */
	@Column(length = DEFAULT_DATE_COLUMN_LENGTH)
	private String parDate;

	/**
	 * 存款人名称
	 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String depositorName;
	/**
	 * 银行机构号（人行14位）
	 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String bankCode;

	/**
	 * 完整机构ID organFullId
	 */
	private String organFullId;
	/**
	 * 银行机构名称
	 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String bankName;
	/**
	 * 账户性质
	 */
	@Enumerated(EnumType.STRING)
	@Column(length = DEFAULT_ENUM_COLUMN_LENGTH)
	private AccountType acctType;
	/**
	 * 账号
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String acctNo;
	/**
	 * 账户名称
	 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String acctName;
	/**
	 * 账户开户日期
	 */
	@Column(length = DEFAULT_DATE_COLUMN_LENGTH)
	private String acctCreateDate;
	/**
	 * 账户销户日期
	 */
	@Column(length = DEFAULT_DATE_COLUMN_LENGTH)
	private String cancelDate;
	/**
	 * 存款人类别(基本户、特殊户)
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String depositorType;
	/**
	 * 注册地地区代码
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String regAreaCode;
	/**
	 * 组织机构代码
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String orgCode;
	/**
	 * 证明文件1种类(工商注册类型:非临时、基本户、专用户、一般户、临时户)
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String fileType;
	/**
	 * 证明文件1编号(工商注册号)
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String fileNo;
	/**
	 * 证明文件2种类(基本户、专用户、特殊户)
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String fileType2;
	/**
	 * 证明文件2编号
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String fileNo2;
	/**
	 * 法人类型（法定代表人、单位负责人）
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String legalType;
	/**
	 * 法人姓名
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String legalName;
	/**
	 * 法人证件类型
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String legalIdcardType;
	/**
	 * 法人证件编号
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String legalIdcardNo;
	/**
	 * 行业归属
	 */
	private String industryCode;
	/**
	 * 国税
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String stateTaxRegNo;
	/**
	 * 地税
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String taxRegNo;
	/**
	 * 无需办理税务登记证的文件或税务机关出具的证明
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String noTaxProve;
	/**
	 * 注册币种
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String regCurrencyType;
	/**
	 * 注册资金
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String registeredCapital;
	/**
	 * 经营（业务）范围（上报人行）
	 */
	@Column(length = DEFAULT_BUSINESSSCOPE_VARCHAR_COLUMN_LENGTH)
	private String businessScope;
	/**
	 * 经营（业务）范围（上报信用机构）
	 */
	@Column(length = DEFAULT_BUSINESSSCOPE_VARCHAR_COLUMN_LENGTH)
	private String businessScopeEccs;
	/**
	 * 注册（登记）地址
	 */
	@Column(length = DEFAULT_COLUMN_500_LENGTH)
	private String regAddress;
	/**
	 * 工商注册地址
	 */
	@Column(length = DEFAULT_COLUMN_500_LENGTH)
	private String indusRegArea;
	/**
	 * 邮政编码
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String zipCode;
	/**
	 * 联系电话
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String telephone;
	/**
	 * 上级单位存款人名称
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String parCorpName;
	/**
	 * 上级单位基本户开户许可证
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String parAccountKey;
	/**
	 * 上级组织机构代码
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String parOrgCode;
	/**
	 * 上级法人类型（法人代表、单位负责人）
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String parLegalType;
	/**
	 * 上级法人姓名
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String parLegalName;
	/**
	 * 上级法人证件类型
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String parLegalIdcardType;
	/**
	 * 上级法人证件编号
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String parLegalIdcardNo;
	/**
	 * 基本户开户许可证核准号
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String accountKey;
	/**
	 * 账户状态
	 */
	@Enumerated(EnumType.STRING)
	@Column(length = DEFAULT_ENUM_COLUMN_LENGTH)
	private AccountStatus accountStatus;
	/**
	 * 资金性质
	 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String capitalProperty;
	/**
	 * 取现标识
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String enchashmentType;
	/**
	 * 账户证明文件类型(一般户、预算专户、非预算专户、非临时)
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String accountFileType;
	/**
	 * 账户证明文件编号(一般户、预算专户、非预算专户、非临时)
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String accountFileNo;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 非临时_账户有效时间
	 */
	@Column(length = DEFAULT_DATE_COLUMN_LENGTH)
	private String effectiveDate;
	/**
	 * 非临时户_账户申请开户原因
	 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String createAccountReason;
	/**
	 * 非临时户_项目部名称
	 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String projectName;
	/**
	 * 非临时户_负责人姓名
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String flsFzrLegalName;
	/**
	 * 非临时户_负责人证件类型
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String flsFzrLegalIdcardType;
	/**
	 * 非临时户_负责人证件号码
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String flsFzrLegalIdcardNo;
	/**
	 * 非临时户_负责人联系电话
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String flsFzrTelephone;
	/**
	 * 非临时户_负责人邮政编码
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String flsFzrZipCode;
	/**
	 * 非临时户_负责人地址
	 */
	private String flsFzrAddress;
	/**
	 * 专用户_账户名称构成方式
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String accountNameFrom;
	/**
	 * 专用户_账户后缀
	 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String saccpostfix;
	/**
	 * 专用户_账户前缀
	 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String saccprefix;
	/**
	 * 专用户_资金人姓名
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String moneyManager;
	/**
	 * 专用户_资金身份种类
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String moneyManagerCtype;
	/**
	 * 专用户_资金身份编号
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String moneyManagerCno;
	/**
	 * 专用户_内设部门名称
	 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String insideDepartmentName;
	/**
	 * 专用户_内设部门负责人名称
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String insideSaccdepmanName;
	/**
	 * 专用户_内设部门负责人身份种类
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String insideSaccdepmanKind;
	/**
	 * 专用户_内设部门负责人身份编号
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String insideSaccdepmanNo;
	/**
	 * 专用户_内设部门联系电话
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String insideTelphone;
	/**
	 * 专用户_内设部门邮编
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String insideZipCode;
	/**
	 * 专用户_内设部门地址
	 */
	private String insideAddress;
	/**
	 * 专用户_开户证明文件种类2
	 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String accountFileType2;
	/**
	 * 专用户_开户证明文件编号2
	 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String accountFileNo2;

	// 20170927 --新增字段
	/** 授权经办人姓名 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String agentName;

	/** 授权经办人证件类型 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String agentType;

	/** 授权经办人证件号码 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String agentTypeNo;

	/** 授权经办人证件到期日 */
	@Column(length = DEFAULT_DATE_COLUMN_LENGTH)
	private String agentTypeNoVoidDate;

	/** 法人证件到期日 */
	@Column(length = DEFAULT_DATE_COLUMN_LENGTH)
	private String legalIdcardToVoidDate;

	/** 组织机构代码证件到期日 */
	@Column(length = DEFAULT_DATE_COLUMN_LENGTH)
	private String orgCodeToVoidDate;

	/**
	 * 国税证件到期日
	 *
	 * @return
	 */
	@Column(length = DEFAULT_DATE_COLUMN_LENGTH)
	private String stateTaxToVoidDate;

	/**
	 * 地税证件到期日
	 *
	 * @return
	 */
	@Column(length = DEFAULT_DATE_COLUMN_LENGTH)
	private String taxRegNoToVoidDate;

	/**
	 * 机构信用代码
	 *
	 * @return
	 */
	@Column(length = DEFAULT_DATE_COLUMN_LENGTH)
	private String orgEccsToVoidDate;
	/**
	 * 上级法人证件到期日
	 *
	 * @return
	 */
	@Column(length = DEFAULT_DATE_COLUMN_LENGTH)
	private String parLegalIdcardDate;

	/**
	 * 工商执照证件到期日
	 *
	 * @return
	 */
	@Column(length = DEFAULT_DATE_COLUMN_LENGTH)
	private String tovoidDate;

	/** 高管股东信息姓名 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String stockHoderName;

	/** 股东证件类型 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String stockHoderType;

	/** 股东证件号码 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String stockHoderTypeNo;

	/** 关联企业 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String relateCompanyName;

	/** 关联企业类型 */
	@Column(length = DEFAULT_SELECT_COLUMN_LENGTH)
	private String relateCompanyType;

	/** 关联企业证件 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String relateCompanyFileType;

	/** 关联企业社会信用代码 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String relateCompanyFileNo;

	/** 备用字段 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String string020;

	/** 备用字段 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String string021;

	/** 备用字段 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String string022;

	/** 备用字段 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String string023;

	/** 备用字段 */
	@Column(length = DEFAULT_COLUMN_100_LENGTH)
	private String string024;

	// 20170927 新增字段结束

	// 可配置页面新增相关运营商校验字段 --开始
	/** 财务主管姓名 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String financeName;

	/** 上级法人联系方式 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String parLegalTelephone;

	/** 法人联系电话 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String legalTelephone;

	/** 经办人联系电话 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String agentTypeNoelephone;

	/** 股东联系电话 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String stockHoderTelephone;

	/** 资金管理人联系电话 */
	@Column(length = DEFAULT_COLUMN_50_LENGTH)
	private String moneyManagerTelephone;
	// 可配置页面新增相关运营商校验字段--结束


	public FetchPbcInfo() {
	}

	public FetchPbcInfo(String depositorName, String fileNo, String acctCreateDate, AccountStatus accountStatus) {
		this.depositorName = depositorName;
		this.fileNo = fileNo;
		this.acctCreateDate = acctCreateDate;
		this.accountStatus = accountStatus;
	}

	public FetchPbcInfo(String depositorName, String fileNo, String acctCreateDate, AccountStatus accountStatus,String acctNo) {
		this.depositorName = depositorName;
		this.fileNo = fileNo;
		this.acctCreateDate = acctCreateDate;
		this.accountStatus = accountStatus;
		this.acctNo = acctNo;
	}

	/**
	 * 年检任务ID
	 */
	private Long annualTaskId;

	/**
	 * 采集任务ID
	 */
	private Long collectTaskId;

	/**
	 * 采集账号Id
	 */
	private Long collectAccountId;
	/*
	 * 每条记录添加采集状态字段
	 */
	@Enumerated(EnumType.STRING)
	private CollectState collectState;
}

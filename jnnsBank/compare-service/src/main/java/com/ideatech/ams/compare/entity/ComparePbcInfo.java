package com.ideatech.ams.compare.entity;

import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.common.entity.AmsEntityLength;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * 人行账管系统账户全量表
 * @author zoulang
 * @date 2017年5月10日 上午7:39:42
 */
@Entity
@Data
public class ComparePbcInfo extends BaseMaintainablePo {
	/**
	 * 采集日期 yyyyMMdd hh:mm:ss
	 */
	@Column(length = 30)
	private String parDate;

	/**
	 * 存款人名称
	 */
	@Column(length = 100)
	private String depositorName;
	/**
	 * 银行机构号（人行14位）
	 */
	@Column(length = 100)
	private String bankCode;

	/**
	 * 完整机构ID organFullId
	 */
	private String organFullId;
	/**
	 * 银行机构名称
	 */
	@Column(length = 100)
	private String bankName;
	/**
	 * 账户性质
	 */
	@Enumerated(EnumType.STRING)
	@Column(length = 40)
	private AccountType acctType;
	/**
	 * 账号
	 */
	@Column(length = 50)
	private String acctNo;
	/**
	 * 账户名称
	 */
	@Column(length = 100)
	private String acctName;
	/**
	 * 账户开户日期
	 */
	@Column(length = 30)
	private String acctCreateDate;
	/**
	 * 账户销户日期
	 */
	@Column(length = 30)
	private String cancelDate;
	/**
	 * 存款人类别(基本户、特殊户)
	 */
	@Column(length = 30)
	private String depositorType;
	/**
	 * 注册地地区代码
	 */
	@Column(length = 50)
	private String regAreaCode;
	/**
	 * 组织机构代码
	 */
	@Column(length = 50)
	private String orgCode;
	/**
	 * 证明文件1种类(工商注册类型:非临时、基本户、专用户、一般户、临时户)
	 */
	@Column(length = 30)
	private String fileType;
	/**
	 * 证明文件1编号(工商注册号)
	 */
	@Column(length = 50)
	private String fileNo;
	/**
	 * 证明文件2种类(基本户、专用户、特殊户)
	 */
	@Column(length = 30)
	private String fileType2;
	/**
	 * 证明文件2编号
	 */
	@Column(length = 50)
	private String fileNo2;
	/**
	 * 法人类型（法定代表人、单位负责人）
	 */
	@Column(length = 30)
	private String legalType;
	/**
	 * 法人姓名
	 */
	@Column(length = 50)
	private String legalName;
	/**
	 * 法人证件类型
	 */
	@Column(length = 30)
	private String legalIdcardType;
	/**
	 * 法人证件编号
	 */
	@Column(length = 50)
	private String legalIdcardNo;
	/**
	 * 行业归属
	 */
	private String industryCode;
	/**
	 * 国税
	 */
	@Column(length = 50)
	private String stateTaxRegNo;
	/**
	 * 地税
	 */
	@Column(length = 50)
	private String taxRegNo;
	/**
	 * 无需办理税务登记证的文件或税务机关出具的证明
	 */
	@Column(length = 30)
	private String noTaxProve;
	/**
	 * 注册币种
	 */
	@Column(length = 30)
	private String regCurrencyType;
	/**
	 * 注册资金
	 */
	@Column(length = 50)
	private String registeredCapital;
	/**
	 * 经营（业务）范围（上报人行）
	 */
	@Column(length = 2000)
	private String businessScope;
	/**
	 * 经营（业务）范围（上报信用机构）
	 */
	@Column(length = 2000)
	private String businessScopeEccs;
	/**
	 * 注册（登记）地址
	 */
	@Column(length = 500)
	private String regAddress;
	/**
	 * 工商注册地址
	 */
	@Column(length = 500)
	private String indusRegArea;
	/**
	 * 邮政编码
	 */
	@Column(length = 30)
	private String zipCode;
	/**
	 * 联系电话
	 */
	@Column(length = 50)
	private String telephone;
	/**
	 * 上级单位存款人名称
	 */
	@Column(length = 50)
	private String parCorpName;
	/**
	 * 上级单位基本户开户许可证
	 */
	@Column(length = 30)
	private String parAccountKey;
	/**
	 * 上级组织机构代码
	 */
	@Column(length = 30)
	private String parOrgCode;
	/**
	 * 上级法人类型（法人代表、单位负责人）
	 */
	@Column(length = 30)
	private String parLegalType;
	/**
	 * 上级法人姓名
	 */
	@Column(length = 30)
	private String parLegalName;
	/**
	 * 上级法人证件类型
	 */
	@Column(length = 30)
	private String parLegalIdcardType;
	/**
	 * 上级法人证件编号
	 */
	@Column(length = 50)
	private String parLegalIdcardNo;
	/**
	 * 基本户开户许可证核准号
	 */
	@Column(length = 30)
	private String accountKey;
	/**
	 * 账户状态
	 */
	@Enumerated(EnumType.STRING)
	@Column(length = 40)
	private AccountStatus accountStatus;
	/**
	 * 资金性质
	 */
	@Column(length = 100)
	private String capitalProperty;
	/**
	 * 取现标识
	 */
	@Column(length = 30)
	private String enchashmentType;
	/**
	 * 账户证明文件类型(一般户、预算专户、非预算专户、非临时)
	 */
	@Column(length = 30)
	private String accountFileType;
	/**
	 * 账户证明文件编号(一般户、预算专户、非预算专户、非临时)
	 */
	@Column(length = 50)
	private String accountFileNo;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 非临时_账户有效时间
	 */
	@Column(length = 30)
	private String effectiveDate;
	/**
	 * 非临时户_账户申请开户原因
	 */
	@Column(length = 100)
	private String createAccountReason;
	/**
	 * 非临时户_项目部名称
	 */
	@Column(length = 100)
	private String projectName;
	/**
	 * 非临时户_负责人姓名
	 */
	@Column(length = 50)
	private String flsFzrLegalName;
	/**
	 * 非临时户_负责人证件类型
	 */
	@Column(length = 30)
	private String flsFzrLegalIdcardType;
	/**
	 * 非临时户_负责人证件号码
	 */
	@Column(length = 50)
	private String flsFzrLegalIdcardNo;
	/**
	 * 非临时户_负责人联系电话
	 */
	@Column(length = 50)
	private String flsFzrTelephone;
	/**
	 * 非临时户_负责人邮政编码
	 */
	@Column(length = 50)
	private String flsFzrZipCode;
	/**
	 * 非临时户_负责人地址
	 */
	private String flsFzrAddress;
	/**
	 * 专用户_账户名称构成方式
	 */
	@Column(length = 30)
	private String accountNameFrom;
	/**
	 * 专用户_账户后缀
	 */
	@Column(length = 100)
	private String saccpostfix;
	/**
	 * 专用户_账户前缀
	 */
	@Column(length = 100)
	private String saccprefix;
	/**
	 * 专用户_资金人姓名
	 */
	@Column(length = 50)
	private String moneyManager;
	/**
	 * 专用户_资金身份种类
	 */
	@Column(length = 30)
	private String moneyManagerCtype;
	/**
	 * 专用户_资金身份编号
	 */
	@Column(length = 50)
	private String moneyManagerCno;
	/**
	 * 专用户_内设部门名称
	 */
	@Column(length = 100)
	private String insideDepartmentName;
	/**
	 * 专用户_内设部门负责人名称
	 */
	@Column(length = 50)
	private String insideSaccdepmanName;
	/**
	 * 专用户_内设部门负责人身份种类
	 */
	@Column(length = 30)
	private String insideSaccdepmanKind;
	/**
	 * 专用户_内设部门负责人身份编号
	 */
	@Column(length = 50)
	private String insideSaccdepmanNo;
	/**
	 * 专用户_内设部门联系电话
	 */
	@Column(length = 50)
	private String insideTelphone;
	/**
	 * 专用户_内设部门邮编
	 */
	@Column(length = 50)
	private String insideZipCode;
	/**
	 * 专用户_内设部门地址
	 */
	private String insideAddress;
	/**
	 * 专用户_开户证明文件种类2
	 */
	@Column(length = 30)
	private String accountFileType2;
	/**
	 * 专用户_开户证明文件编号2
	 */
	@Column(length = 50)
	private String accountFileNo2;

	// 20170927 --新增字段
	/** 授权经办人姓名 */
	@Column(length = 50)
	private String agentName;

	/** 授权经办人证件类型 */
	@Column(length = 30)
	private String agentType;

	/** 授权经办人证件号码 */
	@Column(length = 50)
	private String agentTypeNo;

	/** 授权经办人证件到期日 */
	@Column(length = 30)
	private String agentTypeNoVoidDate;

	/** 法人证件到期日 */
	@Column(length = 30)
	private String legalIdcardToVoidDate;

	/** 组织机构代码证件到期日 */
	@Column(length = 30)
	private String orgCodeToVoidDate;

	/**
	 * 国税证件到期日
	 *
	 * @return
	 */
	@Column(length = 30)
	private String stateTaxToVoidDate;

	/**
	 * 地税证件到期日
	 *
	 * @return
	 */
	@Column(length = 30)
	private String taxRegNoToVoidDate;

	/**
	 * 机构信用代码
	 *
	 * @return
	 */
	@Column(length = 30)
	private String orgEccsToVoidDate;
	/**
	 * 上级法人证件到期日
	 *
	 * @return
	 */
	@Column(length = 30)
	private String parLegalIdcardDate;

	/**
	 * 工商执照证件到期日
	 *
	 * @return
	 */
	@Column(length = 30)
	private String tovoidDate;

	/** 高管股东信息姓名 */
	@Column(length = 50)
	private String stockHoderName;

	/** 股东证件类型 */
	@Column(length = 30)
	private String stockHoderType;

	/** 股东证件号码 */
	@Column(length = 50)
	private String stockHoderTypeNo;

	/** 关联企业 */
	@Column(length = 100)
	private String relateCompanyName;

	/** 关联企业类型 */
	@Column(length = 30)
	private String relateCompanyType;

	/** 关联企业证件 */
	@Column(length = 50)
	private String relateCompanyFileType;

	/** 关联企业社会信用代码 */
	@Column(length = 50)
	private String relateCompanyFileNo;

	/** 备用字段 */
	@Column(length = 100)
	private String string020;

	/** 备用字段 */
	@Column(length = 100)
	private String string021;

	/** 备用字段 */
	@Column(length = 100)
	private String string022;

	/** 备用字段 */
	@Column(length = 100)
	private String string023;

	/** 备用字段 */
	@Column(length = 100)
	private String string024;

	// 20170927 新增字段结束

	// 可配置页面新增相关运营商校验字段 --开始
	/** 财务主管姓名 */
	@Column(length = 50)
	private String financeName;

	/** 上级法人联系方式 */
	@Column(length = 50)
	private String parLegalTelephone;

	/** 法人联系电话 */
	@Column(length = 50)
	private String legalTelephone;

	/** 经办人联系电话 */
	@Column(length = 50)
	private String agentTypeNoelephone;

	/** 股东联系电话 */
	@Column(length = 50)
	private String stockHoderTelephone;

	/** 资金管理人联系电话 */
	@Column(length = 50)
	private String moneyManagerTelephone;
	// 可配置页面新增相关运营商校验字段--结束
}

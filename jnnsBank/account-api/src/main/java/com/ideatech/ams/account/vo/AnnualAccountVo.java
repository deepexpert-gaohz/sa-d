package com.ideatech.ams.account.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ideatech.ams.account.enums.AccountClass;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.AcctBigType;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.*;
import com.ideatech.common.eav.EavJsonSerializer;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 整合账户客户信息以及账户流水信息的账户(多返回给前台使用，后台处理请使用accountBillsAllInfo)
 * 与accountBillsAllInfo的区别是->后者的账户log信息以类形式保存
 * @author van
 * @date 15:53 2018/8/15
 */
@Data
@JsonSerialize(using = EavJsonSerializer.class)
public class AnnualAccountVo implements Serializable {

	private static final long serialVersionUID = 2637403241898181613L;

	private Long id;

	/////流水信息-start//////

	/**
	 * 账户logid
	 */
	private Long accountLogId;
	/**
	 * 单据编号
	 */
	private String billNo;
	/**
	 * 单据日期
	 */
	private String billDate;
	/**
	 * 单据类型（01开户申请、02变更申请、03销户申请、04久悬申请）
	 */
	private BillType billType;
	/**
	 * 单据状态(01新建、02审核中、03已审核、04驳回、05核心开户)
	 */
	private BillStatus status;
	/**
	 * 审核人id
	 */
	private Long approver;
	/**
	 * 审核日期
	 */
	private String approveDate;
	/**
	 * 审核/驳回说明
	 */
	private String approveDesc;
	/**
	 * 退回原因
	 */
	private String denyReason;
	/**
	 * 人行核准状态(01待审核、02审核通过、03无需审核)
	 */
	private CompanyAmsCheckStatus pbcCheckStatus;
	/**
	 * 人行核准日期
	 */
	private String pbcCheckDate;

	/**
	 * 描述
	 */
	private String description;
	/**
	 * 上报人行账管状态（01成功；02失败；03无需上报）
	 */
	private CompanySyncStatus pbcSyncStatus;
	/**
	 * 上报人行账管错误信息
	 */
	private String pbcSyncError;
	/**
	 * 上报操作人
	 */
	private Long pbcOperator;
	/**
	 * 上报人行账管时间
	 */
	private String pbcSyncTime;
	/**
	 * 校验账户报备是否成功
	 */
	private SyncCheckStatus pbcSyncCheck;
	/**
	 * 账户上报方式(01手工上报02自动上报03手工补录04手工虚拟上报05线下手工报备)
	 */
	private CompanySyncOperateType pbcSyncMethod;
	/**
	 * 上报信用代码证状态（01成功；02失败；03无需上报）
	 */
	private CompanySyncStatus eccsSyncStatus;
	/**
	 * 上报信用代码证错误信息
	 */
	private String eccsSyncError;
	/**
	 * 上报操作人
	 */
	private Long eccsOperator;
	/**
	 * 上报信用代码证时间
	 */
	private String eccsSyncTime;
	/**
	 * 账户报备成功后，再次校验是否报备成功
	 */
	private SyncCheckStatus eccsSyncCheck;
	/**
	 * T+1是否来自核心(0否1是)
	 */
	private CompanyIfType acctIsFromCore;
	/**
	 * T+1数据是否完整(0否1是)
	 */
	private CompanyIfType coreDataCompleted;
	/**
	 * T+1账户是否需要手工处理(0否1是)
	 */
	private CompanyIfType handingMark;
	/**
	 * 流水最终状态标识(0否1是)
	 */
	private CompanyIfType finalStatus;
	/**
	 * 流水初始化待补录(完整性)状态(0-无需补录、1-待补录、2-已补录)
	 */
	private String initFullStatus;
	/**
	 * 流水初始化备注信息
	 */
	private String initRemark;
	/**
	 * 流水来源(预填单、核心T+0、核心T+1、AMS)
	 */
	private BillFromSource fromSource;
	/**
	 * 原始流水id（变更销户久悬时，存前一笔流水id）
	 */
	private Long originalBillId;
	/**
	 * 变更字段的流水是否需要上报人行(0:无上报字段  1：包含上报字段)
	 */
	private String changeFieldIsPbcSync;
	/**
	 * 变更字段的流水是否需要上报信用机构(0:无上报字段  1：包含上报字段)
	 */
	private String changeFieldIsEccsSync;

	/**
	 * 核心同步状态
	 */
	private CompanySyncStatus coreSyncStatus;

	/**
	 * 上报人行账管错误信息
	 */
	private String coreSyncError;
	/**
	 * 上报操作人
	 */
	private Long coreOperator;
	/**
	 * 上报人行账管时间
	 */
	private String coreSyncTime;
	/**
	 * 账户上报方式(01手工上报02自动上报03手工补录04手工虚拟上报05线下手工报备)
	 */
	private CompanySyncOperateType coreSyncMethod;

	/**
	 * 人行查询流水信息
	 */
	private Long pbcQuerySerialNo;

	/**
	 * 工商查询流水信息
	 */
	private Long saicQuerySerialNo;
	/////流水信息-start//////

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
	private AccountClass accountClass;

	/**
	 * 账户状态
	 */
	private AccountStatus accountStatus;

	/**
	 * 账户开户日期
	 */
	private String acctCreateDate;

	/**
	 * 销户原因
	 */
	private String acctCancelReason;

	/**
	 * 开户银行代码(12位联行号)
	 */
	private String bankCode;
	/**
	 * 开户银行名称
	 */
	private String bankName;


	/**
	 * 账户激活日期
	 */
	private String acctActiveDate;

	/**
	 * 账户销户日期
	 */
	private String cancelDate;

	/**
	 * 久悬日期
	 */
	private String acctSuspenDate;

	/**
	 * 客户号
	 */
	private String customerNo;

	/**
	 * 完整机构ID
	 */
	private String organFullId;

	/**
	 * 关联账户单据ID
	 */
	private Long refBillId;


	/////以下字段应放在public中，为兼容老版本不做调整/////
	/**
	 * 账户有效期(临时账户)
	 */
	private String effectiveDate;

	/**
	 * 申请开户原因(非临时机构临时存款账户)
	 */
	private String acctCreateReason;

	/**
	 * 备注
	 * 字段在public中也有，应使用public中的remark
	 */
	///private String remark;
	/////end/////

	/**
	 * 币种
	 * 此字段新版本已弃用,使用客户信息中的注册币种
	 */
	private String currencyType;

	/**
	 * 账户ID
	 */
	private Long accountId;

	/**
	 * 账户性质大类(1基本户、2一般户、3专用户、4临时户)
	 */
	private AcctBigType acctBigType;

	/**
	 * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7 非预算单位专用存款账户)
	 */
	private CompanyAcctType acctType;

	/**
	 * 账户许可核准号
	 */
	private String accountLicenseNo;

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

	/////预算-start/////

	/**
	 * 账户名称构成方式(预算户)
	 */
	private String accountNameFrom;

	/**
	 * 账户前缀(预算户)
	 */
	private String saccprefix;

	/**
	 * 账户后缀(预算户)
	 */
	private String saccpostfix;

	/**
	 * 资金性质(预算户)
	 */
	private String capitalProperty;

	/**
	 * 取现标识(预算户)
	 */
	private String enchashmentType;

	/**
	 * 资金管理人姓名(预算户)
	 */
	private String fundManager;

	/**
	 * 资金管理人身份证种类(预算户)
	 */
	private String fundManagerIdcardType;

	/**
	 * 资金管理人身份证编号(预算户)
	 */
	private String fundManagerIdcardNo;

	/**
	 * 资金管理人证件到期日(预算户)
	 */
	private String fundManagerIdcardDue;

	/**
	 * 资金管理人联系电话(预算户)
	 */
	private String fundManagerTelephone;

	/**
	 * 内设部门名称(预算户)
	 */
	private String insideDeptName;

	/**
	 * 内设部门负责人名称(预算户)
	 */
	private String insideLeadName;

	/**
	 * 内设部门负责人身份种类(预算户)
	 */
	private String insideLeadIdcardType;

	/**
	 * 内设部门负责人身份编号(预算户)
	 */
	private String insideLeadIdcardNo;

	/**
	 * 负责人证件到期日(预算户)
	 */
	private String insideLeadIdcardDue;

	/**
	 * 内设部门联系电话(预算户)
	 */
	private String insideTelephone;

	/**
	 * 内设部门地址(预算户)
	 */
	private String insideAddress;

	/**
	 * 内设部门邮编(预算户)
	 */
	private String insideZipcode;

	/////预算-end/////

	/////非临时-start/////

	/**
	 * 非临时项目部名称(非临时)
	 */
	private String nontmpProjectName;

	/**
	 * 非临时负责人姓名(非临时)
	 */
	private String nontmpLegalName;

	/**
	 * 非临时联系电话(非临时)
	 */
	private String nontmpTelephone;

	/**
	 * 非临时邮政编码(非临时)
	 */
	private String nontmpZipcode;

	/**
	 * 非临时地址(非临时)
	 */
	private String nontmpAddress;

	/**
	 * 非临时身份证件种类(非临时)
	 */
	private String nontmpLegalIdcardType;

	/**
	 * 非临时身份证件编号(非临时)
	 */
	private String nontmpLegalIdcardNo;

	/**
	 * 非临时身份证件到期日(非临时)
	 */
	private String nontmpLegalIdcardDue;

	/////非临时-end/////

	/////经办人信息-start/////
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
	 * 业务经办人证件起始日期
	 */
	private String operatorIdcardStartDate;

	/**
	 * 业务经办人国籍
	 */
	private String operatorNationality;


	/////经办人信息-start/////
	/**
	 * 备注
	 */
	private String remark;

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
	private String string008;

	/**
	 * 扩展字段9
	 */
	private String string009;

	/**
	 * 扩展字段10
	 */
	private String string010;


	/////以下字段不确定/////
	/**
	 * 存款人类别
	 */
	private String depositorType;

	/**
	 * 客户id
	 */
	private Long customerLogId;

	/**
	 * 客户log表信息
	 */
//	private CustomerPublicLogInfo customerPublicLogInfo;

	private String depositorName;

	private String createdDate;

	private String createdBy;

	//年检赋值新增字段
	private String legalName;

	private String organCode;

	private String businessScope;

	private String regAddress;

	/**
	 * 组织机构代码
	 */
	private String orgCode;

	/**
	 * 注册地地区代码
	 */
	private String regAreaCode;

	/**
	 * 注册资金（元）
	 */
	private BigDecimal registeredCapital;

	/**
	 * 工商注册号
	 */
	private String regNo;
	/**
	 * 完整注册地址(与营业执照一致)
	 */
	private String regFullAddress;

	/**
	 * 法人证件种类
	 */
	private String legalIdcardType;

	/**
	 * 法人证件号码
	 */
	private String legalIdcardNo;

	/**
	 * 注册币种
	 */
	private String regCurrencyType;

	/**
	 * 国税登记证
	 */
	private String stateTaxRegNo;

	/**
	 * 地税登记证
	 */
	private String taxRegNo;

	//比对规则字段勾选新增---开始
	/**
	 * 营业执照到期日
	 */
	private String businessLicenseDue;
	/**
	 * 成立日期
	 */
	private String setupDate;
	/**
	 * 登记部门
	 */
	private String regOffice;
	/**
	 * 经济行业分类
	 */
	private String economyIndustryName;
	/**
	 * 行业归属
	 */
	private String industryCode;
	/**
	 * 邮政编码
	 */
	private String zipcode;
	/**
	 * 联系电话
	 */
	private String telephone;
	/**
	 * 证明文件1编号
	 */
	private String fileNo;
	/**
	 * 证明文件1种类
	 */
	private String fileType;
	/**
	 * 基本开户许可核准号
	 */
	private String accountKey;
	//比对规则字段勾选新增---结束

}

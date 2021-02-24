package com.ideatech.ams.pbc.dto;


import com.ideatech.ams.pbc.enums.SyncAcctType;
import com.ideatech.ams.pbc.enums.SyncOperateType;
import lombok.Data;

/**
 * 非临时（取消核准）
 * 
 * @author
 *
 */
@Data
public class AmsFeilinshiSyncCondition {

	/**
	 * 账户性质
	 */
	private SyncAcctType acctType;

	/**
	 * 业务操作类型
	 */
	private SyncOperateType operateType;

	/**
	 * 基本存款账户编号
	 */
	private String accountKey;
	/**
	 * 开户许可证号
	 */
	private String accountLicenseNo;

	/**
	 * 许可证对应的注册地区代码
	 */
	private String accountKeyRegAreaCode;
	/**
	 * 基本户注册地地区代码
	 */
	private String regAreaCode;
	/**
	 * 银行机构代码
	 */
	private String bankCode;

	private String bankName;
	/**
	 * 机构所在地地区代码
	 */
	private String bankAreaCode;
	/**
	 * 账号
	 */
	private String acctNo;
	/**
	 * 账户开户日期
	 */
	private String acctCreateDate;

	/**
	 * 账户证明文件种类
	 */
	private String accountFileType;

	/**
	 * 账户证明文件编号
	 */
	private String accountFileNo;

	/**
	 * 本地异地标识(1本地、2异地)
	 */
	private String openAccountSiteType;
	/**
	 * 备注
	 */
	private String remark;

	private String saccprefix; // 前缀

	private String saccpostfix; // 后缀

	private String effectiveDate;// 非临时_有效时间

	private String createAccountReason;// 非临时_申请开户原因

	private String flsProjectName; // 非临时_项目部名称

	private String flsFzrAddress;// 非临时负责人地址

	private String flsFzrZipCode;// 非临时负责人邮政编码

	private String flsFzrTelephone;// 非临时负责人电话

	private String flsFzrLegalIdcardType;// 非临时负责人证件类型

	private String flsFzrLegalIdcardNo;// 非临时负责人证件号码

	private String flsFzrLegalName;// 非临时负责人姓名

	private  String OpenKey;//非临时开户许可证号

	private String acctName;//账户名称

	private String legalName;//法定代表人（单位负责人）姓名

	private String printDate;//打印日期

	private String selectPwd;//查询密码

	/**
	 * 异地开户时隐藏域字段
	 */
	private String sDepRegAreaHidden;

	private String currencyType;
	private String currency0;
	private String currency1;
}

package com.ideatech.ams.pbc.dto;

import lombok.Data;

/**
 * 非预算单位专用存款账户同步字段(条件)包括开户、变更
 * 
 * @author zoulang
 *
 */
@Data
public class AmsFeiyusuanSyncCondition extends AmsYibanSyncCondition {

	/**
	 * 账户名称
	 */
	private String acctName;

	/**
	 * 账户名称构成方式
	 */
	private String accountNameFrom;

	/**
	 * 资金性质
	 */
	private String capitalProperty;

	/**
	 * 资金人姓名
	 */
	private String moneyManager;

	/**
	 * 资金身份种类
	 */
	private String moneyManagerCtype;

	/**
	 * 资金身份编号
	 */
	private String moneyManagerCno;

	/**
	 * _内设部门名称
	 */
	private String insideDepartmentName;

	/**
	 * 内设负责人名称
	 */
	private String insideSaccdepmanName;

	/**
	 * _内设负责人身份种类
	 */
	private String insideSaccdepmanKind;

	/**
	 * 内设身份编号
	 */
	private String insideSaccdepmanNo;

	/**
	 * 预算_内设电话
	 */
	private String insideTelphone;

	/**
	 * 预算_内设编码
	 */
	private String insideZipCode;

	/**
	 * 预算_内设地址
	 */
	private String insideAddress;

	/**
	 * 后缀
	 */
	private String saccpostfix;

	private String saccprefix;
	private String openAccountSiteType;
	private String currencyType;
	private String currency0;
	private String currency1;
}

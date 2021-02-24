package com.ideatech.ams.pbc.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @Description: 账户性质
 * @author zoulang
 * @date 2017年5月10日 上午7:45:11
 */
public enum AccountType {
	/**
	 * 基本户
	 */
	jiben("1", "基本存款账户"),

	/**
	 * 预算单位专用存款账户
	 */
	yusuan("2", "预算单位专用存款账户"),
	/**
	 * 临时机构临时存款账户
	 */
	linshi("3", "临时机构临时存款账户"),
	/**
	 * 非临时机构临时存款账户
	 */
	feilinshi("4", "非临时机构临时存款账户"),
	/**
	 * 特殊单位专用存款账户
	 */
	teshu("5", "特殊单位专用存款账户"),
	/**
	 * 一般单位专用存款账户
	 */
	yiban("6", "一般存款账户"),
	/**
	 * 非预算单位专用存款账户
	 */
	feiyusuan("7", "非预算单位专用存款账户");

	private String value;

	private String fullName;

	AccountType(String value, String fullName) {
		this.value = value;
		this.fullName = fullName;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public static AccountType str2enum(String acctType) {
		if (StringUtils.isBlank(acctType)) {
			return null;
		}
		if (acctType.equals("jiben") || acctType.equals("基本存款账户")) {
			return AccountType.jiben;
		} else if (acctType.equals("yiban") || acctType.equals("一般存款账户")) {
			return AccountType.yiban;
		} else if (acctType.equals("linshi") || acctType.equals("临时机构临时存款账户")) {
			return AccountType.linshi;
		} else if (acctType.equals("feilinshi") || acctType.equals("非临时机构临时存款账户")) {
			return AccountType.feilinshi;
		} else if (acctType.equals("teshu") || acctType.equals("特殊单位专用存款账户")) {
			return AccountType.teshu;
		} else if (acctType.equals("feiyusuan") || acctType.equals("非预算单位专用存款账户")) {
			return AccountType.feiyusuan;
		} else if (acctType.equals("yusuan") || acctType.equals("预算单位专用存款账户")) {
			return AccountType.yusuan;
		}
		return null;
	}
}

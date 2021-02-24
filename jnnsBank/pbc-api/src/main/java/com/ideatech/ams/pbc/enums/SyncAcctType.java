package com.ideatech.ams.pbc.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 账户性质
 * 
 * @author zoulang
 *
 */
public enum SyncAcctType {
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

	SyncAcctType(String value, String fullName) {
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

	public static SyncAcctType str2enum(String acctType) {
		if (StringUtils.isBlank(acctType)) {
			return null;
		}
		if (acctType.equals("jiben") || acctType.contains("基本存款账户") || acctType.equals("1")) {
			return SyncAcctType.jiben;
		} else if (acctType.equals("yiban") || acctType.contains("一般存款账户") || acctType.equals("6")) {
			return SyncAcctType.yiban;
		} else if (acctType.equals("feilinshi") || acctType.contains("非临时机构临时存款账户") || acctType.equals("4")) {
			return SyncAcctType.feilinshi;
		} else if (acctType.equals("linshi") || acctType.contains("临时机构临时存款账户") || acctType.equals("3")) {
			return SyncAcctType.linshi;
		} else if (acctType.equals("teshu") || acctType.contains("特殊单位专用存款账户") || acctType.equals("5")) {
			return SyncAcctType.teshu;
		} else if (acctType.equals("feiyusuan") || acctType.contains("非预算单位专用存款账户") || acctType.equals("7")) {
			return SyncAcctType.feiyusuan;
		} else if (acctType.equals("yusuan") || acctType.contains("预算单位专用存款账户") || acctType.equals("2")) {
			return SyncAcctType.yusuan;
		}
		return null;
	}

}
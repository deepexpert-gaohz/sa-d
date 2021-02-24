package com.ideatech.ams.account.enums.bill;

import org.apache.commons.lang.StringUtils;

/**
 * 流水审核状态
 *
 * @author jogy.he
 */
public enum BillFromSource {

	AMS("账管系统"),

	PRE_BILL("预填单系统"),

	INIT("初始核心导入"),

	CORE("核心系统"),

	HEABO("Heabo");

	BillFromSource(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static BillFromSource str2enum(String enmuStr) {
		if (StringUtils.isBlank(enmuStr)) {
			return null;
		}
		if (enmuStr.equalsIgnoreCase("AMS")) {
			return BillFromSource.AMS;
		} else if (enmuStr.equalsIgnoreCase("prebill")) {
			return BillFromSource.PRE_BILL;
		} else if (enmuStr.equalsIgnoreCase("init")) {
			return BillFromSource.INIT;
		} else if (enmuStr.equalsIgnoreCase("core")) {
			return BillFromSource.CORE;
		} else if (enmuStr.equalsIgnoreCase("heabo")) {
			return BillFromSource.HEABO;
		} else {
			return null;
		}
	}

}

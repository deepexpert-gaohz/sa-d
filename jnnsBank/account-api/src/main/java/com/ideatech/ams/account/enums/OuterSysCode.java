package com.ideatech.ams.account.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @ClassName: OuterSysCode
 * @Description: TODO(外围系统代码)
 * @author jogy.he
 * @date 2017年10月28日 上午10:32:02
 *
 */
public enum OuterSysCode {

	PBC("人行账管系统"),

	ECCS("信用代码证系统"),

	CORE("核心业务系统"),

	IMAGE("影像平台"),

	EV("电子验印");

	OuterSysCode(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static OuterSysCode str2enum(String sync) {
		if (StringUtils.isBlank(sync)) {
			return null;
		}
		if (sync.equalsIgnoreCase("pbc")) {
			return OuterSysCode.PBC;
		} else if (sync.equalsIgnoreCase("eccs")) {
			return OuterSysCode.ECCS;
		} else if (sync.equalsIgnoreCase("core")) {
			return OuterSysCode.CORE;
		} else if (sync.equalsIgnoreCase("image")) {
			return OuterSysCode.IMAGE;
		} else if (sync.equalsIgnoreCase("ev")) {
			return OuterSysCode.EV;
		} 
		return null;
	}

}

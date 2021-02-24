package com.ideatech.ams.apply.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author RJQ
 * 客户类别
 */
public enum CustomerType {
	personal("个人"),
	publics("对公");
	
	private String value;
	
	CustomerType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static CustomerType str2enum(String type) {
		if (StringUtils.isBlank(type)) {
			return null;
		}
		if (type.equals("personal") || type.equals("个人")) {
			return CustomerType.personal;
		} else if (type.equals("publics") || type.equals("对公")) {
			return CustomerType.publics;
		}
		
		return null;
	}
	
}

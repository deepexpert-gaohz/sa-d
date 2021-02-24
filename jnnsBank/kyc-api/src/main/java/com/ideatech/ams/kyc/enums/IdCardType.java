package com.ideatech.ams.kyc.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 身份证的代次
 * @author yang
 *
 */
public enum IdCardType {

	ONE("1"),
	TWO("2");
	private String value;
	IdCardType(String value){
		this.value = value;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public static IdCardType str2enum(String type){
		if (StringUtils.isBlank(type)) {
			return null;
		}
		if("ONE".equals(type) || "1".equals(type)){
			return IdCardType.ONE;
		}
		if("TWO".equals(type) || "2".equals(type)){
			return IdCardType.TWO;
		}
		return null;
	}
}

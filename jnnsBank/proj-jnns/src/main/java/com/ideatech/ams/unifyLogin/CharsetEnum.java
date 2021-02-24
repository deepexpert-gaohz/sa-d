package com.ideatech.ams.unifyLogin;

public enum CharsetEnum {

	/**
	 * UTF-8
	 */
	UTF_8("UTF-8"),
	
	/**
	 * GBK
	 */
	GBK("GBK"),
	
	/**
	 * iso-8859-1
	 */
	ISO8859_1("iso-8859-1");
	
	private String code;

	private CharsetEnum(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}


package com.ideatech.ams.annual.enums;

/**
 * @Description: 账户采集类型
 * @author ft
 * @date 2017年9月19日10:07:56
 */
public enum CollectType {

	CONTINUE("继续采集"), AFRESH("重新采集"), ANNUAL("年检采集");

	private String fullName;

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

	CollectType(String fullName) {
		this.fullName = fullName;
	}
}

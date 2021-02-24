package com.ideatech.ams.compare.enums;

/**
 * @Description: 数据源采集类型
 * @author ft
 * @date 2017年9月19日10:07:56
 */
public enum CollectType {

	ONLINE("在线采集"), IMPORT("手动导入");

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

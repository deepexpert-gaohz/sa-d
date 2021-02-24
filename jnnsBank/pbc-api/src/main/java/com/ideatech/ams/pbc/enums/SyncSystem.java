package com.ideatech.ams.pbc.enums;

/**
 * 同步的系统
 * 
 * @author zoulang
 *
 */
public enum SyncSystem {

	eccs("机构信用代码证系统"), ams("人行账管系统");

	private SyncSystem(String fullName) {
		this.fullName = fullName;
	}

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

}

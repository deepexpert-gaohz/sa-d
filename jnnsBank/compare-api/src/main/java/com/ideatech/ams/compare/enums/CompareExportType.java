package com.ideatech.ams.compare.enums;

/**
 * 比对结果导出类型
 * 
 */
public enum CompareExportType {

	MATCH("比对一致结果"),

	NOT_MATCH("比对一致结果"),

	ALL("所有结果");

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

	CompareExportType(String fullName) {
		this.fullName = fullName;
	}
}

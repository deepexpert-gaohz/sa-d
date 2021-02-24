package com.ideatech.ams.compare.enums;


/**
 * @Description: 账户采集状态
 * @author zoulang
 * @date 2017年5月10日 上午7:47:40
 */
public enum CollectState {

	init("未采集"), success("采集成功"), fail("采集失败"),noNeed("无需采集");

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

	CollectState(String fullName) {
		this.fullName = fullName;
	}
}

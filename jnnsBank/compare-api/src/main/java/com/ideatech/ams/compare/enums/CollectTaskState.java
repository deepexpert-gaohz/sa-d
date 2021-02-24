package com.ideatech.ams.compare.enums;


/**
 * @Description: 账户采集状态
 * @author zoulang
 * @date 2017年5月10日 上午7:47:40
 */
public enum CollectTaskState {
	//-1 未开始 0 正在采集 1 采集成功 2 采集暂停 3 失败 4 关闭等待服务开始
	init("-1","未开始"), collecting("0","正在采集"), done("1","采集成功"), pause("2","采集暂停"),fail("3","采集失败"),waitToOpen("4","服务关闭等待开始");

	private String fullName;

	private String chName;
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	public String getChName() {
		return chName;
	}

	public void setChName(String chName) {
		this.chName = chName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	CollectTaskState(String fullName,String chName) {
		this.fullName = fullName;
		this.chName = chName;
	}
}

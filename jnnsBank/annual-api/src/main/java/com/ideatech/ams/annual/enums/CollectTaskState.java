package com.ideatech.ams.annual.enums;


/**
 * @Description: 账户采集状态
 * @author zoulang
 * @date 2017年5月10日 上午7:47:40
 */
public enum CollectTaskState {
	//-1 未开始 0 正在采集 1 采集成功 2 采集暂停 3 工商文件下载 4 在线工商启动 5 失败 6 关闭等待服务开始
	init("-1"), collecting("0"), done("1"), pause("2"),readyDownloadSaic("3"),readyStartSaic("4"),fail("5"),waitToOpen("6");

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

	CollectTaskState(String fullName) {
		this.fullName = fullName;
	}
}

package com.ideatech.ams.system.log.dto;


public class SysLogInfo  {
	// 系统日志生成时间
	private String syslogDate;

	// 系统日志内容即文件名
	private String syslogContent;

	// 系统日志文件路径（下载用）
	private String syslogFilePath;

	/**
	 * @return the syslogDate
	 */
	public String getSyslogDate() {
		return syslogDate;
	}

	/**
	 * @param syslogDate
	 *            the syslogDate to set
	 */
	public void setSyslogDate(String syslogDate) {
		this.syslogDate = syslogDate;
	}

	/**
	 * @return the syslogContent
	 */
	public String getSyslogContent() {
		return syslogContent;
	}

	/**
	 * @param syslogContent
	 *            the syslogContent to set
	 */
	public void setSyslogContent(String syslogContent) {
		this.syslogContent = syslogContent;
	}

	/**
	 * @return the syslogFilePath
	 */
	public String getSyslogFilePath() {
		return syslogFilePath;
	}

	/**
	 * @param syslogFilePath
	 *            the syslogFilePath to set
	 */
	public void setSyslogFilePath(String syslogFilePath) {
		this.syslogFilePath = syslogFilePath;
	}

}

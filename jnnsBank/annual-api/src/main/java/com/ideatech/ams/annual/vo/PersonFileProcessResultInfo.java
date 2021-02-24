package com.ideatech.ams.annual.vo;

import java.util.List;

/**
 * 个人文件报备到人行后处理结果
 * 
 * @author zl
 *
 */
public class PersonFileProcessResultInfo {

	/**
	 * 下载个人文件处理结果
	 * 
	 * <pre>
	 * 下载时找到对应文件 则处理true
	 * 下载时找到对应文件  则处理false
	 * </pre>
	 */
	private boolean fileExamineStatus;

	/**
	 * 文件处理失败原因
	 */
	private String failReason;

	/**
	 * 提交失败账户数量
	 */
	private String failSubmitNum;

	/**
	 * 下载错误页面返回的html
	 */
	private String html;
	/**
	 * 提交失败的个人账户列表
	 */
	private List<String> failSubmitAccountList;

	public PersonFileProcessResultInfo() {
		fileExamineStatus = true;
	}

	/**
	 * @return the fileExamineStatus
	 */
	public boolean isFileExamineStatus() {
		return fileExamineStatus;
	}

	/**
	 * @param fileExamineStatus
	 *            the fileExamineStatus to set
	 */
	public void setFileExamineStatus(boolean fileExamineStatus) {
		this.fileExamineStatus = fileExamineStatus;
	}

	/**
	 * @return the failReason
	 */
	public String getFailReason() {
		return failReason;
	}

	/**
	 * @param failReason
	 *            the failReason to set
	 */
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	/**
	 * @return the failSubmitAccountList
	 */
	public List<String> getFailSubmitAccountList() {
		return failSubmitAccountList;
	}

	/**
	 * @param failSubmitAccountList
	 *            the failSubmitAccountList to set
	 */
	public void setFailSubmitAccountList(List<String> failSubmitAccountList) {
		this.failSubmitAccountList = failSubmitAccountList;
	}

	/**
	 * @return the failSubmitNum
	 */
	public String getFailSubmitNum() {
		return failSubmitNum;
	}

	/**
	 * @param failSubmitNum
	 *            the failSubmitNum to set
	 */
	public void setFailSubmitNum(String failSubmitNum) {
		this.failSubmitNum = failSubmitNum;
	}

	/**
	 * @return the html
	 */
	public String getHtml() {
		return html;
	}

	/**
	 * @param html
	 *            the html to set
	 */
	public void setHtml(String html) {
		this.html = html;
	}

}

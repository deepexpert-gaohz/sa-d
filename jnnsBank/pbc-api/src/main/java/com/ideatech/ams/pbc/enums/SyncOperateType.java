package com.ideatech.ams.pbc.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 业务类型
 * 
 * @author zoulang
 *
 */
public enum SyncOperateType {

	ACCT_INIT("初始化"),

	ACCT_OPEN("开户"),

	ACCT_CHANGE("变更"),

	ACCT_SUSPEND("久悬"),

	ACCT_REVOKE("销户"),

	ACCT_EXTENSION("展期"),

	ACCT_CLOSESUSPEND("撤销久悬"),
	ACCT_SEARCH("查询");

	private String fullName;

	SyncOperateType(String fullName) {
		this.fullName = fullName;
	}

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

	public static SyncOperateType str2enum(String acctType) {
		if (StringUtils.isBlank(acctType)) {
			return null;
		}
		if (acctType.equals("ACCT_OPEN") || acctType.contains("开户") || acctType.equals("1")) {
			return SyncOperateType.ACCT_OPEN;
		} else if (acctType.equals("ACCT_CHANGE") || acctType.contains("变更") || acctType.equals("2")) {
			return SyncOperateType.ACCT_CHANGE;
		} else if (acctType.equals("ACCT_SUSPEND") || acctType.contains("久悬") || acctType.equals("3")) {
			return SyncOperateType.ACCT_SUSPEND;
		} else if (acctType.equals("ACCT_REVOKE") || acctType.contains("销户") || acctType.contains("撤销") || acctType.equals("4")) {
			return SyncOperateType.ACCT_REVOKE;
		} else if (acctType.equals("ACCT_SEARCH") || acctType.contains("查询")) {
			return SyncOperateType.ACCT_SEARCH;
		}else if (acctType.equals("ACCT_EXTENSION") || acctType.contains("展期")) {
			return SyncOperateType.ACCT_EXTENSION;
		}else if (acctType.equals("ACCT_CLOSESUSPEND") || acctType.contains("撤销久悬")) {
			return SyncOperateType.ACCT_CLOSESUSPEND;
		}
		return null;
	}

}

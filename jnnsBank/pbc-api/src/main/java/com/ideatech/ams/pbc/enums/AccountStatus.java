package com.ideatech.ams.pbc.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @Description: 账户状态
 * @author zoulang
 * @date 2018年5月9日 上午7:47:40
 */
public enum AccountStatus {
	normal("正常"), suspend("久悬"), revoke("销户"), unKnow("未知"), notExist("不存在"), notActive("未激活");

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

	AccountStatus(String fullName) {
		this.fullName = fullName;
	}


    public static AccountStatus str2enum(String enmuStr) {
        if (StringUtils.isBlank(enmuStr)) {
            return null;
        }
        if (enmuStr.equals("normal") || enmuStr.equals("正常")) {
            return AccountStatus.normal;
        } else if (enmuStr.equals("suspend") || enmuStr.equals("久悬")) {
            return AccountStatus.normal;
        } else if (enmuStr.equals("revoke") || enmuStr.equals("销户")) {
            return AccountStatus.revoke;
        } else if (enmuStr.equals("unKnow") || enmuStr.equals("未知")) {
            return AccountStatus.unKnow;
        } else if (enmuStr.equals("notExist") || enmuStr.equals("不存在")) {
            return AccountStatus.notExist;
        } else if (enmuStr.equals("notActive") || enmuStr.equals("未激活")) {
            return AccountStatus.notActive;
        } else {
            return null;
        }
    }
    
	public static AccountStatus str2enumByAmsAcctStatus(String accountStatusStr) {
		if (StringUtils.isBlank(accountStatusStr)) {
			return null;
		}
		if (accountStatusStr.equals("正常")) {
			return AccountStatus.normal;
		} else if (accountStatusStr.equals("销户") || accountStatusStr.equals("撤销")) {
			return AccountStatus.revoke;
		} else if (accountStatusStr.equals("久悬")) {
			return AccountStatus.suspend;
		} else if (accountStatusStr.equals("未激活")) {
			return AccountStatus.notActive;
		} else if (accountStatusStr.equals("不存在")) {
			return AccountStatus.notExist;
		} else {
			return AccountStatus.unKnow;
		}
	}
}

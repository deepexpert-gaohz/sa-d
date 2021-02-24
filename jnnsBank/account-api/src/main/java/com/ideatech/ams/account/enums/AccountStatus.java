package com.ideatech.ams.account.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author zoulang
 * @Description: 账户状态
 * @date 2017年5月10日 上午7:47:40
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
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    AccountStatus(String fullName) {
        this.fullName = fullName;
    }

    public static AccountStatus str2enum(String accountStatusStr) {
        if (StringUtils.isBlank(accountStatusStr)) {
            return null;
        }
        if (accountStatusStr.equals("正常") || accountStatusStr.equals("normal")) {
            return AccountStatus.normal;
        } else if (accountStatusStr.equals("销户") || accountStatusStr.equals("撤销") || accountStatusStr.equals("revoke")) {
            return AccountStatus.revoke;
        } else if (accountStatusStr.equals("久悬") || accountStatusStr.equals("suspend")) {
            return AccountStatus.suspend;
        } else if (accountStatusStr.equals("未激活") || accountStatusStr.equals("notActive")) {
            return AccountStatus.notActive;
        } else if (accountStatusStr.equals("不存在") || accountStatusStr.equals("notExist")) {
            return AccountStatus.notExist;
        } else {
            return AccountStatus.unKnow;
        }
    }
}

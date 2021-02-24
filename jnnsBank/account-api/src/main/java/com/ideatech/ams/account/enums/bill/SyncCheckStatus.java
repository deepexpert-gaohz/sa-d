package com.ideatech.ams.account.enums.bill;

import org.apache.commons.lang.StringUtils;

/**
 * 报备后校验状态
 *
 * @author zl
 */
public enum SyncCheckStatus {

    init("初始化"), checkPass("校验通过"), checkNotPass("校验未通过");

    SyncCheckStatus(String fullName) {
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
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public static SyncCheckStatus str2enum(String enmuStr) {
        if (StringUtils.isBlank(enmuStr)) {
            return null;
        }
        if (enmuStr.equals("checkNotPass")) {
            return SyncCheckStatus.checkNotPass;
        } else if (enmuStr.equals("checkPass")) {
            return SyncCheckStatus.checkPass;
        } else if (enmuStr.equals("init")) {
            return SyncCheckStatus.init;
        } else {
            return null;
        }
    }

}

package com.ideatech.ams.account.enums.bill;

import org.apache.commons.lang.StringUtils;

/**
 * 核准类账户所在人行审核状态
 *
 * @author zl
 */
public enum CompanyAmsCheckStatus {

    WaitCheck("待审核"), CheckPass("审核通过"), NoCheck("无需审核");

    CompanyAmsCheckStatus(String fullName) {
        this.fullName = fullName;
    }

    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public static CompanyAmsCheckStatus str2enum(String enmuStr) {
        if (StringUtils.isBlank(enmuStr)) {
            return null;
        }
        if (enmuStr.equals("WaitCheck")) {
            return CompanyAmsCheckStatus.WaitCheck;
        } else if (enmuStr.equals("CheckPass")) {
            return CompanyAmsCheckStatus.CheckPass;
        } else if (enmuStr.equals("NoCheck")) {
            return CompanyAmsCheckStatus.NoCheck;
        } else {
            return null;
        }
    }

}

package com.ideatech.ams.account.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 本地异地标识
 */
public enum OpenAccountSiteType {
    LOCAL("本地"), ALLOPATRIC("异地");

    private String value;

    OpenAccountSiteType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static OpenAccountSiteType str2enum(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (str.equals("本地") || str.equals("LOCAL")) {
            return OpenAccountSiteType.LOCAL;
        } else if (str.equals("异地") || str.equals("ALLOPATRIC")) {
            return OpenAccountSiteType.ALLOPATRIC;
        }
        return null;
    }
}

package com.ideatech.ams.account.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author RJQ
 * 客户类别
 */
public enum AccountClass {
    personal("个人"),
    publics("对公");

    private String value;

    AccountClass(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static AccountClass str2enum(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        if (type.equals("personal") || type.equals("个人")) {
            return AccountClass.personal;
        } else if (type.equals("publics") || type.equals("对公")) {
            return AccountClass.publics;
        }

        return null;
    }

}

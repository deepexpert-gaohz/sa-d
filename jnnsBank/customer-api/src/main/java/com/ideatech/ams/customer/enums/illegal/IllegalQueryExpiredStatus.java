package com.ideatech.ams.customer.enums.illegal;

import org.apache.commons.lang.StringUtils;

public enum IllegalQueryExpiredStatus {

    EXPIRED("已过期"),
    NOTEXPIRED("未过期"),
    EMPTY("未查到");


    private String value;
    IllegalQueryExpiredStatus(String value){ this.value = value;}

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static IllegalQueryExpiredStatus str2enum(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        if (type.equalsIgnoreCase("empty")) {
            return IllegalQueryExpiredStatus.EMPTY;
        } else if (type.equalsIgnoreCase("已过期")) {
            return IllegalQueryExpiredStatus.EXPIRED;
        }else if (type.equalsIgnoreCase("未过期")) {
            return IllegalQueryExpiredStatus.NOTEXPIRED;
        }

        return null;
    }
}

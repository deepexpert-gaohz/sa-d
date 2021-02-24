package com.ideatech.ams.customer.enums.illegal;

import org.apache.commons.lang.StringUtils;

public enum IllegalQueryStatus {

    EMPTY("查询无结果"),
    NORMAL("正常"),
    ILLEGAL("严重违法"),
    CHANGEMESS("经营异常");


    private String value;
    IllegalQueryStatus(String value){ this.value = value;}

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static IllegalQueryStatus str2enum(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        if (type.equalsIgnoreCase("empty") || type.equals("查询无结果")) {
            return IllegalQueryStatus.EMPTY;
        } else if (type.equalsIgnoreCase("normal") || type.equals("正常")) {
            return IllegalQueryStatus.NORMAL;
        }else if (type.equalsIgnoreCase("illegal") || type.equals("严重违法")) {
            return IllegalQueryStatus.ILLEGAL;
        }else if (type.equalsIgnoreCase("changemess") || type.equals("经营异常")) {
            return IllegalQueryStatus.CHANGEMESS;
        }

        return null;
    }
}

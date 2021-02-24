package com.ideatech.ams.customer.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author RJQ
 * 客户类别
 */
public enum CustomerType {
    PERSONAL("个人"),
    CORPORATE("对公");

    private String value;

    CustomerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static CustomerType str2enum(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        if (type.equalsIgnoreCase("personal") || type.equals("个人")) {
            return CustomerType.PERSONAL;
        } else if (type.equalsIgnoreCase("corporate") || type.equals("对公")) {
            return CustomerType.CORPORATE;
        }

        return null;
    }

}

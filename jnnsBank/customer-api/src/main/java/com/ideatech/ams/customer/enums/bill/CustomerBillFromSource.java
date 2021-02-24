package com.ideatech.ams.customer.enums.bill;

import org.apache.commons.lang.StringUtils;

/**
 * 流水审核状态
 *
 * @author jogy.he
 */
public enum CustomerBillFromSource {

    AMS("账管系统"),

    CORE("核心系统");

    CustomerBillFromSource(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static CustomerBillFromSource str2enum(String enmuStr) {
        if (StringUtils.isBlank(enmuStr)) {
            return null;
        }
        if (enmuStr.equalsIgnoreCase("ams")) {
            return CustomerBillFromSource.AMS;
        } else if (enmuStr.equalsIgnoreCase("core")) {
            return CustomerBillFromSource.CORE;
        } else {
            return null;
        }
    }

}

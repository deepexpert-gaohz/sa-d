package com.ideatech.ams.customer.enums.bill;

import org.apache.commons.lang.StringUtils;

/**
 * 流水审核状态
 *
 * @author jogy.he
 */
public enum CustomerBillStatus {

    NEW("新建"),

    APPROVING("审核中"),

    APPROVED("审核通过");

    CustomerBillStatus(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static CustomerBillStatus str2enum(String enmuStr) {
        if (StringUtils.isBlank(enmuStr)) {
            return null;
        }
        if (enmuStr.equalsIgnoreCase("new")) {
            return CustomerBillStatus.NEW;
        } else if (enmuStr.equalsIgnoreCase("approving")) {
            return CustomerBillStatus.APPROVING;
        } else if (enmuStr.equalsIgnoreCase("approved")) {
            return CustomerBillStatus.APPROVED;
        } else {
            return null;
        }
    }

}

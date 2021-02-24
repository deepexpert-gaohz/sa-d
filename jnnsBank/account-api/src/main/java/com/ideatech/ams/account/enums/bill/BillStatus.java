package com.ideatech.ams.account.enums.bill;

import org.apache.commons.lang.StringUtils;

/**
 * 流水审核状态
 *
 * @author jogy.he
 */
public enum BillStatus {

    NEW("新建"),

    APPROVING("审核中"),

    WAITING_REPORTING("等待上报"),

    APPROVED("审核通过"),

    REJECT("驳回"),

    WAITING_CORE("待核心开户"),

    WAITING_SUPPLEMENT("待补录");

    BillStatus(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static BillStatus str2enum(String enmuStr) {
        if (StringUtils.isBlank(enmuStr)) {
            return null;
        }
        if (enmuStr.equalsIgnoreCase("new")) {
            return BillStatus.NEW;
        } else if (enmuStr.equalsIgnoreCase("approving")) {
            return BillStatus.APPROVING;
        } else if (enmuStr.equalsIgnoreCase("approved")) {
            return BillStatus.APPROVED;
        } else if (enmuStr.equalsIgnoreCase("reject")) {
            return BillStatus.REJECT;
        } else if (enmuStr.equalsIgnoreCase("waiting_supplement")) {
            return BillStatus.WAITING_SUPPLEMENT;
        } else if (enmuStr.equalsIgnoreCase("waiting_core")) {
            return BillStatus.WAITING_CORE;
        } else if (enmuStr.equalsIgnoreCase("waiting_reporting")) {
            return BillStatus.WAITING_REPORTING;
        }  {
            return null;
        }
    }

    public static String getValueBy(String acctType) {
        if (StringUtils.isBlank(acctType)) {
            return null;
        }
        if (acctType.equals("New") || acctType.equals("新建")) {
            return BillStatus.NEW.value;
        } else if (acctType.equalsIgnoreCase("approving") || acctType.equals("审核中")) {
            return BillStatus.APPROVING.value;
        } else if (acctType.equalsIgnoreCase("approved") || acctType.equals("审核通过")) {
            return BillStatus.APPROVED.value;
        } else if (acctType.equalsIgnoreCase("reject") || acctType.equals("驳回")) {
            return BillStatus.REJECT.value;
        } else if (acctType.equalsIgnoreCase("waiting_core") || acctType.equals("待核心开户")) {
            return BillStatus.WAITING_CORE.value;
        } else if (acctType.equalsIgnoreCase("waiting_supplement") || acctType.equals("待补录")) {
            return BillStatus.WAITING_SUPPLEMENT.value;
        } else if (acctType.equalsIgnoreCase("waiting_reporting") || acctType.equals("等待上报")) {
            return BillStatus.WAITING_REPORTING.value;
        }
        return null;
    }

}

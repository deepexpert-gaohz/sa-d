package com.ideatech.common.enums;

import org.apache.commons.lang3.StringUtils;

public enum BillType {

    ALL("所有"),

    ACCT_INIT("存量"),

    ACCT_OPEN("新开户"),

    ACCT_CHANGE("变更"),

    ACCT_SUSPEND("久悬"),

    ACCT_EXTENSION("展期"),

    ACCT_SEARCH("查询"),

    ACCT_REVOKE("销户"),

    ACCT_CLOSESUSPEND("撤销久悬"),

    ACCT_SAIC("工商信息"),

    APPT_UNCOMPLETE("接洽打印"),

    APPT_AFTERCOMPLETE("已受理详情");

    private String value;

    BillType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static BillType str2enum(String operateType) {
        if (StringUtils.isBlank(operateType)) {
            return null;
        }
        if (operateType.equals("ACCT_OPEN") || operateType.equals("新开户")) {
            return BillType.ACCT_OPEN;
        } else if (operateType.equals("ACCT_CHANGE") || operateType.equals("变更")) {
            return BillType.ACCT_CHANGE;
        } else if (operateType.equals("ACCT_REVOKE") || operateType.equals("销户")) {
            return BillType.ACCT_REVOKE;
        } else if (operateType.equals("ACCT_SUSPEND") || operateType.equals("久悬")) {
            return BillType.ACCT_SUSPEND;
        } else if (operateType.equals("ACCT_SEARCH") || operateType.equals("查询")) {
            return BillType.ACCT_SEARCH;
        } else if (operateType.equals("ACCT_SAIC") || operateType.equals("工商信息")) {
            return BillType.ACCT_SAIC;
        } else if (operateType.equals("APPT_UNCOMPLETE") || operateType.equals("接洽打印")) {
            return BillType.APPT_UNCOMPLETE;
        } else if (operateType.equals("APPT_AFTERCOMPLETE") || operateType.equals("已受理详情")) {
            return BillType.APPT_AFTERCOMPLETE;
        }else if (operateType.equals("ACCT_EXTENSION") || operateType.equals("展期")) {
            return BillType.ACCT_EXTENSION;
        }else if (operateType.equals("ACCT_CLOSESUSPEND") || operateType.equals("撤销久悬")) {
            return BillType.ACCT_CLOSESUSPEND;
        }
        return null;
    }

    public static String getValueBy(String acctType) {
        if (StringUtils.isBlank(acctType)) {
            return null;
        }
        if (acctType.equals("ACCT_INIT") || acctType.equals("存量")) {
            return BillType.ACCT_INIT.value;
        } else if (acctType.equals("ACCT_OPEN") || acctType.equals("新开户")) {
            return BillType.ACCT_OPEN.value;
        } else if (acctType.equals("ACCT_CHANGE") || acctType.equals("变更")) {
            return BillType.ACCT_CHANGE.value;
        } else if (acctType.equals("ACCT_SUSPEND") || acctType.equals("久悬")) {
            return BillType.ACCT_SUSPEND.value;
        } else if (acctType.equals("ACCT_SEARCH") || acctType.equals("查询")) {
            return BillType.ACCT_SEARCH.value;
        } else if (acctType.equals("ACCT_REVOKE") || acctType.equals("销户")) {
            return BillType.ACCT_REVOKE.value;
        } else if (acctType.equals("ACCT_SAIC") || acctType.equals("工商信息")) {
            return BillType.ACCT_SAIC.value;
        } else if (acctType.equals("APPT_UNCOMPLETE") || acctType.equals("接洽打印")) {
            return BillType.APPT_UNCOMPLETE.value;
        } else if (acctType.equals("APPT_AFTERCOMPLETE") || acctType.equals("已受理详情")) {
            return BillType.APPT_AFTERCOMPLETE.value;
        }else if (acctType.equals("ACCT_EXTENSION") || acctType.equals("展期")) {
            return BillType.ACCT_EXTENSION.value;
        }else if (acctType.equals("ACCT_CLOSESUSPEND") || acctType.equals("撤销久悬")) {
            return BillType.ACCT_CLOSESUSPEND.value;
        }
        return null;
    }

}

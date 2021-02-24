package com.ideatech.ams.account.enums.bill;

import org.apache.commons.lang.StringUtils;

/**
 * @author jogy.he
 * @ClassName: BillTypeNo
 * @Description: 单据类型编号
 * @date
 */
public enum BillTypeNo {

    ACCT_INIT("00"),//存量

    ACCT_OPEN("01"),//新开户

    ACCT_CHANGE("02"),//变更

    ACCT_SUSPEND("03"),//久悬

    ACCT_REVOKE("04"),//销户

    ACCT_YANZI2NORMAL("05"),//验资户转正常

    ACCT_ZENGZI2NORMAL("06"),

    ACCT_EXTENSION("07"),//非临时展期

    ACCT_CLOSESUSPEND("08");//撤销久悬


    private String value;

    BillTypeNo(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.name());
    }

    public static BillTypeNo str2enum(String operateType) {
        if (StringUtils.isBlank(operateType)) {
            return null;
        }
        if (operateType.equals("ACCT_INIT")) {
            return BillTypeNo.ACCT_INIT;
        } else if (operateType.equals("ACCT_OPEN")) {
            return BillTypeNo.ACCT_OPEN;
        } else if (operateType.equals("ACCT_CHANGE")) {
            return BillTypeNo.ACCT_CHANGE;
        } else if (operateType.equals("ACCT_REVOKE")) {
            return BillTypeNo.ACCT_REVOKE;
        } else if (operateType.equals("ACCT_SUSPEND")) {
            return BillTypeNo.ACCT_SUSPEND;
        }else if (operateType.equals("ACCT_CLOSESUSPEND")) {
            return BillTypeNo.ACCT_CLOSESUSPEND;
        }
        return null;
    }

    public static String getFullName(String operateType) {
        if (StringUtils.isBlank(operateType)) {
            return null;
        }
        if (operateType.equals("ACCT_INIT") || operateType.equals("00")) {
            return "存量";
        } else if (operateType.equals("ACCT_OPEN") || operateType.equals("01")) {
            return "新开户";
        } else if (operateType.equals("ACCT_CHANGE") || operateType.equals("02")) {
            return "变更";
        } else if (operateType.equals("ACCT_REVOKE") || operateType.equals("04")) {
            return "销户";
        } else if (operateType.equals("ACCT_SUSPEND") || operateType.equals("03")) {
            return "久悬";
        }else if (operateType.equals("ACCT_CLOSESUSPEND") || operateType.equals("08")) {
            return "撤销久悬";
        }
        return null;
    }

}

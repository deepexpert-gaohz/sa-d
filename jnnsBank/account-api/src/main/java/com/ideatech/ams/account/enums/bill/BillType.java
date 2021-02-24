/**
 * @ProjectName idea-ams-core
 * @FileName FormStatus.java
 * @PackageName:com.idea.ams.domain.enums
 * @author ku
 * @date 2015年7月30日下午3:47:00
 * @Copyright (c) 2015,kuyonggang@ideatech.info All Rights Reserved.
 * @since 1.0.0
 */
package com.ideatech.ams.account.enums.bill;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @ClassName: CompanyAcctType
 * @Description: 对公报备业务类型枚举
 * @author zoulang
 * @date 2015年11月13日 上午9:55:09
 *
 */
public enum BillType {

    ACCT_INIT("存量"),

    ACCT_OPEN("新开户"),

    ACCT_CHANGE("变更"),

    ACCT_SUSPEND("久悬"),

    ACCT_SEARCH("查询"),

    ACCT_EXTENSION("展期"),

    ACCT_REVOKE("销户"),

    ACCT_CLOSESUSPEND("撤销久悬"),

    ACCT_YANZI2NORMAL("验资户转正常"),

    ACCT_ZENGZI2NORMAL("增资户转正常");

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
        } else if (acctType.equals("ACCT_EXTENSION") || acctType.equals("展期")) {
            return BillType.ACCT_EXTENSION.value;
        }else if (acctType.equals("ACCT_CLOSESUSPEND") || acctType.equals("撤销久悬")) {
            return BillType.ACCT_CLOSESUSPEND.value;
        }
        return null;
    }

}

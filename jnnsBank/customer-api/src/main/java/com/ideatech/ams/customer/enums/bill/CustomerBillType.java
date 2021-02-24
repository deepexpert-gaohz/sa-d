/**
 * @ProjectName idea-ams-core
 * @FileName FormStatus.java
 * @PackageName:com.idea.ams.domain.enums
 * @author ku
 * @date 2015年7月30日下午3:47:00
 * @Copyright (c) 2015,kuyonggang@ideatech.info All Rights Reserved.
 * @since 1.0.0
 */
package com.ideatech.ams.customer.enums.bill;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @ClassName: CompanyAcctType
 * @Description: 对公报备业务类型枚举
 * @author zoulang
 * @date 2015年11月13日 上午9:55:09
 *
 */
public enum CustomerBillType {

    INIT("存量"),

    OPEN("新开户"),

    CHANGE("变更"),

    REVOKE("销户");

    private String value;

    CustomerBillType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static CustomerBillType str2enum(String operateType) {
        if (StringUtils.isBlank(operateType)) {
            return null;
        }
        if (operateType.equalsIgnoreCase("ACCT_INIT") || operateType.equals("存量")) {
            return CustomerBillType.INIT;
        } else if (operateType.equalsIgnoreCase("ACCT_OPEN") || operateType.equals("新开户")) {
            return CustomerBillType.OPEN;
        } else if (operateType.equalsIgnoreCase("ACCT_CHANGE") || operateType.equals("变更")) {
            return CustomerBillType.CHANGE;
        } else if (operateType.equalsIgnoreCase("ACCT_REVOKE") || operateType.equals("销户")) {
            return CustomerBillType.REVOKE;
        }
        return null;
    }

}

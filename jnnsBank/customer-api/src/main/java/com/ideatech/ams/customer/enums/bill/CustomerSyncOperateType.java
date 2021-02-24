package com.ideatech.ams.customer.enums.bill;

import org.apache.commons.lang.StringUtils;

/**
 * 对公报备 账户报备方式
 *
 * @author zl
 */
public enum CustomerSyncOperateType {

    personSyncType("手工上报"),

    autoSyncType("自动上报"),

    halfSyncType("手工补录 "),
    /**
     * 手工确认系统默认只改状态）
     */
    sureVirtualSyncType("手工虚拟上报"),
    /**
     * 线下手工报备 系统自动确认 自动到人行账管系统中查询账户是否柜员已手工报备，若手工已报备则默认改成报备成功
     */
    offLineSyncType("线下手工报备");

    CustomerSyncOperateType(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static CustomerSyncOperateType str2enum(String enmuStr) {
        if (StringUtils.isBlank(enmuStr)) {
            return null;
        }
        if (enmuStr.equals("personSyncType")) {
            return CustomerSyncOperateType.personSyncType;
        } else if (enmuStr.equals("autoSyncType")) {
            return CustomerSyncOperateType.autoSyncType;
        } else if (enmuStr.equals("halfSyncType")) {
            return CustomerSyncOperateType.halfSyncType;
        } else if (enmuStr.equals("sureVirtualSyncType")) {
            return CustomerSyncOperateType.sureVirtualSyncType;
        } else if (enmuStr.equals("offLineSyncType")) {
            return CustomerSyncOperateType.offLineSyncType;
        }
        return null;
    }

}

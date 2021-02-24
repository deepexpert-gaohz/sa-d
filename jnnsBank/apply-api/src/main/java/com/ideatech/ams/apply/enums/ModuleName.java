package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/21.
 */
public enum ModuleName {

    MODULE_NAME_01 ("KYC", "客户尽调模块"),
    MODULE_NAME_02 ("PRE_OPEN_ACCOUNT", "预约开户模块");

    static Map<String, String> enumMap=new HashMap<String, String>();

    static {
        for (ModuleName type : ModuleName.values()) {
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;

    private String displayName;

    ModuleName(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return this.value;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}

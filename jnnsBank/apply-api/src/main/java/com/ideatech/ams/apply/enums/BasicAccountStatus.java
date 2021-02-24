package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum BasicAccountStatus {

    BASIC_ACCOUNT_STATUS_01 ("1", "正常"),
    BASIC_ACCOUNT_STATUS_02 ("2", "久悬"),
    BASIC_ACCOUNT_STATUS_03 ("3", "注销"),
    BASIC_ACCOUNT_STATUS_09 ("9", "其他");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(BasicAccountStatus type:BasicAccountStatus.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;

    private String displayName;

    BasicAccountStatus(String value, String displayName) {
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

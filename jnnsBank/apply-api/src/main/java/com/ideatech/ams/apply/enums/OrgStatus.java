package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */

public enum OrgStatus {

    ORG_STATUS_01 ("1", "正常"),
    ORG_STATUS_02 ("2", "注销"),
    ORG_STATUS_03 ("9", "其他");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(OrgStatus type:OrgStatus.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;

    private String displayName;

    OrgStatus(String value, String displayName) {
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

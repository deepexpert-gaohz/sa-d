package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum OrgType {

    ORG_TYPE_01 ("1", "企业"),
    ORG_TYPE_02 ("2", "事业单位"),
    ORG_TYPE_03 ("3", "机关"),
    ORG_TYPE_04 ("4", "社会团体"),
    ORG_TYPE_07 ("7", "个体工商户"),
    ORG_TYPE_09 ("9", "其他");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(OrgType type:OrgType.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;

    private String displayName;

    OrgType(String value, String displayName) {
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

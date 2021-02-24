package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum LegalType {

    LEGAL_TYPE_01 ("1", "法定代表人"),
    LEGAL_TYPE_02 ("2", "单位负责人");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(LegalType type:LegalType.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;

    private String displayName;

    LegalType(String value, String displayName) {
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

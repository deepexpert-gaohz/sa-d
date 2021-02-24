package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum IsSameRegistArea {

    IS_SAME_REGIST_AREA_01 ("1", "是"),
    IS_SAME_REGIST_AREA_02 ("0", "否");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(IsSameRegistArea type:IsSameRegistArea.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;

    private String displayName;

    IsSameRegistArea(String value, String displayName) {
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

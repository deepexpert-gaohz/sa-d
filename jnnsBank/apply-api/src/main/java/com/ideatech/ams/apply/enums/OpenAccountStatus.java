package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/13.
 */
public enum OpenAccountStatus {

    PROCESSING("PROCESSING", "开户中"),
    SUCCESS("SUCCESS", "开户成功"),
    FAILED("FAILED", "开户失败");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(OpenAccountStatus type:OpenAccountStatus.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {

        return enumMap.get(value);
    }

    OpenAccountStatus(String value, String displayName) {

        this.value = value;
        this.displayName = displayName;
    }

    private String value;

    private String displayName;

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return this.displayName;
    }


}

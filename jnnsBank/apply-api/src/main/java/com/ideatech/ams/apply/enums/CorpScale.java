package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum CorpScale {

    CORP_SCALE_02 ("2", "大型企业"),
    CORP_SCALE_03 ("3", "中型企业"),
    CORP_SCALE_04 ("4", "小型企业"),
    CORP_SCALE_05 ("5", "微型企业"),
    CORP_SCALE_09 ("9", "其他");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(CorpScale type:CorpScale.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }


    private String value;

    private String displayName;

    CorpScale(String value, String displayName) {
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

package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum FileType {

    FILE_TYPE_01 ("01", "工商营业执照"),
    FILE_TYPE_02 ("02", "批文"),
    FILE_TYPE_03 ("03", "登记证书"),
    FILE_TYPE_04 ("04", "开户证明");


    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(FileType type:FileType.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;

    private String displayName;

    FileType(String value, String displayName) {
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

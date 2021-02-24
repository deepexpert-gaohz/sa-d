package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum RegOffice {

    REG_OFFICE_01 ("G", "工商部门"),
    REG_OFFICE_02 ("R", "人民银行"),
    REG_OFFICE_03 ("M", "民政部门"),
    REG_OFFICE_04 ("B", "机构编制部门"),
    REG_OFFICE_05 ("S", "司法行政部门"),
    REG_OFFICE_06 ("W", "外交部门"),
    REG_OFFICE_07 ("Z", "宗教部门"),
    REG_OFFICE_08 ("Q", "其他");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(RegOffice type:RegOffice.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;
    private String displayName;

    RegOffice(String value, String displayName) {
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

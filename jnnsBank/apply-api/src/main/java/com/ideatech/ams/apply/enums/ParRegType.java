package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum ParRegType {

    PAR_REG_TYPE_01 ("01", "工商注册号"),
    PAR_REG_TYPE_02 ("02", "机关和事业单位登记号"),
    PAR_REG_TYPE_03 ("03", "社会团体登记号"),
    PAR_REG_TYPE_04 ("04", "民办非企业单位登记号"),
    PAR_REG_TYPE_05 ("05", "基金会登记号"),
    PAR_REG_TYPE_06 ("06", "宗教活动场所登记号"),
    PAR_REG_TYPE_07 ("07", "统一社会信用代码"),
    PAR_REG_TYPE_08 ("08", "商事与非商事登记证号"),
    PAR_REG_TYPE_99 ("99", "其他");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(ParRegType type:ParRegType.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;

    private String displayName;

    ParRegType(String value, String displayName) {
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

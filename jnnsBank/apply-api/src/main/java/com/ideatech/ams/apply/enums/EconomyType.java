package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum EconomyType {

    ECONOMY_TYPE_10 ("10", "内资"),
    ECONOMY_TYPE_11 ("11", "国有全资"),
    ECONOMY_TYPE_12 ("12", "集体全资"),
    ECONOMY_TYPE_13 ("13", "股份合作"),
    ECONOMY_TYPE_14 ("14", "联营"),
    ECONOMY_TYPE_15 ("15", "有限责任公司"),
    ECONOMY_TYPE_16 ("16", "股份有限公司"),
    ECONOMY_TYPE_17 ("17", "私有"),
    ECONOMY_TYPE_19 ("19", "其它内资"),
    ECONOMY_TYPE_20 ("20", "港澳台投资"),
    ECONOMY_TYPE_21 ("21", "内地和港澳台投资"),
    ECONOMY_TYPE_22 ("22", "内地和港澳台合作"),
    ECONOMY_TYPE_23 ("23", "港澳台独资"),
    ECONOMY_TYPE_24 ("24", "港澳台投资股份有限公司"),
    ECONOMY_TYPE_29 ("29", "其他港澳台投资"),
    ECONOMY_TYPE_30 ("30", "国外投资"),
    ECONOMY_TYPE_31 ("31", "中外合资"),
    ECONOMY_TYPE_33 ("33", "外资"),
    ECONOMY_TYPE_32 ("32", "中外合作"),
    ECONOMY_TYPE_34 ("34", "国外投资股份有限公司"),
    ECONOMY_TYPE_39 ("39", "其他国外投资"),
    ECONOMY_TYPE_90 ("90", "其他");


    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(EconomyType type:EconomyType.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;

    private String displayName;

    EconomyType (String value, String displayName) {
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

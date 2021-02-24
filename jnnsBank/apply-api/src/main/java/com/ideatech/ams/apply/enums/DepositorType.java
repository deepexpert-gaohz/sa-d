package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/18.
 */
public enum DepositorType {

    DEPOSITOR_TYPE_01("01", "企业法人"),
    DEPOSITOR_TYPE_02("02", "非法人企业"),
    DEPOSITOR_TYPE_03("03", "机关"),
    DEPOSITOR_TYPE_04("04", "实行预算管理的事业单位"),
    DEPOSITOR_TYPE_05("05", "非预算管理的事业单位"),
    DEPOSITOR_TYPE_06("06", "团级(含)以上军队及分散执勤的支(分)队"),
    DEPOSITOR_TYPE_07("07", "团级(含)以上武警部队及分散执勤的支(分)队"),
    DEPOSITOR_TYPE_08("08", "社会团体"),
    DEPOSITOR_TYPE_09("09", "宗教组织"),
    DEPOSITOR_TYPE_10("10", "民办非企业组织"),
    DEPOSITOR_TYPE_11("11", "外地常设机构"),
    DEPOSITOR_TYPE_12("12", "外国驻华机构"),
    DEPOSITOR_TYPE_13("13", "有字号的个体工商户"),
    DEPOSITOR_TYPE_14("14", "无字号的个体工商户"),
    DEPOSITOR_TYPE_15("15", "居民委员会、村民委员会、社区委员会"),
    DEPOSITOR_TYPE_16("16", "单位设立的独立核算的附属机构"),
    DEPOSITOR_TYPE_17("17", "其他组织"),
    DEPOSITOR_TYPE_18("20", "境外机构");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(DepositorType type:DepositorType.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;
    private String displayName;

    DepositorType(String value, String displayName) {
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

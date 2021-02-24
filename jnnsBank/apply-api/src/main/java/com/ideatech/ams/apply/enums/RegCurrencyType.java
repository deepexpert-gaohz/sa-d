package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum RegCurrencyType {

    REG_CURRENCY_TYPE_01 ("CNY", "人民币"),
    REG_CURRENCY_TYPE_02 ("USD", "美元"),
    REG_CURRENCY_TYPE_03 ("HKD", "港元"),
    REG_CURRENCY_TYPE_04 ("EUR", "欧元"),
    REG_CURRENCY_TYPE_05 ("KRW", "韩元"),
    REG_CURRENCY_TYPE_06 ("JPY", "日元"),
    REG_CURRENCY_TYPE_07 ("GBP", "英镑"),
    REG_CURRENCY_TYPE_08 ("SGD", "新加坡元"),
    REG_CURRENCY_TYPE_09 ("AUD", "澳大利亚元"),
    REG_CURRENCY_TYPE_10 ("CAD", "加拿大元"),
    REG_CURRENCY_TYPE_11 ("XEU", "其它货币折美元");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(RegCurrencyType type:RegCurrencyType.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }


    private String value;

    private String displayName;

    RegCurrencyType(String value, String displayName) {
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

package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum ParLegalIdCardType {

    PAR_LEGAL_ID_CARD_TYPE_01 ("1", "身份证"),
    PAR_LEGAL_ID_CARD_TYPE_02 ("2", "军官证"),
    PAR_LEGAL_ID_CARD_TYPE_03 ("3", "文职干部证"),
    PAR_LEGAL_ID_CARD_TYPE_04 ("4", "警官证"),
    PAR_LEGAL_ID_CARD_TYPE_05 ("5", "士兵证"),
    PAR_LEGAL_ID_CARD_TYPE_06 ("6", "护照"),
    PAR_LEGAL_ID_CARD_TYPE_07 ("7", "港、澳、台居民通行证"),
    PAR_LEGAL_ID_CARD_TYPE_09 ("9", "其它合法身份证件"),
    PAR_LEGAL_ID_CARD_TYPE_08 ("8", "户口簿");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(ParLegalIdCardType type:ParLegalIdCardType.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;

    private String displayName;

    ParLegalIdCardType(String value, String displayName) {
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

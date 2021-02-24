package com.ideatech.ams.risk.enums;

import java.util.HashMap;
import java.util.Map;

public enum OverFieldsEnums {

    OVER_FIELDS_01("01", "yd_id"),
    OVER_FIELDS_02("02", "yd_corporate_bank"),
    OVER_FIELDS_03("03", "yd_corporate_full_id"),
    OVER_FIELDS_04("04", "yd_org_id"),
    OVER_FIELDS_05("05", "yd_risk_amt"),
    OVER_FIELDS_06("06", "yd_risk_id"),
    OVER_FIELDS_07("07", "yd_kh_name"),
    OVER_FIELDS_08("08", "yd_risk_desc"),
    OVER_FIELDS_09("09", "yd_org_name"),
    OVER_FIELDS_10("10", "yd_risk_id");


    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(OverFieldsEnums type: OverFieldsEnums.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;
    private String displayName;

    OverFieldsEnums(String value, String displayName) {
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

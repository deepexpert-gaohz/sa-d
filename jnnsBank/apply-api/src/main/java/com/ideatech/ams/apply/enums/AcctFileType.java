package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum AcctFileType {

    ACCT_FILE_TYPE_07 ("07", "其他结算需要的证明"),
    ACCT_FILE_TYPE_06 ("06", "借款合同");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(AcctFileType type:AcctFileType.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;

    private String displayName;

    AcctFileType(String value, String displayName) {
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

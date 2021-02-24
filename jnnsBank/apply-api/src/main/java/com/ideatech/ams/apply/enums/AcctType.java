package com.ideatech.ams.apply.enums;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hammer on 2018/3/20.
 */
public enum AcctType {

    ACCT_TYPE_01 ("jiben", "基本存款账户"),
    ACCT_TYPE_02 ("yiban", "一般存款账户"),
    ACCT_TYPE_03 ("yusuan", "预算单位专用存款账户"),
    ACCT_TYPE_04 ("feiyusuan", "非预算单位专用存款账户"),
    ACCT_TYPE_05 ("teshu", "特殊单位专用存款账户"),
    ACCT_TYPE_06 ("linshi", "临时机构临时存款账户"),
    ACCT_TYPE_07 ("feilinshi", "非临时机构临时存款账户");

    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(AcctType type:AcctType.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }


    private String value;

    private String displayName;

    AcctType(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return this.value;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static AcctType str2enum(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        if (type.equals("jiben") || type.equals("基本存款账户")) {
            return AcctType.ACCT_TYPE_01;
        } else if (type.equals("yiban") || type.equals("一般存款账户")) {
            return AcctType.ACCT_TYPE_02;
        } else if (type.equals("yusuan") || type.equals("预算单位专用存款账户")) {
            return AcctType.ACCT_TYPE_03;
        } else if (type.equals("feiyusuan") || type.equals("非预算单位专用存款账户")) {
            return AcctType.ACCT_TYPE_04;
        } else if (type.equals("teshu") || type.equals("特殊单位专用存款账户")) {
            return AcctType.ACCT_TYPE_05;
        } else if (type.equals("linshi") || type.equals("临时机构临时存款账户")) {
            return AcctType.ACCT_TYPE_06;
        } else if (type.equals("feilinshi") || type.equals("非临时机构临时存款账户")) {
            return AcctType.ACCT_TYPE_07;
        }
        return null;
    }
}

package com.ideatech.ams.kyc.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author wangqingan
 * @version 09/02/2018 1:40 PM
 */
public enum EmployeeType {
    DongShi("1","董事"),
    JianShi("2","监事"),
    GaoGuan("3","经理");

    private String type;
    private String value;

    EmployeeType(String type, String value ){
        this.type = type;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static EmployeeType str2enum(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        if (value.equals("董事") || value.equals("DongShi") || value.equals("1")) {
            return EmployeeType.DongShi;
        } else if (value.equals("监事") || value.equals("JianShi") || value.equals("2")) {
            return EmployeeType.JianShi;
        } else if (value.equals("经理") || value.equals("GaoGuan") || value.equals("3")){
            return EmployeeType.GaoGuan;
        }

        return null;
    }
}

package com.ideatech.ams.customer.enums;

import org.apache.commons.lang.StringUtils;

public enum SaicMonitorEnum {

    INIT("初始化"),
    ANNUAL("年检调用"),
    SAIC("工商调用"),
    ILLEGAL("批量违法"),
    KYC("客户尽调");

    private String value;
    SaicMonitorEnum(String value){
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public static SaicMonitorEnum str2enum(String type){
        if (StringUtils.isBlank(type)) {
            return null;
        }
        if("INIT".equals(type)){
            return SaicMonitorEnum.INIT;
        }
        if("ANUAL".equals(type)){
            return SaicMonitorEnum.ANNUAL;
        }
        if("SAIC".equals(type)){
            return SaicMonitorEnum.SAIC;
        }
        if("KYC".equals(type)){
            return SaicMonitorEnum.KYC;
        }
        return null;
    }
}

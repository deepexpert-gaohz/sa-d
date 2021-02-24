package com.ideatech.ams.system.blacklist.enums;

import org.apache.commons.lang.StringUtils;

public enum EBlackListEntrySource {
    ALL("所有"),
    AMS("账管系统维护"),
    AMLRS("反洗钱系统"),
    TF("电信诈骗"),
    SAIC("工商经营异常");

    EBlackListEntrySource(String name) {
        this.name = name;
    }

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static EBlackListEntrySource getValue(String name) {
        EBlackListEntrySource[] values = EBlackListEntrySource.values();
        for (EBlackListEntrySource value : values) {
            if (StringUtils.equals(value.getName(), name)) {
                return value;
            }
        }
        return null;
    }
}

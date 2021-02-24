package com.ideatech.ams.system.whitelist.enums;

import org.apache.commons.lang.StringUtils;

public enum WhiteListEntrySource {
    ALL("所有"),
    AMS("账管系统维护"),
    CORE("核心同步"),
    IMPORT("批量导入"),
    BLACK("黑名单标注"),
    OTHER("其他");

    WhiteListEntrySource(String name) {
        this.name = name;
    }

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static WhiteListEntrySource getValue(String name) {
        WhiteListEntrySource[] values = WhiteListEntrySource.values();
        for (WhiteListEntrySource value : values) {
            if (StringUtils.equals(value.getName(), name)) {
                return value;
            }
        }
        return null;
    }
}

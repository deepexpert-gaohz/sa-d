package com.ideatech.ams.annual.enums;

import org.apache.commons.lang.StringUtils;

public enum DataSourceEnum {

    PBC("人行"),

    SAIC("工商"),

    CORE("核心");

    private String value;

    DataSourceEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static DataSourceEnum str2enum(String dataSource) {
        if (StringUtils.isBlank(dataSource)) {
            return null;
        }
        if (dataSource.equalsIgnoreCase("PBC") || dataSource.equals("人行")) {
            return DataSourceEnum.PBC;
        } else if (dataSource.equalsIgnoreCase("SAIC") || dataSource.equals("工商")) {
            return DataSourceEnum.SAIC;
        } else if (dataSource.equalsIgnoreCase("CORE") || dataSource.equals("核心")) {
            return DataSourceEnum.CORE;
        }

        return null;
    }
}

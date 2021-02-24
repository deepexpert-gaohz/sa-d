package com.ideatech.ams.compare.enums;

import lombok.Getter;

@Getter
public enum TaskRate {
    none(""),
    day("一次"),
    week("每周"),
    month("每月"),
    quarter("每季度");

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    TaskRate(String value) {
        this.value = value;
    }
}

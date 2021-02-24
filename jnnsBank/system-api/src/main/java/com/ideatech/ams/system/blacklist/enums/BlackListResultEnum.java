package com.ideatech.ams.system.blacklist.enums;

public enum BlackListResultEnum {
    BLACK("黑名单"),
    NORMAL("正常"),
    WHITE("白名单");

    private String value;

    BlackListResultEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

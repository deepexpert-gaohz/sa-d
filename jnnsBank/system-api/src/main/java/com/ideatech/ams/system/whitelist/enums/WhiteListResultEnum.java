package com.ideatech.ams.system.whitelist.enums;

public enum WhiteListResultEnum {
    BLACK("黑名单"),
    NORMAL("正常"),
    WHITE("白名单");

    private String value;

    WhiteListResultEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

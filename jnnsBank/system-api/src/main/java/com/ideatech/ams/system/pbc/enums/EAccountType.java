package com.ideatech.ams.system.pbc.enums;

public enum EAccountType {
    AMS("人行账管系统"),
    PICP("身份联网核查系统"),
    ECCS("机构信用代码系统"),
    IMS("人行影像系统");

    private String text;

    EAccountType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

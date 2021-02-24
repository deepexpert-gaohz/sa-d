package com.ideatech.ams.system.pbc.enums;

/**
 * @author liangding
 * @create 2018-05-17 上午12:21
 **/
public enum  EAccountStatus {
    NEW("未校验"),
    VALID("有效"),
    INVALID("无效");
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    EAccountStatus(String name) {

        this.name = name;
    }
}

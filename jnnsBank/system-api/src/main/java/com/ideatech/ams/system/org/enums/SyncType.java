package com.ideatech.ams.system.org.enums;

/**
 * 同步类型
 */
public enum SyncType {
    INSERT("新增"),
    UPDATE("更新"),
    DELETE("删除");
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    SyncType(String fullName) {
        this.fullName = fullName;
    }
}

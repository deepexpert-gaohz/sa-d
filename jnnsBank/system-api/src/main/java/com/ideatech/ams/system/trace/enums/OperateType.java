package com.ideatech.ams.system.trace.enums;

import lombok.Data;

/**
 * 操作类型
 * @author jzh
 * @date 2019-10-30.
 */


public enum OperateType {

    INSERT("新建"),
    UPDATE("修改"),
    DELETE("删除"),
    SELECT("搜索"),
    IMPORT("导入"),
    EXPORT("导出"),
    DISABLE("禁用"),
    ENABLE("启用"),
    START("启动"),
    PAUSE("暂停"),
    END("结束"),
    OTHER("其他");

    private String name;

    public String getName(){
        return this.name;
    }

    OperateType(String name){
        this.name = name;
    }
}

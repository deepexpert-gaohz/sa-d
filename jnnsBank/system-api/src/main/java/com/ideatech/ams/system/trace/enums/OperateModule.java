package com.ideatech.ams.system.trace.enums;

/**
 * @author jzh
 * @date 2019-10-30.
 */
public enum OperateModule {

    ANNUAL("年检管理"),
    USER("用户管理"),
    ROLE("角色管理"),
    PERMISSION("菜单管理"),
    ORGANIZATION("组织机构管理"),
    ANNOUNCEMENT("公告管理"),
    ADMINLOG("日志管理"),
    DICTIONARY("数据字典管理"),
    CONFIG("系统配置管理"),
    NOTICE("证件到期提醒");

    private String name;

    public String getName(){
        return this.name;
    }
    OperateModule(String name){
        this.name = name;
    }
}

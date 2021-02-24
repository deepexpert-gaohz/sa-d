package com.ideatech.ams.system.user.poi;

import lombok.Data;

/**
 * Created by jzh on 2019/1/8.
 */
@Data
public class SysUserPoi {

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String cname;

    /**
     * 角色
     */
    private String roleName;

    /**
     * 所属机构
     */
    private String orgName;
}

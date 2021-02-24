package com.ideatech.ams.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liangding
 * @create 2018-08-31 下午4:34
 **/
@Data
public class LoginInfoVo implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 姓名
     */
    private String cname;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 机构ID
     */
    private Long orgId;
    /**
     * 机构编号
     */
    private String bankCode;
    /**
     * 机构名称
     */
    private String orgName;
}

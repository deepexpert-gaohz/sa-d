package com.ideatech.ams.pbc.dto;

import lombok.Data;

/**
 * 同步系统登录用户对象
 *
 * @author zoulang
 */
@Data
public class PbcUserAccount {

    /**
     * 用户名
     */
    private String loginUserName;

    /**
     * 用户密码
     */
    private String loginPassWord;

    /**
     * 系统Ip
     */
    private String loginIp;
}

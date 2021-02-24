package com.ideatech.ams.pbc.dto.auth;


import lombok.Data;

/**
 * 人行账管系统
 * 邹郎
 *
 * @version 1.0.0
 */
@SuppressWarnings("serial")
@Data
public class AmsAuth extends LoginAuth {

    public Integer level; // 网点级别

    public AmsAuth(String ip, String loginName, String loginPwd) {
        super(ip, loginName, loginPwd);
    }


}

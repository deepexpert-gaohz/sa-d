package com.ideatech.ams.pbc.dto.auth;


import lombok.Data;

/**
 * 机构信用代码证系统 邹郎 Mar 12, 2015 10:56:24 AM
 * 
 * @version 1.0.0
 */
@Data
public class EccsAuth extends LoginAuth {
    
    private static final long serialVersionUID = 1L;
    
    public EccsAuth (String ip,String loginName, String loginPwd) {
        super(ip,loginName,loginPwd);
    }
}

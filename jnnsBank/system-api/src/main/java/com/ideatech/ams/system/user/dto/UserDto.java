package com.ideatech.ams.system.user.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author liangding
 * @create 2018-05-04 上午10:30
 **/
@Data
public class UserDto {
    private Long id;
    private String username;
    private String cname;
    private String mobile;
    private String password;
    private Long roleId;
    private String roleName;
    private Long orgId;
    private String orgName;
    private Boolean enabled;
    private Boolean isExpire;
    private Date pwdUpdateDate;
    //密码有效期天数
    private Integer pwdExpireDay;
    /**
     * 海峡银行推送统一认证平台标志位
     */
    private String userStatus;

    /**
     * 锁定状态
     */
    private String lockedStatus;

    /**
     * 锁定时间
     */
    private String lockedTime;

    /**
     * 错误次数
     */
    private Integer pwFailCount;

}

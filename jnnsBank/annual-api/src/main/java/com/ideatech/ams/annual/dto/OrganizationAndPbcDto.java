package com.ideatech.ams.annual.dto;

import com.ideatech.ams.system.pbc.enums.EAccountStatus;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author liangding
 * @create 2018-05-02 上午11:11
 **/
@Data
public class OrganizationAndPbcDto extends BaseMaintainableDto {
    private Long id;

    private String fullId;

    private String name;

    private String code;

    private String pbcCode;

    private String institutionCode;

    private String mobile;

    private Long parentId;


    /**
     * 人行系统ip地址
     *
     * @since 1.0.0
     */
    private String pbcIp;

    /**
     * 人行用户名
     *
     * @since 1.0.0
     */
    private String pbcUsername;

    /**
     * 人行密码
     *
     * @since 1.0.0
     */
    private String pbcPassword;

    /**
     * 人行账管用户名登录状态
     */
    @Enumerated(EnumType.STRING)
    private EAccountStatus pbcLoginStatus;

    private boolean pbcEnabled;

    /**
     * 机构信用代码ip地址
     *
     * @since 1.0.0
     */
    private String eccsIp;

    /**
     * 机构信用代码证用户名
     *
     * @since 1.0.0
     */
    private String eccsUsername;

    /**
     * 机构信用代码证密码
     *
     * @since 1.0.0
     */
    private String eccsPassword;

    /**
     * 机构信用代码证登录状态
     */
    @Enumerated(EnumType.STRING)
    private EAccountStatus eccsLoginStatus;

    private boolean eccsEnabled;


}

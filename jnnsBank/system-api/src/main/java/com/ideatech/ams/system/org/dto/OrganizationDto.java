package com.ideatech.ams.system.org.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * @author liangding
 * @create 2018-05-02 上午11:11
 **/
@Data
public class OrganizationDto extends BaseMaintainableDto {
    private Long id;

    private String fullId;

    private String name;

    private String code;

    private String pbcCode;

    private String institutionCode;

    private String mobile;
    /**
     * 网点电话
     */
    private String telephone;

    private Long parentId;

    /**
     * 机构简称
     */
    private String shortName;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 网点地址
     */
    private String address;

    /**
     * 组织机构类型 BANK(银行类) 或者AGENCY(中介机构类)
     */
    private String orgType;

    /**
     * 网点是否开放 1为开放 0为不开放
     */
    private String isOpen;

    /**
     * 网点是否为业务网点 1是 0不是
     */
    private String isOperatingDepartment;

    /**
     * 是否取消核准机构
     */
    private Boolean cancelHeZhun;

    /**
     * 是否有子机构标志位
     */
    private Boolean childs;



    private String corporateBank;


}

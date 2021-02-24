package com.ideatech.ams.system.org.dto;

import com.ideatech.ams.system.org.enums.SyncType;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * @author liangding
 * @create 2018-05-02 上午11:11
 **/
@Data
public class OrganizationSyncDto extends BaseMaintainableDto {
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
     * 之前机构完整ID
     */
    private String beforeFullId;

    /**
     * 之后机构完整ID
     */
    private String afterFullId;
    /**
     *  同步完成
     */
    private Boolean syncFinishStatus = Boolean.FALSE;
    /**
     * 同步成功
     */
    private Boolean syncSuccessStatus = Boolean.FALSE;
    /**
     * 错误信息
     */
    private String errorMsg;

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
     * 同步类型
     */
    private SyncType syncType;

    /**
     * 父类同步id
     */
    private String parentSyncId;

    /**
     * 原始的pbcCode
     */
    private String originalPbcCode;

}

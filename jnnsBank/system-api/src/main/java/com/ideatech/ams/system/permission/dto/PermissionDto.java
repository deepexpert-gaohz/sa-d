package com.ideatech.ams.system.permission.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * @author liangding
 * @create 2018-05-06 下午7:49
 **/
@Data
public class PermissionDto {
    private Long id;

    private String code;

    private String title;

    private Long parentId;

    private String url;

    private String icon;

    private String permissionType;

    private Long orderNum;

    private String method;

    private String description;

    private Boolean enabled;

    //前端权限勾选标识
    private Boolean checked = false;
}

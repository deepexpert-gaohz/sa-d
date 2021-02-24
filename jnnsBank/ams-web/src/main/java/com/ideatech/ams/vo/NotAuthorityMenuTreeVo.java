package com.ideatech.ams.vo;

import com.ideatech.common.vo.TreeNodeVo;
import lombok.Data;

/**
 * 同PermissionDto
 * @author jzh
 * @date 2019/5/24.
 */

@Data
public class NotAuthorityMenuTreeVo extends TreeNodeVo {
    private String code;
    private String description;
    private boolean enabled;
    private String icon;
    private String method;
    private Long orderNum;
    private String permissionType;
    private String title;
    private String url;

}

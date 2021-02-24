package com.ideatech.ams.system.user.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

import java.util.List;

/**
 * @author liangding
 * @create 2018-05-07 下午4:15
 **/
@Data
public class UserSearchDto extends PagingDto<UserDto> {
    private String name;//昵称
    private Long orgId;//所属机构id
    private String orgFullId;
    private String username;//登录账号
    private Long roleId;//角色id
    private String roleName;//角色名称
    private String orgName;//所属机构名称
    private Boolean enabled;//是否启用
}

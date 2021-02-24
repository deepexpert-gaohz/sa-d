package com.ideatech.ams.system.role.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * @author liangding
 * @create 2018-05-06 下午7:54
 **/
@Data
public class RoleDto {
    private Long id;
    private String code;
    private String name;
    private Boolean enabled;
    private String level;
}

package com.ideatech.ams.system.role.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

/**
 * @author liangding
 * @create 2018-05-09 下午5:07
 **/
@Data
public class RoleSearchDto extends PagingDto<RoleDto> {
    private String name;
}

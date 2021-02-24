package com.ideatech.ams.system.permission.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

/**
 * @author liangding
 * @create 2018-05-09 上午1:01
 **/
@Data
public class PermissionSearchDto extends PagingDto<PermissionDto> {
    private Long parentId;
}

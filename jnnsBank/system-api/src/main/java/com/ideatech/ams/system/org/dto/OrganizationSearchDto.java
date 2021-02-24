package com.ideatech.ams.system.org.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

/**
 * @author liangding
 * @create 2018-05-08 上午11:09
 **/
@Data
public class OrganizationSearchDto extends PagingDto<OrganizationDto> {
    private String name;
}

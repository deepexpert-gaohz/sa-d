package com.ideatech.ams.compare.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;
@Data
public class CompareFieldDto extends BaseMaintainableDto {
    private Long id;
    private String name;
    private String field;
}

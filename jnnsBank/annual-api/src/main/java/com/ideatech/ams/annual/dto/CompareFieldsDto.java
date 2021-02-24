package com.ideatech.ams.annual.dto;

import com.ideatech.ams.annual.enums.CompareFieldEnum;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class CompareFieldsDto extends BaseMaintainableDto {

    private Long id;

    private CompareFieldEnum compareFieldEnum;

    /**
     * 是否启用
     */
    private boolean active;

	private Long taskId;

    private boolean lockFiled;
}

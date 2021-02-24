package com.ideatech.ams.annual.dto;

import com.ideatech.ams.annual.enums.CompareFieldEnum;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class CompareRuleDto extends BaseMaintainableDto {

    private Long id;

    private DataSourceEnum dataSourceEnum;

    private CompareFieldEnum compareFieldEnum;

    /**
     * 是否比对
     */
    private boolean active;
    /**
     *  空算过
     */
    private boolean nullpass;

	private Long taskId;
}

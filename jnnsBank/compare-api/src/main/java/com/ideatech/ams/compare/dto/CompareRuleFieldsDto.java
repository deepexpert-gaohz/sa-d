package com.ideatech.ams.compare.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * @author jzh
 * @date 2019/1/17.
 */
@Data
public class CompareRuleFieldsDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 规则id
     */
    private Long compareRuleId;
    /**
     * 比对字段id
     */
    private Long compareFieldId;
    /**
     * 是否使用
     */
    private Boolean active;

    private CompareRuleDto compareRuleDto;

    private CompareFieldDto compareFieldDto;
}

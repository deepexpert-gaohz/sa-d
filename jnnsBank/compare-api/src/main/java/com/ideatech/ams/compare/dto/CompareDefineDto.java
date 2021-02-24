package com.ideatech.ams.compare.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * @author jzh
 * @date 2019/1/17.
 */

@Data
public class CompareDefineDto extends BaseMaintainableDto {

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
     * 数据源id
     */
    private Long dataSourceId;
    /**
     * 是否使用
     */
    private Boolean active;
    /**
     * 空算过
     */
    private Boolean nullpass;

    private CompareRuleDto compareRuleDto;

    private CompareFieldDto compareFieldDto;

    private DataSourceDto dataSourceDto;

}

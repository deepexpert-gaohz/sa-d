package com.ideatech.ams.compare.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * @author jzh
 * @date 2019/1/17.
 */
@Data
public class CompareRuleDataSourceDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 数据源id
     */
    private Long dataSourceId;
    /**
     * 规则id
     */
    private Long compareRuleId;
    /**
     * 先决数据源
     */
    private String parentDataSourceIds;
    /**
     * 是否使用
     */
    private Boolean active;

    private DataSourceDto dataSourceDto;

    private CompareRuleDto compareRuleDto;

}

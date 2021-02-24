package com.ideatech.ams.compare.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;

/**
 * 比对数据源
 */

@Entity
@Data
public class CompareRuleDataSource extends BaseMaintainablePo {
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
}

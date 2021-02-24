package com.ideatech.ams.compare.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;

/**
 * 比对规则
 */

@Entity
@Data
public class CompareDefine extends BaseMaintainablePo {
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
     *  空算过
     */
    private Boolean nullpass;

}

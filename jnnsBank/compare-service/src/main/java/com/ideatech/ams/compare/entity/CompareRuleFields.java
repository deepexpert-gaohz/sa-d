package com.ideatech.ams.compare.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;

/**
 *比对字段与比对规则之前的关联表
 */
@Entity
@Data
public class CompareRuleFields extends BaseMaintainablePo {
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

}

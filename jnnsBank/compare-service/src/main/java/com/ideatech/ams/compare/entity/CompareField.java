package com.ideatech.ams.compare.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 比对字段
 */
@Entity
@Data
public class CompareField extends BaseMaintainablePo {
    /**
     * 名称
     */
    @Column(length = 100)
    private String name;
    /**
     * domain字段
     */
    @Column(length = 100)
    private String field;
}

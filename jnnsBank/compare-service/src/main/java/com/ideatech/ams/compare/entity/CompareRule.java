package com.ideatech.ams.compare.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 比对规则
 */
@Entity(name = "moduleCompareRule")
@Table(name = "yd_compare_rule")
@Data
public class CompareRule extends BaseMaintainablePo {
    /**
     * 规则名称
     */
    @Column(length = 100)
    private String name;

    /**
     * 使用次数
     */
    private Integer count;

    /**
     * 创建时间
     *  yyyy-mm-dd
     */
    private String createTime;

    /**
     * 创建人
     */
    private String creater;
    /**
     * 法人黑名单
     */
    private Boolean personBlackList;
    /**
     * 企业黑名单
     */
    private Boolean bussBlackList;

    /**
     * 机构fullId
     */
    private String organFullId;
}

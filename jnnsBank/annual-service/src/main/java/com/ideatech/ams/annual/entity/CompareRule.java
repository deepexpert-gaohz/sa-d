package com.ideatech.ams.annual.entity;

import com.ideatech.ams.annual.enums.CompareFieldEnum;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;

/**
 * 比对规则定义
 */
@Entity
@Table(name = "yd_comparerule")
@Data
public class CompareRule extends BaseMaintainablePo {

    /**
     * 数据来源
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private DataSourceEnum dataSourceEnum;

    /**
     * 比对字段
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private CompareFieldEnum compareFieldEnum;

    /**
     * 是否比对
     */
    @Column(length = 10)
    private boolean active;
    /**
     *  空算过
     */
    @Column(length = 10)
    private boolean nullpass;

    /**
     * 采集任务ID
     */
	private Long taskId;

}

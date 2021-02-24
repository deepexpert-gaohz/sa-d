package com.ideatech.ams.annual.entity;

import com.ideatech.ams.annual.enums.CompareFieldEnum;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;

/**
 * 比对定义字段，用于保存一个比对定义中需要比对哪些字段
 */
@Entity
@Table(name = "yd_comparefields")
@Data
public class CompareFields extends BaseMaintainablePo {

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private CompareFieldEnum compareFieldEnum;

    /**
     * 是否启用
     */
    @Column(length = 10)
    private boolean active;

    /**
     * 采集任务ID
     */
    private Long taskId;
    /**
     * 是否锁住
     */
    @Column(length = 10)
    private boolean lockFiled = false;

}

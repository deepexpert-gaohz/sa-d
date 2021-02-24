package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 经营异常
 */
@Table(name = "yd_saicinfo_changemess")
@Entity
@Data
public class ChangeMess extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 作出决定机关
     */
    @Column(name = "yd_belongorg")
    private String belongorg;

    /**
     * 列入日期
     */
    @Column(name = "yd_indate")
    private String indate;

    /**
     * 列入经营异常名录原因
     */
    @Column(name = "yd_inreason")
    private String inreason;

    /**
     * 移出日期
     */
    @Column(name = "yd_outdate")
    private String outdate;

    /**
     * 移出经营异常名录原因
     */
    @Column(name = "yd_outreason")
    private String outreason;

    /**
     * 作出决定机关
     */
    @Column(name = "yd_outorgan")
    private String outorgan;
    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

}
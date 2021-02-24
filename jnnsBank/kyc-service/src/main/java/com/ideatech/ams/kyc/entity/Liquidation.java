package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 清算信息
 */
@Table(name = "yd_saicinfo_liquidation")
@Entity
@Data
public class Liquidation extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 债权承接人
     */
    @Column(name = "yd_claintranee")
    private String claintranee;

    /**
     * 债务承接人
     */
    @Column(name = "yd_debttranee")
    private String debttranee;

    /**
     * 清算完结日期
     */
    @Column(name = "yd_ligenddate")
    private String ligenddate;

    /**
     * 清算责任人
     */
    @Column(name = "yd_ligentity")
    private String ligentity;

    /**
     * 清算负责人
     */
    @Column(name = "yd_ligprincipal")
    private String ligprincipal;

    /**
     * 清算完结情况
     */
    @Column(name = "yd_ligst")
    private String ligst;

    /**
     * 清算组成员
     */
    @Column(name = "yd_liqmen")
    private String liqmen;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

}
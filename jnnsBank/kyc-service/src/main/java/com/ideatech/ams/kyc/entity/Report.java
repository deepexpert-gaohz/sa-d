package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 年报
 */
@Table(name = "yd_saicinfo_report")
@Entity
@Data
public class Report extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 年报
     */
    @Column(name = "yd_annualreport")
    private String annualreport;

    /**
     * 发布日期
     */
    @Column(name = "yd_releasedate")
    private String releasedate;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

}
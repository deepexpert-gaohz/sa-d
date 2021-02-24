package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 严重违法失信企业名单
 */
@Table(name = "yd_saicinfo_illegal")
@Entity
@Data
public class Illegal extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 列入日期
     */
    @Column(name = "yd_date")
    private String date;

    /**
     * 移出日期
     */
    @Column(name = "yd_dateout")
    private String dateout;

    /**
     * 序号
     */
    @Column(name = "yd_order")
    private String order;

    /**
     * 做出决定机关（列入）
     */
    @Column(name = "yd_organ")
    private String organ;

    /**
     * 作出决定机关（移出）
     */
    @Column(name = "yd_organout")
    private String organout;

    /**
     * 列入严重违法失信企业名单原因
     */
    @Column(name = "yd_reason")
    private String reason;

    /**
     * 移出严重违法失信企业名单（黑名单）原因
     */
    @Column(name = "yd_reasonout")
    private String reasonout;

    /**
     * 类别
     */
    @Column(name = "yd_type")
    private String type;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

}
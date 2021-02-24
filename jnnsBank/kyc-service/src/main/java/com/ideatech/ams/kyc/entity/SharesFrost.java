package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 股权冻结历史信息
 */
@Table(name = "yd_saicinfo_sharesfrost")
@Data
@Entity
public class SharesFrost extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 冻结金额
     */
    @Column(name = "yd_froam")
    private String froam;

    /**
     * 冻结机关
     */
    @Column(name = "yd_froauth")
    private String froauth;

    /**
     * 冻结文号
     */
    @Column(name = "yd_frodocno")
    private String frodocno;

    /**
     * 冻结起始日期
     */
    @Column(name = "yd_frofrom")
    private String frofrom;

    /**
     * 冻结截至日期
     */
    @Column(name = "yd_froto")
    private String froto;

    /**
     * 解冻机关
     */
    @Column(name = "yd_thawauth")
    private String thawauth;

    /**
     * 解冻说明
     */
    @Column(name = "yd_thawcomment")
    private String thawcomment;

    /**
     * 解冻日期
     */
    @Column(name = "yd_thawdate")
    private String thawdate;

    /**
     * 解冻文号
     */
    @Column(name = "yd_thawdocno")
    private String thawdocno;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

}
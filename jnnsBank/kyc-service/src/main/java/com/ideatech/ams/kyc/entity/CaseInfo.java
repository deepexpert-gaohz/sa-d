package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 行政处罚历史信息
 */
@Table(name = "yd_saicinfo_caseinfo")
@Entity
@Data
public class CaseInfo extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 案由
     */
    @Column(name = "yd_casereason")
    private String casereason;

    /**
     * 案件结果
     */
    @Column(name = "yd_caseresult")
    private String caseresult;

    /**
     * 案发时间
     */
    @Column(name = "yd_casetime")
    private String casetime;

    /**
     * 案件类型
     */
    @Column(name = "yd_casetype")
    private String casetype;

    /**
     * 执行类别
     */
    @Column(name = "yd_exesort")
    private String exesort;

    /**
     * 主要违法事实
     */
    @Column(name = "yd_illegfact")
    private String illegfact;

    /**
     * 处罚金额
     */
    @Column(name = "yd_penam")
    private String penam;

    /**
     * 处罚机关
     */
    @Column(name = "yd_penauth")
    private String penauth;

    /**
     * 处罚依据
     */
    @Column(name = "yd_penbasis")
    private String penbasis;

    /**
     * 处罚决定书签发日期
     */
    @Column(name = "yd_pendesissdate")
    private String pendesissdate;

    /**
     * 处罚执行情况
     */
    @Column(name = "yd_penexest")
    private String penexest;

    /**
     * 处罚结果
     */
    @Column(name = "yd_penresult")
    private String penresult;

    /**
     * 处罚种类
     */
    @Column(name = "yd_pentype")
    private String pentype;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

}
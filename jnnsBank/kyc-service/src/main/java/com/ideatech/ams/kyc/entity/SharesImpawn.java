package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 股权出质历史信息
 */
@Table(name = "yd_saicinfo_sharesimpawn")
@Entity
@Data
public class SharesImpawn extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 出质金额
     */
    @Column(name = "yd_impam")
    private String impam;

    /**
     * 出质审批部门
     */
    @Column(name = "yd_impexaeep")
    private String impexaeep;

    /**
     * 出质备案日期
     */
    @Column(name = "yd_imponrecdate")
    private String imponrecdate;

    /**
     * 质权人姓名
     */
    @Column(name = "yd_imporg")
    private String imporg;

    /**
     * 出质人类别
     */
    @Column(name = "yd_imporgtype")
    private String imporgtype;

    /**
     * 出质批准日期
     */
    @Column(name = "yd_impsandate")
    private String impsandate;

    /**
     * 出质截至日期
     */
    @Column(name = "yd_impto")
    private String impto;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

}
package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 动产抵押信息
 */
@Table(name = "yd_saicinfo_mordetail")
@Entity
@Data
public class MorDetail extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 申请抵押原因
     */
    @Column(name = "yd_appregrea")
    private String appregrea;

    /**
     * 注销日期
     */
    @Column(name = "yd_candate")
    private String candate;

    /**
     * 抵押ID
     */
    @Column(name = "yd_morregid")
    private String morregid;

    /**
     * 登记证号
     */
    @Column(name = "yd_morregcno")
    private String morregcno;

    /**
     * 状态标识
     */
    @Column(name = "yd_mortype")
    private String mortype;

    /**
     * 抵押权人
     */
    @Column(name = "yd_more")
    private String more;

    /**
     * 抵押人
     */
    @Column(name = "yd_mortgagor")
    private String mortgagor;

    /**
     * 履约起始日期
     */
    @Column(name = "yd_pefperfrom")
    private String pefperfrom;

    /**
     * 履约截止日期
     */
    @Column(name = "yd_pefperto")
    private String pefperto;

    /**
     * 被担保主债权数额(万元)
     */
    @Column(name = "yd_priclasecam")
    private String priclasecam;

    /**
     * 被担保主债权种类
     */
    @Column(name = "yd_priclaseckind")
    private String priclaseckind;

    /**
     * 登记机关
     */
    @Column(name = "yd_regorg")
    private String regorg;

    /**
     * 登记日期
     */
    @Column(name = "yd_regidate")
    private String regidate;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

}
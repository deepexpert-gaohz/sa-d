package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 法定代表人其他公司任职信息
 */
@Table(name = "yd_saicinfo_frposition")
@Entity
@Data
public class FrPosition extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 注销日期
     */
    @Column(name = "yd_candate")
    private String candate;

    /**
     * 企业(机构)名称
     */
    @Column(name = "yd_entname")
    private String entname;

    /**
     * 企业状态
     */
    @Column(name = "yd_entstatus")
    private String entstatus;

    /**
     * 企业(机构)类型
     */
    @Column(name = "yd_enttype")
    private String enttype;

    /**
     * 开业日期
     */
    @Column(name = "yd_esdate")
    private String esdate;

    /**
     * 是否法定代表人
     */
    @Column(name = "yd_lerepsign")
    private String lerepsign;

    /**
     * 法定代表人名称
     */
    @Column(name = "yd_name")
    private String name;

    /**
     * 职务
     */
    @Column(name = "yd_position")
    private String position;

    /**
     * 注册资本(万元)
     */
    @Column(name = "yd_regcap")
    private String regcap;

    /**
     * 注册资本币种
     */
    @Column(name = "yd_regcapcur")
    private String regcapcur;

    /**
     * 注册号
     */
    @Column(name = "yd_regno")
    private String regno;

    /**
     * 登记机关
     */
    @Column(name = "yd_regorg")
    private String regorg;

    /**
     * 吊销日期
     */
    @Column(name = "yd_revdate")
    private String revdate;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

}
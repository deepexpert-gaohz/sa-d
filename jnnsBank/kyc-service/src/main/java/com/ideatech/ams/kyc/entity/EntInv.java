package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 企业对外投资情况
 */
@Table(name = "yd_saicinfo_entinv")
@Entity
@Data
public class EntInv extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 注销⽇期
     */
    @Column(name = "yd_candate")
    private String candate;

    /**
     * 出资⽅式
     */
    @Column(name = "yd_conform")
    private String conform;

    /**
     * 企业总数量
     */
    @Column(name = "yd_binvvamount")
    private String binvvamount;

    /**
     * 注册地址⾏政区编号
     */
    @Column(name = "yd_regorgcode")
    private String regorgcode;

    /**
     * 认缴出资币种
     */
    @Column(name = "yd_congrocur")
    private String congrocur;

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
     * 认缴出资额(万元)
     */
    @Column(name = "yd_subconam")
    private String subconam;

    /**
     * 开业日期
     */
    @Column(name = "yd_esdate")
    private String esdate;

    /**
     * 出资比例
     */
    @Column(name = "yd_fundedratio")
    private String fundedratio;

    /**
     * 法定代表⼈姓名
     */
    @Column(name = "yd_name")
    private String name;

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
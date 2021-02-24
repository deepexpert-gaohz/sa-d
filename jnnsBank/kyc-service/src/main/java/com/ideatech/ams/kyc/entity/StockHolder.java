package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 股东
 */
@Table(name = "yd_saicinfo_stockholder")
@Entity
@Data
public class StockHolder extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 出资日期
     */
    @Column(name = "yd_condate")
    private String condate;

    /**
     * 出资比例
     */
    @Column(name = "yd_fundedratio")
    private String fundedratio;

    /**
     * 名称
     */
    @Column(name = "yd_name")
    private String name;

    /**
     * 币种
     */
    @Column(name = "yd_regcapcur")
    private String regcapcur;

    /**
     * 股东类型
     */
    @Column(name = "yd_strtype")
    private String strtype;

    /**
     * 认缴出资额
     */
    @Column(name = "yd_subconam")
    private String subconam;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

    /**
     * 联系地址
     */
    @Column(name = "yd_address")
    private String address;

    /**
     * 证件到期日
     */
    @Column(name = "yd_idcarddue")
    private String idcarddue;

    /**
     * 证件号码
     */
    @Column(name = "yd_idcardno")
    private String idcardno;

    /**
     * 证件类型
     */
    @Column(name = "yd_idcardtype")
    private String idcardtype;

    /**
     * 联系电话
     */
    @Column(name = "yd_telephone")
    private String telephone;

    /**
     * 实缴出资额
     */
    @Column(name = "yd_realamount")
    private String realamount;

    /**
     * 实缴出资日期
     */
    @Column(name = "yd_realdate")
    private String realdate;

    /**
     * 实缴出资方式
     */
    @Column(name = "yd_realtype")
    private String realtype;

    /**
     * 认缴出资方式
     */
    @Column(name = "yd_investtype")
    private String investtype;

    /**
     * 证件起始日期
     */
    private String idCardStart;

    /**
     * 国籍
     */
    private String nationality;

    /**
     * 出生日期
     */
    private String dob;

    /**
     * 性别
     */
    private String sex;


}
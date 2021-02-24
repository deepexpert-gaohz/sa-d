package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 受益人
 */
@Table(name = "yd_saicinfo_beneficiary")
@Entity
@Data
public class Beneficiary extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 联系地址
     */
    @Column(name = "yd_address")
    private String address;

    /**
     * 出资比例
     */
    @Column(name = "yd_capital")
    private String capital;

    /**
     * capitalPercent
     */
    @Column(name = "yd_capitalpercent")
    private String capitalpercent;

    /**
     * 证件到期日
     */
    @Column(name = "yd_idcarddue")
    private String idcarddue;

    /**
     * 证件号码
     */
    @Column(name = "yd_identifyno")
    private String identifyno;

    /**
     * 证件类型
     */
    @Column(name = "yd_identifytype")
    private String identifytype;

    /**
     * 受益人姓名
     */
    @Column(name = "yd_name")
    private String name;

    /**
     * 联系电话
     */
    @Column(name = "yd_telephone")
    private String telephone;

    /**
     * 受益人类型
     */
    @Column(name = "yd_type")
    private String type;

    /**
     * 出资链
     */
    @Column(name = "yd_capitalchain")
    @Lob
    private String capitalchain;


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
package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 监事
 */
@Table(name = "yd_saicinfo_supervise")
@Entity
@Data
public class Supervise extends BaseMaintainablePo {
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
     * 姓名
     */
    @Column(name = "yd_name")
    private String name;

    /**
     * 职位
     */
    @Column(name = "yd_position")
    private String position;

    /**
     * 性别
     */
    @Column(name = "yd_sex")
    private String sex;

    /**
     * 联系电话
     */
    @Column(name = "yd_telephone")
    private String telephone;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;


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

}
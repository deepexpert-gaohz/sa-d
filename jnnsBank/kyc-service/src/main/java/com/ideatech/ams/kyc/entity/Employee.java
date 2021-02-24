package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 主要人员
 */
@Table(name = "yd_saicinfo_employee")
@Entity
@Data
public class Employee extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 职务
     */
    @Column(name = "yd_job")
    private String job;

    /**
     * 姓名
     */
    @Column(name = "yd_name")
    private String name;

    /**
     * 性别
     */
    @Column(name = "yd_sex")
    private String sex;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

    /**
     * 职位类别:董/监/高
     */
    @Column(name = "yd_type")
    private String type;

}
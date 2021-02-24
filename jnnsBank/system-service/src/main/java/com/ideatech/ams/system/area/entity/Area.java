package com.ideatech.ams.system.area.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 地区
 */
@Table(name = "base_area")
@Data
@Entity
public class Area extends BaseMaintainablePo {

    /**
     * 地区编码
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 地区名称
     */
    @Column(name = "area_name")
    private String areaName;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 注册地区代码
     */
    @Column(name = "reg_code")
    private String regCode;

    /**
     * 邮编
     */
    @Column(name = "zipcode")
    private String zipcode;
}

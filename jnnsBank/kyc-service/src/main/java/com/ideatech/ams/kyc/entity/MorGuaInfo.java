package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 动产抵押物信息
 */
@Table(name = "yd_saicinfo_morguainfo")
@Entity
@Data
public class MorGuaInfo extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 抵押物名称
     */
    @Column(name = "yd_guaname")
    private String guaname;

    /**
     * 抵押ID
     */
    @Column(name = "yd_morregid")
    private String morregid;

    /**
     * 数量
     */
    @Column(name = "yd_quan")
    private String quan;

    /**
     * 价值(万元)
     */
    @Column(name = "yd_value")
    private String value;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

}
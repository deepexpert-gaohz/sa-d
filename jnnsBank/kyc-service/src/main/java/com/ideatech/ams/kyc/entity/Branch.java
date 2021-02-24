package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 分支机构
 */
@Table(name = "yd_saicinfo_branch")
@Entity
@Data
public class Branch extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private String saicinfoId;

    /**
     * 分支机构地址
     */
    @Column(name = "yd_braddr")
    private String braddr;

    /**
     * 分支机构负责人
     */
    @Column(name = "yd_brprincipal")
    private String brprincipal;

    /**
     * 分支机构企业注册号
     */
    @Column(name = "yd_brregno")
    private String brregno;

    /**
     * 一般经营项目
     */
    @Column(name = "yd_cbuitem")
    private String cbuitem;

    /**
     * 分支机构名称
     */
    @Column(name = "yd_name")
    private String name;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

}
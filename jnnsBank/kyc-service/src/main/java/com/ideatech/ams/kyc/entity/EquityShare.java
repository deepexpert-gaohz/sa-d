package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 股权机构
 */
@Table(name = "yd_saicinfo_equityshare")
@Entity
@Data
public class EquityShare extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 出资额
     */
    @Column(name = "yd_capital")
    private String capital;

    /**
     * 层级
     */
    @Column(name = "yd_layer")
    private Integer layer;

    /**
     * 企业名称或者人名
     */
    @Column(name = "yd_name")
    private String name;

    /**
     * 节点ID
     */
    @Column(name = "yd_nodeid")
    private Long nodeid;

    /**
     * 父节点ID
     */
    @Column(name = "yd_parentnodeid")
    private Long parentnodeid;

    /**
     * 股份占比
     */
    @Column(name = "yd_percent")
    private Float percent;

    /**
     * 类型
     */
    @Column(name = "yd_type")
    private String type;

//    /**
//     * 序列号 来自IDP
//     */
//    @Column(name = "yd_index")
//    private Integer index;

    /**父节点名称 来自IDP*/
    @Transient
    private String parent;

    /**子节点*/
    @Transient
    private List<EquityShare> childs = new ArrayList<EquityShare>();

}
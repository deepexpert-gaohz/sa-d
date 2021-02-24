package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EquityShareDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 出资额
     */
    private String capital;

    /**
     * 层级
     */
    private Integer layer;

    /**
     * 企业名称或者人名
     */
    private String name;

    /**
     * 节点ID
     */
    private Long nodeid;

    /**
     * 父节点ID
     */
    private Long parentnodeid;

    /**
     * 股份占比
     */
    private Float percent;

    /**
     * 类型
     */
    private String type;

//    /**
//     * 序列号 来自IDP
//     */
//    @Column(name = "yd_index")
//    private Integer index;

    /**父节点名称 来自IDP*/
    private String parent;

    /**子节点*/
    private List<EquityShareDto> childs = new ArrayList<EquityShareDto>();;

}
package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class BranchDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private String saicinfoId;

    /**
     * 分支机构地址
     */
    private String braddr;

    /**
     * 分支机构负责人
     */
    private String brprincipal;

    /**
     * 分支机构企业注册号
     */
    private String brregno;

    /**
     * 一般经营项目
     */
    private String cbuitem;

    /**
     * 分支机构名称
     */
    private String name;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}

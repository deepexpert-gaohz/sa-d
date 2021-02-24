package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class LiquidationDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 债权承接人
     */
    private String claintranee;

    /**
     * 债务承接人
     */
    private String debttranee;

    /**
     * 清算完结日期
     */
    private String ligenddate;

    /**
     * 清算责任人
     */
    private String ligentity;

    /**
     * 清算负责人
     */
    private String ligprincipal;

    /**
     * 清算完结情况
     */
    private String ligst;

    /**
     * 清算组成员
     */
    private String liqmen;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}
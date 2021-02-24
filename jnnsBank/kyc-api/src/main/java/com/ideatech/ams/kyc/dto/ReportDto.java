package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class ReportDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 年报
     */
    private String annualreport;

    /**
     * 发布日期
     */
    private String releasedate;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}
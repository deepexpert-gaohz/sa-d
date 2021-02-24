package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class ChangeRecordDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 变更时间
     */
    private String changedate;

    /**
     * 变更项
     */
    private String type;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

    /**
     * 变更后内容
     */
    private String aftercontent;

    /**
     * 变更前内容
     */
    private String beforecontent;

}
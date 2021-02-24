package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class MorGuaInfoDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 抵押物名称
     */
    private String guaname;

    /**
     * 抵押ID
     */
    private String morregid;

    /**
     * 数量
     */
    private String quan;

    /**
     * 价值(万元)
     */
    private String value;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}
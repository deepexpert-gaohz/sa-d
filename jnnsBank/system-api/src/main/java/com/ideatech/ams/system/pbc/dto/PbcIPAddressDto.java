package com.ideatech.ams.system.pbc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class PbcIPAddressDto extends BaseMaintainableDto {
    private Long id;

    /**
     * 人行ip地址
     */
    private String ip;

    /**
     * 省份
     */
    private String provinceName;

    /**
     * 人行年检是否提交标记(true: 人行年检提交  false: 人行年检不提交)
     */
    private Boolean isAnnualSubmit;

}

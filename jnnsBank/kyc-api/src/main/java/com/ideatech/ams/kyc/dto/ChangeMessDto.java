package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class ChangeMessDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 作出决定机关
     */
    private String belongorg;

    /**
     * 列入日期
     */
    private String indate;

    /**
     * 列入经营异常名录原因
     */
    private String inreason;

    /**
     * 移出日期
     */
    private String outdate;

    /**
     * 移出经营异常名录原因
     */
    private String outreason;

    /**
     * 作出决定机关
     */
    private String outorgan;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}
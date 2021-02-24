package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class SharesFrostDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 冻结金额
     */
    private String froam;

    /**
     * 冻结机关
     */
    private String froauth;

    /**
     * 冻结文号
     */
    private String frodocno;

    /**
     * 冻结起始日期
     */
    private String frofrom;

    /**
     * 冻结截至日期
     */
    private String froto;

    /**
     * 解冻机关
     */
    private String thawauth;

    /**
     * 解冻说明
     */
    private String thawcomment;

    /**
     * 解冻日期
     */
    private String thawdate;

    /**
     * 解冻文号
     */
    private String thawdocno;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}
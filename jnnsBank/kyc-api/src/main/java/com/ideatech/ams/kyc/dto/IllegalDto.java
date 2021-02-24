package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class IllegalDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 列入日期
     */
    private String date;

    /**
     * 移出日期
     */
    private String dateout;

    /**
     * 序号
     */
    private String order;

    /**
     * 做出决定机关（列入）
     */
    private String organ;

    /**
     * 作出决定机关（移出）
     */
    private String organout;

    /**
     * 列入严重违法失信企业名单原因
     */
    private String reason;

    /**
     * 移出严重违法失信企业名单（黑名单）原因
     */
    private String reasonout;

    /**
     * 类别
     */
    private String type;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}
package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class IllegalQueriesDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 企业名称
     */
    private String name;

    /**
     *  注册号
     */
    private String unityCreditCode;

    /**
     *类型
     */
    private String type;

    /**
     *列入原因
     */
    private String inReason;

    /**
     *列入时间
     */
    private String inDate;

    /**
     *列入机关
     */
    private String inOrgan;

    /**
     *移出原因
     */
    private String outReason;

    /**
     *移出时间
     */
    private String outDate;

    /**
     *移出机关
     */
    private String outOrgan;
}

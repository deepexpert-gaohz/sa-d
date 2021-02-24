package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class SharesImpawnDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 出质金额
     */
    private String impam;

    /**
     * 出质审批部门
     */
    private String impexaeep;

    /**
     * 出质备案日期
     */
    private String imponrecdate;

    /**
     * 质权人姓名
     */
    private String imporg;

    /**
     * 出质人类别
     */
    private String imporgtype;

    /**
     * 出质批准日期
     */
    private String impsandate;

    /**
     * 出质截至日期
     */
    private String impto;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}
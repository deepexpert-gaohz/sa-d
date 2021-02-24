package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class MorDetailDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 申请抵押原因
     */
    private String appregrea;

    /**
     * 注销日期
     */
    private String candate;

    /**
     * 抵押ID
     */
    private String morregid;

    /**
     * 登记证号
     */
    private String morregcno;

    /**
     * 状态标识
     */
    private String mortype;

    /**
     * 抵押权人
     */
    private String more;

    /**
     * 抵押人
     */
    private String mortgagor;

    /**
     * 履约起始日期
     */
    private String pefperfrom;

    /**
     * 履约截止日期
     */
    private String pefperto;

    /**
     * 被担保主债权数额(万元)
     */
    private String priclasecam;

    /**
     * 被担保主债权种类
     */
    private String priclaseckind;

    /**
     * 登记机关
     */
    private String regorg;

    /**
     * 登记日期
     */
    private String regidate;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}
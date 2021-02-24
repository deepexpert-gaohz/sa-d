package com.ideatech.ams.annual.dto;

import lombok.Data;

/**
 * @Description 采集的时间配置
 * @Author wanghongjie
 * @Date 2018/8/9
 **/
@Data
public class CollectConfigDto {

    private Long annualTaskId;

    /**
     * 人行采集时间配置
     */
    private boolean pbcUnlimited;

    /**
     * 人行采集开始日期
     */
    private String pbcStartDate;

    /**
     * 人行采集开始时间
     */
    private String pbcStartTime;

    /**
     * 人行采集结束日期
     */
    private String pbcEndDate;

    /**
     * 人行采集结束时间
     */
    private String pbcEndTime;

    /**
     * 工商采集时间配置
     */
    private boolean saicUnlimited;

    /**
     * 工商采集日期
     */
    private String saicStartDate;

    /**
     * 工商采集时间
     */
    private String saicStartTime;

    /**
     * 工商结束日期
     */
    private String saicEndDate;

    /**
     * 工商结束时间
     */
    private String saicEndTime;

}

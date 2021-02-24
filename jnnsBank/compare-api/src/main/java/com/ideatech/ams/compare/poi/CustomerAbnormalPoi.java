package com.ideatech.ams.compare.poi;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/6/17.
 */

@Data
public class CustomerAbnormalPoi {

    /**
     * 客户名称
     */
    private String depositorName;

    /**
     * 银行机构代码
     */
    private String code;

    /**
     * 机构名称
     */
    private String organName;


    /**
     * 是否严重违法
     */
    private String illegal;

    /**
     * 是否经营异常
     */
    private String changeMess;

    /**
     * 是否营业到期
     */
    private String businessExpires;


    /**
     * 是否异常工商状态
     */
    private String abnormalState;

    /**
     * 是否信息异动
     */
    private String changed;

    /**
     * 异动时间
     */
    private String abnormalTime;

    /**
     * 处理状态
     */
    private String processState;

    /**
     * 处理时间
     */
    private String processTime;

    /**
     * 处理人
     */
    private String processer;

    /**
     * 是否发送短信 （1发送成功 2发送失败 0未发送）
     */
    private String message;
}

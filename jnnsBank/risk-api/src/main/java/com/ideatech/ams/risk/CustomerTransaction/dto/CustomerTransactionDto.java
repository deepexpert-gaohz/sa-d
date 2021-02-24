package com.ideatech.ams.risk.CustomerTransaction.dto;

import lombok.Data;

@Data
public class CustomerTransactionDto {
    private Long id;
    /**
     * 存款人名称
     */
    private String depositorName;


    /**
     * 机构id
     */
    private String organFullId;


    /**
     * 银行机构代码
     */
    private String code;

    /**
     * 机构名称
     */
    private String organName;

    /**
     * 原始工商信息saicInfo
     */
    private Long saicInfoId;

    /**
     * 是否严重违法
     */
    private Boolean illegal;

    /**
     * 是否经营异常
     */
    private Boolean changeMess;

    /**
     * 是否营业到期
     */
    private Boolean businessExpires;



    /**
     * 是否异常工商状态
     */
    private Boolean abnormalState;

    /**
     * 是否信息异动
     */
    private Boolean changed;

    /**
     * 是否为异常客户（客户异动管理页面过滤使用）
     */
    private Boolean abnormal;

    /**
     * 异动时间
     */
    private String abnormalTime;

    /**
     * 处理人
     */
    private String processer;

    /**
     * 处理时间
     */
    private String processTime;

    /**
     * 处理动作
     */
    private String processAction;

    /**
     * 处理状态
     */
    private String processState;

    /**
     * 是否发送短信 （1发送成功 2发送失败 0未发送）
     */
    private String message;

}

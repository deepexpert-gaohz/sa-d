package com.ideatech.ams.account.dto.poi;


import lombok.Data;

@Data
public class StatisticsForDatePoi {

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 企业名称
     */
    private String depositorName;

    /**
     * 账户类型
     */
    private String acctType;

    /**
     * 操作类型
     */
    private String billType;

    /**
     * 网点机构号
     */
    private String organCode;

    /**
     * 申请日期
     */
    private String acctCreateDate;

    /**
     * 申请人
     */
    private String createdBy;

    /**
     * 人行上报状态
     */
    private String pbcSyncStatus;

    /**
     * 信用上报状态
     */
    private String eccsSyncStatus;

    /**
     * 人行上报时间
     */
    private String pbcSyncTime;

    /**
     * 信用上报时间
     */
    private String eccsSyncTime;
}

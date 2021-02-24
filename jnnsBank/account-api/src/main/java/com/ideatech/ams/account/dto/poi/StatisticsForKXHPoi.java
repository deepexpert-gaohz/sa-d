package com.ideatech.ams.account.dto.poi;


import com.ideatech.ams.account.enums.OpenAccountSiteType;
import com.ideatech.ams.account.enums.bill.CompanySyncOperateType;
import lombok.Data;

@Data
public class StatisticsForKXHPoi {

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
     * 人行上报状态
     */
    private String pbcSyncStatus;

    /**
     * 信用上报状态
     */
    private String eccsSyncStatus;

    /**
     * 本地异地标识(0本地、1异地)
     */
    private String openAccountSiteType;

    /**
     * 申请日期
     */
    private String acctCreateDate;

    /**
     * 申请人
     */
    private String createdBy;

    /**
     * 账户上报方式(01手工上报02自动上报03手工补录04手工虚拟上报05线下手工报备)
     */
    private String pbcSyncMethod;
}

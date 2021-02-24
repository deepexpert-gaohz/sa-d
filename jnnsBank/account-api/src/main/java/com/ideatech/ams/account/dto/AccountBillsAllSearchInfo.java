package com.ideatech.ams.account.dto;

import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.enums.bill.CompanyAmsCheckStatus;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by houxianghua on 2018/11/16.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountBillsAllSearchInfo extends AccountBillsAllInfo {

    /**
     * 申请开始时间（查询条件）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDate;

    /**
     * 申请结束时间（查询条件）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /**
     * 账管上报开始时间（查询条件）
     */
    private String pbcSyncBeginDate;

    /**
     * 账管上报结束时间（查询条件）
     */
    private String pbcSyncEndDate;

    /**
     * 信用代码上报开始时间（查询条件）
     */
    private String eccsSyncBeginDate;

    /**
     * 信用代码上报结束时间（查询条件）
     */
    private String eccsSyncEndDate;

    /**
     * 上报开始时间（查询条件）
     */
    private String syncBeginDate;

    /**
     * 上报结束时间（查询条件）（账管上报或者信用代码上报的上报时间）
     */
    private String syncEndDate;

    /**
     * 核准核准开始日期（查询条件）
     */
    private String pbcCheckBeginDate;

    /**
     * 核准核准结束日期（查询条件）
     */
    private String pbcCheckEndDate;

    /**
     * 申请人id集合（查询条件）
     */
    private List<String> createdByes;

    /**
     * 机构fullId集合（查询条件）
     */
    private List<String> organFullIdList;

    /**
     * 网点机构号（核心机构号）
     */
    private String kernelOrgCode;

    /**
     * 网点机构名称（核心机构名称）
     */
    private String kernelOrgName;

    /**
     * 组织机构代码
     */
    private String orgCode;

    /**
     * 白名单查询条件集合
     */
    private List<String> whiteLists;

    /**
     * 页面区分是否白名单页面  1：白名单数据菜单  0：正常数据
     */
    private String whiteList;

    /**
     * 单据日期billDate开始时间（查询条件）
     */
    private String billBeginDate;

    /**
     * 单据日期billDate结束时间（查询条件）
     */
    private String billEndDate;
    /**
     * 人行核准状态(01待审核、02审核通过、03无需审核)
     */
    private List<CompanyAmsCheckStatus> pbcCheckStatuses;
    /**
     * 影像上报列表标识
     */
    private String syncImageList;
    /**
     * 上报人行账管状态（01成功；02失败；03无需上报）
     */
    private CompanySyncStatus pbcSyncStatus;
    /**
     * 影像上报状态
     */
    private CompanySyncStatus imgaeSyncStatus;

    /**
     * 影像上报状态
     */
    private List<CompanySyncStatus> imgaeSyncStatuses;

}

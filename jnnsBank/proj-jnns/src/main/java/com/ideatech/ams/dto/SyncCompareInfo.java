package com.ideatech.ams.dto;

import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * 上报账户实时比对报表Info
 *
 * @auther ideatech
 * @create 2018-11-29 9:41 AM
 **/

@Data
public class SyncCompareInfo {

    private Long id;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 核心机构号
     */
    private String organCode;

    /**
     * 完整机构号
     */
    private String organFullId;

    /**
     * 开户时间
     */
    private String acctOpenDate;

    /**
     * 企业名称
     */
    private String depositorName;

    /**
     * 账户性质
     */
    private String acctType;


    /**
     * 开始日期
     */
    private String businessbeginDate;

    /**
     * 结束日期
     */
    private String businessendDate;



    /**
     * 业务日期
     */
    private String businessDate;


    /**
     * 开销户标志
     * 01:开户标志
     * 02：销户
     * 03：变更
     */
    @Column(length = 100)
    private String kaixhubz;


    /**
     * 账户状态集合
     */
    private List<String> acctTypeList;

    /**
     * 开户机构
     */
    private String  kaihjigo;

    /**
     * 账户分类代码1
     */
    private String zhufldm1;


    /**
     * 销户机构
     */
    private String  xiohjigo;

    /**
     * 人行上报状态
     */
    @Column(length = 50)
    private String pbcStarts;

    /**
     * 机构信用代码上报状态
     */
    @Column(length = 50)
    private String eccsStarts;

    /**
     * 人行上报状态集合
     */
    private List<String> pbcStartsList;

    /**
     * 机构信用代码上报状态集合
     */
    private List<String> eccsStartsList;

}

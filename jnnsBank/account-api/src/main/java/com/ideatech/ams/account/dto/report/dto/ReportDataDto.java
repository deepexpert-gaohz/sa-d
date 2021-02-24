package com.ideatech.ams.account.dto.report.dto;

import lombok.Data;

@Data
public class ReportDataDto {

    //存款人类别
    private String depositorType;

    //基本存款账户
    //本月用户开立情况
    //基本存款账户开户数
    private String baseAcct;
    //基本存款账户上年开户数
    private String baseLYAcct;
    //基本存款账户上月开户数
    private String baseLMAcct;
    //基本存款账户同比变化率
    private String baseYToY;
    //基本存款账户环比变化率
    private String baseMToM;

    //存量账户情况
    //基本存款账户存量数
    private String baseStorageCount;
    //基本存款账户上年存量数
    private String baseLYStorageCount;
    //基本存款账户上月存量数
    private String baseLMStorageCount;
    //存量账户同比
    private String baseStorageYToY;
    //存量账户环比
    private String baseStorageMToM;


    //一般存款账户
    //本月用户开立情况
    //一般存款账户开户数
    private String generalAcct;
    //一般存款账户上年开户数
    private String generalLYAcct;
    //一般存款账户上月开户数
    private String generalLMAcct;
    //一般存款账户同比变化率
    private String generalYToY;
    //一般存款账户环比变化率
    private String generalMToM;

    //存量账户情况
    //一般存款账户存量数
    private String generalStorageCount;
    //一般存款账户上年存量数
    private String generalLYStorageCount;
    //一般存款账户上月存量数
    private String generalLMStorageCount;
    //存量账户同比
    private String generalStorageYToY;
    //存量账户环比
    private String generalStorageMToM;


    //专用存款账户
    //本月用户开立情况
    //专用存款账户开户数
    private String specialAcct;
    //专用存款账户上年开户数
    private String specialLYAcct;
    //专用存款账户上月开户数
    private String specialLMAcct;
    //专用存款账户同比变化率
    private String specialYToY;
    //专用存款账户环比变化率
    private String specialMToM;
    //存量账户情况
    //专用存款账户存量数
    private String specialStorageCount;
    //专用存款账户上年存量数
    private String specialLYStorageCount;
    //专用存款账户上月存量数
    private String specialLMStorageCount;
    //存量账户同比
    private String specialStorageYToY;
    //存量账户环比
    private String specialStorageMToM;


    //临时存款账户
    //本月用户开立情况
    //临时存款账户开户数
    private String provisionalAcct;
    //临时存款账户上年开户数
    private String provisionalLYAcct;
    //临时存款账户上月开户数
    private String provisionalLMAcct;
    //临时存款账户同比变化率
    private String provisionalYToY;
    //临时存款账户环比变化率
    private String provisionalMToM;
    //存量账户情况
    //临时存款账户存量数
    private String provisionalStorageCount;
    //临时存款账户上年存量数
    private String provisionalLYStorageCount;
    //临时存款账户上月存量数
    private String provisionalLMStorageCount;
    //存量账户同比
    private String provisionalStorageYToY;
    //存量账户环比
    private String provisionalStorageMToM;


    //合计
    //本月用户开立情况
    private String totalAcct;
    //上年开户数
    private String totalLYAcct;
    //上月开户数
    private String totalLMAcct;
    //合计存款账户同比变化率
    private String totalYToY;
    //合计存款账户环比变化率
    private String totalMToM;

    //存量账户情况
    //合计存量账户数
    private String totalStorageCount;
    //合计上年存量账户数
    private String totalLYStorageCount;
    //合计上月存量账户数
    private String totalLMStorageCount;
    //合计存量账户同比
    private String totalStorageYToY;
    //合计存量账户环比
    private String totalStorageMToM;


}

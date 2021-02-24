package com.ideatech.ams.risk.report.dto;

import lombok.Data;

@Data
public class ReportDataDto {

    //存款人类别
    private String depositorType;

    //基本存款账户
    //本月用户开立情况
    //基本存款账户开户数
    private String baseAcct;
    //基本存款账户同比变化率
    private String baseYToY;
    //基本存款账户环比变化率
    private String baseMToM;
    //存量账户情况
    //基本存款账户存量数
    private String baseStorageCount;
    //存量账户同比
    private String baseStorageYToY;
    //存量账户环比
    private String baseStorageMToM;


    //一般存款账户
    //本月用户开立情况
    //一般存款账户开户数
    private String generalAcct;
    //一般存款账户同比变化率
    private String generalYToY;
    //一般存款账户环比变化率
    private String generalMToM;
    //存量账户情况
    //一般存款账户存量数
    private String generalStorageCount;
    //存量账户同比
    private String generalStorageYToY;
    //存量账户环比
    private String generalStorageMToM;


    //专用存款账户
    //本月用户开立情况
    //专用存款账户开户数
    private String specialAcct;
    //专用存款账户同比变化率
    private String specialYToY;
    //专用存款账户环比变化率
    private String specialMToM;
    //存量账户情况
    //专用存款账户存量数
    private String specialStorageCount;
    //存量账户同比
    private String specialStorageYToY;
    //存量账户环比
    private String specialStorageMToM;


    //临时存款账户
    //本月用户开立情况
    //临时存款账户开户数
    private String provisionalAcct;
    //临时存款账户同比变化率
    private String provisionalYToY;
    //临时存款账户环比变化率
    private String provisionalMToM;
    //存量账户情况
    //临时存款账户存量数
    private String provisionalStorageCount;
    //存量账户同比
    private String provisionalStorageYToY;
    //存量账户环比
    private String provisionalStorageMToM;


    //合计
    //本月用户开立情况
    //临时存款账户开户数
    private String totalAcct;
    //临时存款账户同比变化率
    private String totalYToY;
    //临时存款账户环比变化率
    private String totalMToM;
    //存量账户情况
    //临时存款账户存量数
    private String totalStorageCount;
    //存量账户同比
    private String totalStorageYToY;
    //存量账户环比
    private String totalStorageMToM;


}

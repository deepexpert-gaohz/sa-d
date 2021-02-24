package com.ideatech.ams.account.dto;

import lombok.Data;

@Data
public class ReportStatisticsForDateDTO {
    private String createdDate;//日期
    private Long allNum;//当日增量业务数
    private Long pbcNumAll;//需上报人行
    private Long pbcNum;//上报人行成功数
    private Long eccsNumAll;//需上报信用代码数
    private Long eccsNum;//上报信用代码成功数
}

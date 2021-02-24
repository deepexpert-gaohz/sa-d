package com.ideatech.ams.account.dto.report.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class ReportAccountDto extends PagingDto<ReportAccountDto> {

    private String depositorType;
    private String acctBigType;
    private String currentMonthData;
    private String lastMonthData;
    private String lastYearData;

    private String beginPeriodData;//期初余额
    private String endPeriodData;//期末资金
    private String inflowData;//流入资金
    private String outflowData;//流出资金

}

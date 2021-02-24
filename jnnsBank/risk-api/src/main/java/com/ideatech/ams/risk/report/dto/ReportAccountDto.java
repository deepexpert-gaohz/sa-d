package com.ideatech.ams.risk.report.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class ReportAccountDto extends PagingDto<ReportAccountDto> {

    private String depositorType;
    private String acctBigType;
    private String currentMonthData;
    private String lastMonthData;
    private String lastYearData;

    public ReportAccountDto(String depositorType,String acctBigType, String currentMonthData, String lastMonthData, String lastYearData) {
        this.depositorType = depositorType;
        this.acctBigType = acctBigType;
        this.currentMonthData = currentMonthData;
        this.lastMonthData = lastMonthData;
        this.lastYearData = lastYearData;
    }
}

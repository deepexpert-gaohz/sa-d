package com.ideatech.ams.risk.report.service;

import com.ideatech.ams.account.dto.report.dto.AcctNumChangeRepDto;
import com.ideatech.ams.account.dto.report.dto.ReportAccountDto;
import com.ideatech.ams.account.dto.report.dto.ReportDataDto;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.text.ParseException;
import java.util.List;

public interface ReportDataService {
    List<ReportDataDto> createAqmatReports(String currentDateStr) throws ParseException;

    HSSFWorkbook exportXLS(String currentDateStr) throws ParseException;
    HSSFWorkbook  exportAcctChangeXLS(String currentDateStr) throws ParseException;
    HSSFWorkbook  exportfinancialLiquXLS(String year, String season) throws ParseException;
    List<AcctNumChangeRepDto> createAcctNumChange(String currentDateStr);

    List<ReportAccountDto> createFinancialLiquReport(String currentDateStr);
}

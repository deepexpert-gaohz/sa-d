package com.ideatech.ams.account.dto.report.service;

import com.ideatech.ams.account.dto.report.dto.ReportDataDto;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.text.ParseException;
import java.util.List;

public interface ReportDataService {
    List<ReportDataDto> createAqmatReports(String currentDateStr) throws ParseException;

    HSSFWorkbook exportXLS(String currentDateStr) throws ParseException;
}

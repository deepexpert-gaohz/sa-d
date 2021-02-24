package com.ideatech.ams.controller.risk;

import com.ideatech.ams.account.dto.report.dto.ReportDataDto;
import com.ideatech.ams.account.dto.report.dto.ReportReturnDto;
import com.ideatech.ams.account.dto.report.service.ReportDataService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportDataController {

    @Autowired
    private ReportDataService reportDataService;
    /**
     * 企业银行账户数量监测分析表
     * @Date 2019-06-01
     * @author yangcq
     * @return
     */
    @GetMapping("/createReportData")
    public ResultDto createReportData(String currentDateStr) throws ParseException {

        List<ReportDataDto> list = reportDataService.createAqmatReports(currentDateStr);
        ReportReturnDto reportReturnDto = new ReportReturnDto();
        reportReturnDto.setList(list);
        reportReturnDto.setTotalRecord((long)list.size());
        reportReturnDto.setTotalPages(100);
        return ResultDtoFactory.toAckData(reportReturnDto);
    }

    @RequestMapping("/exportXLS")
    public void exportXLS(String currentDateStr, HttpServletResponse response) throws Exception {
        HSSFWorkbook wb = reportDataService.exportXLS(currentDateStr);
        //指定下载的文件名
        response.setHeader("Content-Disposition", "attachment;filename=" +  new String(
                "企业银行账户数量监测分析表.xls".getBytes(),"iso-8859-1"));
        response.setContentType("application/vnd.ms-excel");

        try {
            OutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

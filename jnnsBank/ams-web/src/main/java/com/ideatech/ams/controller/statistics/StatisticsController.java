package com.ideatech.ams.controller.statistics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.customer.dto.illegal.IllegalQueryBatchDto;
import com.ideatech.ams.system.annotation.dto.MessageLogDto;
import com.ideatech.ams.system.annotation.service.MessageLogService;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.BrowserUtil;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@RestController
@RequestMapping("/statistics")
@Slf4j
public class StatisticsController {

    @Autowired
    private MessageLogService messageLogService;

    @GetMapping("/page")
    public TableResultResponse<MessageLogDto> batchList(MessageLogDto dto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        TableResultResponse<MessageLogDto> tableResultResponse = messageLogService.queryPage(dto, pageable);
        return tableResultResponse;
    }

    @GetMapping("/view/{id}")
    public ObjectRestResponse viewData(@PathVariable("id") Long id) {
        MessageLogDto messageLogDto = messageLogService.findById(id);
        return new ObjectRestResponse<>().rel(true).result(JSON.toJSON(messageLogDto));
    }

    @GetMapping("/findByDate")
    public ObjectRestResponse findByDate(MessageLogDto dto) {
        JSONObject json = messageLogService.findByDate(dto);
        return new ObjectRestResponse<>().rel(true).result(json);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadStatisticsPoiResultExcel(MessageLogDto dto, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        IExcelExport illegalQueryExcel = messageLogService.exportStatisticePoiExcel(dto);
        response.setHeader("Content-disposition", "attachment; filename="+generateFileName("接口监控记录",request)+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",illegalQueryExcel);
        toClient.flush();
        response.getOutputStream().close();
    }

    /**
     * 生成导出文件名
     * @param
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    private String generateFileName(String fileName1, HttpServletRequest request) throws UnsupportedEncodingException {
        StringBuilder fileName = new StringBuilder();
        fileName.append(BrowserUtil.generateFileName(fileName1,request))
                .append("-").append(DateUtils.DateToStr(new Date(),"yyyy-MM-dd"));
        return fileName.toString();
    }
}

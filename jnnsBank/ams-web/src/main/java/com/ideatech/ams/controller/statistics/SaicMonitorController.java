package com.ideatech.ams.controller.statistics;

import com.ideatech.ams.account.vo.AccountStatisticsInfoVo;
import com.ideatech.ams.customer.dto.SaicMonitorDto;
import com.ideatech.ams.customer.dto.SaicStateDto;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.dto.TreeTable;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.BrowserUtil;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@RestController
@RequestMapping("/saicMonitor")
@Slf4j
public class SaicMonitorController {

    @Autowired
    private SaicMonitorService saicMonitorService;

    @Autowired
    private SaicStateDto saicStateDto;

    @GetMapping("/info/list")
    public TreeTable menuInfoList(Long pid, AccountStatisticsInfoVo accountStatisticsInfoVo){
        Long organId = null;
        if (pid == null) {
            organId = SecurityUtils.getCurrentUser().getOrgId();
        }
        return saicMonitorService.query(pid, organId);
    }


    @GetMapping("/page")
    public TableResultResponse<SaicMonitorDto> batchList(SaicMonitorDto dto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        TableResultResponse<SaicMonitorDto> tableResultResponse = saicMonitorService.queryPage(dto, pageable);
        return tableResultResponse;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadStatisticsPoiResultExcel(SaicMonitorDto dto, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        IExcelExport illegalQueryExcel = saicMonitorService.exportSaicMonitorExcel(dto);
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

    /**
     * 获取工商接口状态
     */
    @GetMapping("/state")
    public ResultDto getState(){
        if (saicStateDto==null){
            return ResultDtoFactory.toNack("获取失败");
        }else {
            return ResultDtoFactory.toAck("获取成功",saicStateDto);
        }
    }
}

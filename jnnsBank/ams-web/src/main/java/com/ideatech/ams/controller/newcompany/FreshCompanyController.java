package com.ideatech.ams.controller.newcompany;

import com.ideatech.ams.customer.dto.neecompany.FreshCompanyConfigDto;
import com.ideatech.ams.customer.dto.neecompany.FreshCompanyDto;
import com.ideatech.ams.customer.service.newcompany.FreshCompanyConfigService;
import com.ideatech.ams.customer.service.newcompany.FreshCompanyService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
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

@Slf4j
@RestController
@RequestMapping(value = "/freshCompany")
public class FreshCompanyController {

    @Autowired
    private FreshCompanyService freshCompanyService;

    @Autowired
    private FreshCompanyConfigService freshCompanyConfigService;


    /**
     * 分页查询
     * @param dto
     * @param pageable
     * @return
     */
    @GetMapping("/query")
    public TableResultResponse<FreshCompanyDto> list(FreshCompanyDto dto, @PageableDefault(sort = {"openDate","id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        TableResultResponse<FreshCompanyDto> tableResultResponse = freshCompanyService.query(dto, pageable);

        return tableResultResponse;
    }

    /**
     * 详情
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public ResultDto<FreshCompanyDto> list(Long id) {
        FreshCompanyDto freshCompanyDto = freshCompanyService.detail(id);

        if(freshCompanyDto == null) {
            return ResultDtoFactory.toNack("");
        }
        return ResultDtoFactory.toAckData(freshCompanyDto);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadFreshCompanyExcel(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IExcelExport freshCompanyExcel = freshCompanyService.exportExcel();
        response.setHeader("Content-disposition", "attachment; filename="+generateFileName("新增企业信息",request)+".xls");
        response.setContentType("application/octet-stream");

        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd", freshCompanyExcel);
        toClient.flush();
        response.getOutputStream().close();
    }

    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    public ObjectRestResponse<FreshCompanyConfigDto> getConfig() {
        FreshCompanyConfigDto configDto = freshCompanyConfigService.getConfig();

        return new ObjectRestResponse<>().result(configDto);
    }

    @RequestMapping(value = "/saveConfig", method = RequestMethod.GET)
    public ObjectRestResponse<FreshCompanyConfigDto> saveConfig(FreshCompanyConfigDto dto) {
       /* if(!dto.getUnlimited()) {
            return new ObjectRestResponse<FreshCompanyConfigDto>().rel(false).msg("采集配置未开启");
        }*/
        freshCompanyConfigService.saveConfig(dto);
        return new ObjectRestResponse<FreshCompanyConfigDto>().rel(true);
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

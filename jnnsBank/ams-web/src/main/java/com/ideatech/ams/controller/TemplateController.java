package com.ideatech.ams.controller;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.system.template.dto.TemplateDto;
import com.ideatech.ams.system.template.dto.TemplateSearchDto;
import com.ideatech.ams.system.template.entity.TemplatePo;
import com.ideatech.ams.system.template.service.TemplateService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.CompanyAcctType;
import com.ideatech.common.enums.DepositorType;
import com.ideatech.common.util.FileExtraUtils;
import com.ideatech.common.util.PdfGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * @author liangding
 * @create 2018-07-10 下午10:29
 **/
@RestController
@RequestMapping("/template")
@Slf4j
public class TemplateController extends BaseController<TemplateService, TemplateDto> {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @GetMapping("/page")
    public ResultDto<TemplateSearchDto> page(TemplateSearchDto templateSearchDto) {
        TemplateSearchDto searchDto = getBaseService().search(templateSearchDto);
        return ResultDtoFactory.toAckData(searchDto);
    }

    @PostMapping("/upload")
    public void upload(@Param("file") MultipartFile file, HttpServletResponse response) throws IOException {
        File tempFile;
        try {
            tempFile = File.createTempFile(file.getOriginalFilename(), ".pdf");
            file.transferTo(tempFile);
            HashMap data = new HashMap();
            data.put("fileName", tempFile.getCanonicalPath());
            data.put("displayName", file.getOriginalFilename());
            ResultDto dto =  ResultDtoFactory.toAckData(data);
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        } catch (IOException e) {
            log.error("保存文件失败", e);
            ResultDto dto = ResultDtoFactory.toNack("文件上传失败");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
    }

    @Override
    public ResultDto create(TemplateDto templateDto) {
        String fileName = templateDto.getFileName();
        try (FileInputStream fis = new FileInputStream(new File(fileName))) {
            templateDto.setTemplaeContent(IOUtils.toByteArray(fis));
        } catch (IOException e) {
            log.error("读取模版内容出错", e);
            return ResultDtoFactory.toNack("读取模板内容出错，请重新上传");
        }
        getBaseService().save(templateDto);
        return ResultDtoFactory.toAck();
    }

    @Override
    public ResultDto update(Long id, TemplateDto templateDto) {
        templateDto.setId(id);
        String fileName = templateDto.getFileName();
        try (FileInputStream fis = new FileInputStream(new File(fileName))) {
            templateDto.setTemplaeContent(IOUtils.toByteArray(fis));
        } catch (IOException e) {
            log.error("读取模版内容出错", e);
            return ResultDtoFactory.toNack("读取模板内容出错，请重新上传");
        }
        getBaseService().save(templateDto);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam(name = "id") Long id, HttpServletRequest request) {
        TemplateDto byId = getBaseService().findById(id);
        HttpHeaders headers = new HttpHeaders();
        String fileName = null;
        try {
            fileName = FileExtraUtils.handleFileName(request, "download.pdf");
        } catch (UnsupportedEncodingException e) {
            //ignore
            log.warn("文件名处理出错", e);
        }
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(byId.getTemplaeContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/preview")
    public ResponseEntity<byte[]> preview(@RequestParam(name = "id") Long id, HttpServletRequest request) throws IOException {
        TemplateDto byId = getBaseService().findById(id);
        HttpHeaders headers = new HttpHeaders();
        String fileName = null;
        try {
            fileName = FileExtraUtils.handleFileName(request, "preview.pdf");
        } catch (UnsupportedEncodingException e) {
            //ignore
            log.warn("文件名处理出错", e);
        }
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(PdfGenerator.generate(byId.getTemplaeContent(), new HashMap<String, Object>()), headers, HttpStatus.OK);
    }

    /**
     * 获取打印模版
     * @param billType
     * @param depositorType
     * @return
     */
    @GetMapping("/getTemplateNameList")
    public List<String> getTemplateNameList(BillType billType, DepositorType depositorType,CompanyAcctType acctType) {

        return templateService.listTemplate(billType, depositorType,acctType);
    }

    /**
     * 获取打印模版
     * @param billType
     * @param depositorType
     * @return
     */
    @GetMapping("/getSaicTemplateNameList")
    public List<String> getSaicTemplateNameList(BillType billType, DepositorType depositorType) {

        return templateService.listTemplateName(billType, depositorType);
    }

}

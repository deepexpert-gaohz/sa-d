package com.ideatech.ams.controller;


import com.ideatech.ams.system.proof.dto.ProofReportDto;
import com.ideatech.ams.system.proof.enums.ProofType;
import com.ideatech.ams.system.proof.service.ProofReportService;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.FileExtraUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/proof")
@Slf4j
public class ProofReportController {
    @Value("${ams.image.path}")
    private String filePath;
    @Autowired
    private ProofReportService proofReportService;
    @RequestMapping(value = "/search/kyc")
    public TableResultResponse query(ProofReportDto accountKycReportDto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable){
        List<ProofType> list = new ArrayList<>();
        list.add(ProofType.KYC);
        accountKycReportDto.setTypeList(list);
        return proofReportService.query(accountKycReportDto,pageable);
    }
    @RequestMapping(value = "/search/price")
    public TableResultResponse queryPrice(ProofReportDto accountKycReportDto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable){
        List<ProofType> list = new ArrayList<>();
        list.add(ProofType.PBC);
        list.add(ProofType.SAIC);
        list.add(ProofType.PHONE);
        list.add(ProofType.JUDICIAL_INFORMATION);
        accountKycReportDto.setTypeList(list);
        return proofReportService.query(accountKycReportDto,pageable);
    }
    @RequestMapping(value = "/kyc/pdf")
    public ResponseEntity<byte[]> pdf(ProofReportDto accountKycReportDto, HttpServletRequest request){
        HttpHeaders headers = new HttpHeaders();
        String username = SecurityUtils.getCurrentUsername();
        List<ProofType> list = new ArrayList<>();
        list.add(ProofType.KYC);
        accountKycReportDto.setTypeList(list);
        List<ProofReportDto> reportDtos = (List<ProofReportDto>)proofReportService.searchAll(accountKycReportDto).get("data");
        Map<String,Object> map = new HashMap<String,Object>();
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(reportDtos);
        map.put("name",username);
        map.put("time",DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
        map.put("num",reportDtos.size());
        InputStream inputStream = null;
        String fileName =null;
        String fileName2=null;
        InputStream is = null;
        //OutputStream outputStream = null;
        try {
            fileName = FileExtraUtils.handleFileName(request, "download.pdf");
            String path =creatPath();
            fileName2 = path +File.separator+fileName;
            inputStream = new ClassPathResource("jasperReport"+File.separator+"kyc.jasper").getInputStream();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, jrDataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, fileName2);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            is = new FileInputStream(fileName2);
            byte[] byteArray = IOUtils.toByteArray(is);
            return new ResponseEntity<>(byteArray, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(is);
            if(StringUtils.isNotBlank(fileName2)){
                File file = new File(fileName2);
                if(file.exists()){
                    FileUtils.deleteQuietly(file);
                    log.info("删除客户尽调临时文件");
                }
            }
        }
        return null;
    }
    @RequestMapping(value = "/price/pdf")
    public ResponseEntity<byte[]> pricePdf(ProofReportDto accountKycReportDto, HttpServletRequest request){
        HttpHeaders headers = new HttpHeaders();
        String username = SecurityUtils.getCurrentUsername();
        List<ProofType> list = new ArrayList<>();
        list.add(ProofType.PBC);
        list.add(ProofType.SAIC);
        list.add(ProofType.PHONE);
        accountKycReportDto.setTypeList(list);
        Map<String,Object> mapData = proofReportService.searchAll(accountKycReportDto);
        List<ProofReportDto> reportDtos = (List<ProofReportDto>)mapData.get("data");
        Map<String,Object> map = new HashMap<String,Object>();
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(reportDtos);
        map.put("name",username);
        map.put("time",DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
        map.put("num",mapData.get("totle").toString());
        InputStream inputStream = null;
        String fileName =null;
        String fileName2=null;
        InputStream is = null;
        try {
            fileName = FileExtraUtils.handleFileName(request, "downloadPrice.pdf");
            String path =creatPath();
            fileName2 = path +File.separator+fileName;
            inputStream = new ClassPathResource("jasperReport"+File.separator+"price.jasper").getInputStream();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, jrDataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, fileName2);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            is = new FileInputStream(fileName2);
            byte[] byteArray = IOUtils.toByteArray(is);
            return new ResponseEntity<>(byteArray, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(is);
            if(StringUtils.isNotBlank(fileName2)){
                File file = new File(fileName2);
                if(file.exists()){
                    FileUtils.deleteQuietly(file);
                    log.info("删除第三方数据查询临时文件");
                }
            }
        }
        return null;
    }
    @GetMapping(value = "/export")
    public void export(ProofReportDto accountKycReportDto, HttpServletResponse response) throws IOException {
        IExcelExport iExcelExport = proofReportService.export(accountKycReportDto);
        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(System.currentTimeMillis()+"", "UTF-8")+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",iExcelExport);
        toClient.flush();
        response.getOutputStream().close();

    }
    private String creatPath(){
        String path = filePath+"/temp";
        File file = new File(path);
        if(!file.exists()){
            log.info("开始创建临时文件夹："+path);
            file.mkdirs();
        }
        return path;
    }
}

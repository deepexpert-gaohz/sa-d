package com.ideatech.ams.controller.annual;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.annual.dto.*;
import com.ideatech.ams.annual.executor.*;
import com.ideatech.ams.annual.service.*;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.system.batch.enums.BatchTypeEnum;
import com.ideatech.ams.system.batch.service.BatchService;
import com.ideatech.ams.vo.*;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.excel.util.ImportExcel;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebMethod;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/beneficiary")
@Slf4j
public class BeneficiaryController {

    @Autowired
    private BatchService batchService;

    @Autowired
    private CoreBeneficiarySerivce coreBeneficiarySerivce;

    @Autowired
    private CoreStockHolderService coreStockHolderService;

    @Autowired
    private SaicStockHolderService saicStockHolderService;

    @Autowired
    private SaicBeneficiarySerivce saicBeneficiarySerivce;

    @Autowired
    private TelecomValidationSerivce telecomValidationSerivce;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Value("${ams.batch.beneficiaryNameCompare:false}")
    private Boolean flag;

    @Autowired
    private SaicInfoService saicInfoService;

    @Autowired
    private SaicMonitorService saicMonitorService;

    /**
     * 受益人文件上传
     * @param file
     * @param response
     * @throws IOException
     */
    @PostMapping(value = "/beneficiary/upload")
    @WebMethod(exclude = true)
    public void beneficiaryUpload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        try {
            int headerNum = 1;//标题行号，数据行号=标题行号+1
            ImportExcel importExcel = new ImportExcel(file, headerNum, 0);
            if (importExcel.getRow(0).getPhysicalNumberOfCells() != 13) {
                ResultDtoFactory.toNack("导入失败，错误的模板");
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入失败，错误的模板");
            } else {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start("导入excel数据");
                List<CoreBeneficiaryExcelRowVo> dataList = importExcel.getDataList(CoreBeneficiaryExcelRowVo.class);
                //验证必填项
                for (int i = 0; i < dataList.size(); i++) {
                    CoreBeneficiaryExcelRowVo cberv = dataList.get(i);
                    String message = null;
                    if (StringUtils.isBlank(cberv.getCustomerNo())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“客户号”为必填项";
                    }
                    if (StringUtils.isBlank(cberv.getCustomerName())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“客户名称”为必填项";
                    }
                    if (StringUtils.isBlank(cberv.getAcctNo())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“账号”为必填项";
                    }
                    if (StringUtils.isBlank(cberv.getOrganCode())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“核心机构号”为必填项";
                    }
                    if (message != null) {
                        log.error("导入失败", message);
                        dto.setCode(ResultCode.NACK);
                        dto.setMessage("导入失败，" + message);
                        return;
                    }
                }

                final String batchNo = batchService.createBatch(file.getOriginalFilename(), file.getSize(), (long) dataList.size(), BatchTypeEnum.BENEFICIARY);
                List<CoreBeneficiaryDto> coreBeneficiaryDtos = ConverterService.convertToList(dataList, CoreBeneficiaryDto.class);
                CollectionUtils.forAllDo(coreBeneficiaryDtos, new Closure() {
                    @Override
                    public void execute(Object input) {
                        ((CoreBeneficiaryDto) input).setBatchNo(batchNo);
                    }
                });
                coreBeneficiarySerivce.insert(coreBeneficiaryDtos);
                stopWatch.stop();
                log.info(stopWatch.toString());
                try {
                    BeneficiaryCompareTaskExecutor executor = new BeneficiaryCompareTaskExecutor(batchNo);
                    executor.setUserName(SecurityUtils.getCurrentUsername());
                    executor.setOrganId(SecurityUtils.getCurrentUser().getOrgId());
                    taskExecutor.submit(executor);
                    dto.setCode(ResultCode.ACK);
                    dto.setMessage("导入成功, 请等待后台自动比对结束");
                } catch (Exception e) {
                    log.error("启动比对任务失败", e);
                    dto.setCode(ResultCode.NACK);
                    dto.setMessage("启动比对任务失败");
                }
            }
        } catch (Exception e) {
            log.error("导入失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入失败");
        }finally {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
    }


    /**
     * 受益人上传-只比对受益人名称
     * @param file
     * @param response
     * @throws IOException
     */
    @PostMapping(value = "/beneficiary/uploadName")
    @WebMethod(exclude = true)
    public void beneficiaryUploadName(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        try {
            int headerNum = 1;//标题行号，数据行号=标题行号+1
            ImportExcel importExcel = new ImportExcel(file, headerNum, 0);
            if (importExcel.getRow(0).getPhysicalNumberOfCells() != 9) {
                ResultDtoFactory.toNack("导入失败，错误的模板");
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入失败，错误的模板");
            } else {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start("导入excel数据");
                List<CoreBeneficiaryNameExcelRowVo> dataList = importExcel.getDataList(CoreBeneficiaryNameExcelRowVo.class);
                //验证必填项
                for (int i = 0; i < dataList.size(); i++) {
                    CoreBeneficiaryNameExcelRowVo cberv = dataList.get(i);
                    String message = null;
                    if (StringUtils.isBlank(cberv.getCustomerNo())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“客户号”为必填项";
                    }
                    if (StringUtils.isBlank(cberv.getCustomerName())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“客户名称”为必填项";
                    }
                    if (StringUtils.isBlank(cberv.getAcctNo())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“账号”为必填项";
                    }
                    if (StringUtils.isBlank(cberv.getOrganCode())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“核心机构号”为必填项";
                    }
                    if (message != null) {
                        log.error("导入失败", message);
                        dto.setCode(ResultCode.NACK);
                        dto.setMessage("导入失败，" + message);
                        return;
                    }
                }

                final String batchNo = batchService.createBatch(file.getOriginalFilename(), file.getSize(), (long) dataList.size(), BatchTypeEnum.BENEFICIARYNAME);
                List<CoreBeneficiaryDto> coreBeneficiaryDtos = ConverterService.convertToList(dataList, CoreBeneficiaryDto.class);
                CollectionUtils.forAllDo(coreBeneficiaryDtos, new Closure() {
                    @Override
                    public void execute(Object input) {
                        ((CoreBeneficiaryDto) input).setBatchNo(batchNo);
                    }
                });
                coreBeneficiarySerivce.insert(coreBeneficiaryDtos);
                stopWatch.stop();
                log.info(stopWatch.toString());
                try {
                    BeneficiaryNameCompareTaskExecutor executor = new BeneficiaryNameCompareTaskExecutor(batchNo,flag);
                    executor.setSaicInfoService(saicInfoService);
                    taskExecutor.submit(executor);
                    dto.setCode(ResultCode.ACK);
                    dto.setMessage("导入成功, 请等待后台自动比对结束");
                } catch (Exception e) {
                    log.error("启动比对任务失败", e);
                    dto.setCode(ResultCode.NACK);
                    dto.setMessage("启动比对任务失败");
                }
            }
        } catch (Exception e) {
            log.error("导入失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入失败");
        }finally {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
    }

    /**
     * 受益人文件上传（采集工商受益人信息）
     * @param file
     * @param response
     * @throws IOException
     */
    @PostMapping(value = "/beneficiary/uploadCollect")
    @WebMethod(exclude = true)
    public void beneficiaryUploadCollect(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        try {
            int headerNum = 1;//标题行号，数据行号=标题行号+1
            ImportExcel importExcel = new ImportExcel(file, headerNum, 0);
            if (importExcel.getRow(0).getPhysicalNumberOfCells() != 4) {
                ResultDtoFactory.toNack("导入失败，错误的模板");
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入失败，错误的模板");
            } else {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start("导入excel数据");
                List<BeneficiaryCollectExcelRowVo> dataList = importExcel.getDataList(BeneficiaryCollectExcelRowVo.class);

                //验证必填项
                for (int i = 0; i < dataList.size(); i++) {
                    BeneficiaryCollectExcelRowVo cberv = dataList.get(i);
                    String message = null;
                    if (StringUtils.isBlank(cberv.getCustomerNo())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“客户号”为必填项";
                    }
                    if (StringUtils.isBlank(cberv.getCustomerName())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“客户名称”为必填项";
                    }
                    if (StringUtils.isBlank(cberv.getAcctNo())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“账号”为必填项";
                    }
                    if (StringUtils.isBlank(cberv.getOrganCode())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“核心机构号”为必填项";
                    }
                    if (message != null) {
                        log.error("导入失败", message);
                        dto.setCode(ResultCode.NACK);
                        dto.setMessage("导入失败，" + message);
                        return;
                    }
                }

                //创建批量任务
                final String batchNo = batchService.createBatch(file.getOriginalFilename(), file.getSize(), (long) dataList.size(), BatchTypeEnum.BENEFICIARYCOLLECT);
                List<CoreBeneficiaryDto> coreBeneficiaryDtos = ConverterService.convertToList(dataList, CoreBeneficiaryDto.class);
                CollectionUtils.forAllDo(coreBeneficiaryDtos, new Closure() {
                    @Override
                    public void execute(Object input) {
                        ((CoreBeneficiaryDto) input).setBatchNo(batchNo);
                    }
                });
                coreBeneficiarySerivce.insert(coreBeneficiaryDtos);
                stopWatch.stop();
                log.info(stopWatch.toString());
                try {
                    BeneficiaryCollectionTaskExecutor executor = new BeneficiaryCollectionTaskExecutor(batchNo);
                    executor.setUserName(SecurityUtils.getCurrentUsername());
                    executor.setOrganId(SecurityUtils.getCurrentUser().getOrgId());
                    taskExecutor.submit(executor);
                    dto.setCode(ResultCode.ACK);
                    dto.setMessage("导入成功, 请等待后台自动采集结束");
                } catch (Exception e) {
                    log.error("启动采集任务失败", e);
                    dto.setCode(ResultCode.NACK);
                    dto.setMessage("启动采集任务失败");
                }
            }
        } catch (Exception e) {
            log.error("导入失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入失败");
        }finally {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
    }

    /**
     * 电信运营商文件上传
     * @param file
     * @param response
     * @throws IOException
     */
    @PostMapping(value = "/telecom/upload")
    @WebMethod(exclude = true)
    public void telecomUpload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        try {
            int headerNum = 0;//标题行号，数据行号=标题行号+1
            ImportExcel importExcel = new ImportExcel(file, headerNum, 0);
            if (importExcel.getRow(0).getPhysicalNumberOfCells() != 7) {
                ResultDtoFactory.toNack("导入失败，错误的模板");
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入失败，错误的模板");
            } else {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start("导入excel数据");
                List<TelecomValidationExcelRowVo> dataList = importExcel.getDataList(TelecomValidationExcelRowVo.class);
                //验证必填项
                for (int i = 0; i < dataList.size(); i++) {
                    TelecomValidationExcelRowVo tverv = dataList.get(i);
                    String message = null;
                    if (StringUtils.isBlank(tverv.getCustomerNo())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“客户号”为必填项";
                    }
                    if (StringUtils.isBlank(tverv.getCustomerName())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“客户名称”为必填项";
                    }
                    if (StringUtils.isBlank(tverv.getAcctNo())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“账号”为必填项";
                    }
                    if (StringUtils.isBlank(tverv.getBankCode())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“核心机构号”为必填项";
                    }
                    if (StringUtils.isBlank(tverv.getName())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“姓名”为必填项";
                    }
                    if (StringUtils.isBlank(tverv.getIdNo())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“身份证号”为必填项";
                    }
                    if (StringUtils.isBlank(tverv.getMobile())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“手机号”为必填项";
                    }
                    if (message != null) {
                        log.error("导入失败", message);
                        dto.setCode(ResultCode.NACK);
                        dto.setMessage("导入失败，" + message);
                        return;
                    }
                }
                final String batchNo = batchService.createBatch(file.getOriginalFilename(), file.getSize(), (long) dataList.size(), BatchTypeEnum.TELECOM_3EL);
                List<TelecomValidationDto> telecomValidationDtos = ConverterService.convertToList(dataList, TelecomValidationDto.class);
                CollectionUtils.forAllDo(telecomValidationDtos, new Closure() {
                    @Override
                    public void execute(Object input) {
                        ((TelecomValidationDto) input).setBatchNo(batchNo);
                    }
                });
                telecomValidationSerivce.insert(telecomValidationDtos);
                stopWatch.stop();
                log.info(stopWatch.toString());
                try {
                    taskExecutor.submit(new TelecomValidationTaskExecutor(batchNo));
                    dto.setCode(ResultCode.ACK);
                    dto.setMessage("导入成功, 请等待后台自动校验结束");
                } catch (Exception e) {
                    log.error("启动比对任务失败", e);
                    dto.setCode(ResultCode.NACK);
                    dto.setMessage("启动比对任务失败");
                }
            }
        } catch (Exception e) {
            log.error("导入失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入失败");
        }finally {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
    }

    /**
     * 控股股东文件上传
     * @param file
     * @param response
     * @throws IOException
     */
    @PostMapping(value = "/stockholder/upload")
    @WebMethod(exclude = true)
    public void stockholderUpload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        try {
            int headerNum = 1;//标题行号，数据行号=标题行号+1
            ImportExcel importExcel = new ImportExcel(file, headerNum, 0);
            if (importExcel.getRow(0).getPhysicalNumberOfCells() != 6) {
                ResultDtoFactory.toNack("导入失败，错误的模板");
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入失败，错误的模板");
            } else {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start("导入excel数据");
                List<CoreStockHolderExcelRowVo> dataList = importExcel.getDataList(CoreStockHolderExcelRowVo.class);
                //验证必填项
                for (int i = 0; i < dataList.size(); i++) {
                    CoreStockHolderExcelRowVo csherv = dataList.get(i);
                    String message = null;
                    if (StringUtils.isBlank(csherv.getCustomerNo())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“客户号”为必填项";
                    }
                    if (StringUtils.isBlank(csherv.getCustomerName())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“客户名称”为必填项";
                    }
                    if (StringUtils.isBlank(csherv.getAcctNo())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“账号”为必填项";
                    }
                    if (StringUtils.isBlank(csherv.getOrganCode())) {
                        message = "第" + (headerNum + 1 + i + 1) + "行“核心机构号”为必填项";
                    }
                    if (message != null) {
                        log.error("导入失败", message);
                        dto.setCode(ResultCode.NACK);
                        dto.setMessage("导入失败，" + message);
                        return;
                    }
                }
                final String batchNo = batchService.createBatch(file.getOriginalFilename(), file.getSize(), (long) dataList.size(), BatchTypeEnum.STOCKHOLDER);
                List<CoreStockHolderDto> coreStockHolderDtos = ConverterService.convertToList(dataList, CoreStockHolderDto.class);
                CollectionUtils.forAllDo(coreStockHolderDtos, new Closure() {
                    @Override
                    public void execute(Object input) {
                        ((CoreStockHolderDto) input).setBatchNo(batchNo);
                    }
                });
                coreStockHolderService.insert(coreStockHolderDtos);
                stopWatch.stop();
                log.info(stopWatch.toString());
                dto.setCode(ResultCode.ACK);
                dto.setMessage("导入成功, 请等待后台自动比对结束");
                StockHolderCompareTaskExecutor executor = new StockHolderCompareTaskExecutor(batchNo);
                executor.setUserName(SecurityUtils.getCurrentUsername());
                executor.setOrganId(SecurityUtils.getCurrentUser().getOrgId());
                executor.setCoreStockHolderService(coreStockHolderService);
                executor.setSaicStockHolderService(saicStockHolderService);
                executor.setBatchService(batchService);
                executor.setSaicInfoService(saicInfoService);
                executor.setSaicMonitorService(saicMonitorService);
                taskExecutor.submit(executor);
            }
        } catch (Exception e) {
            log.error("导入失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("导入失败");
        }finally {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
    }

    @RequestMapping("/stockholders")
    public ResultDto listStockHolderResult(SaicStockHolderSearchDto searchDto, PagingDto pagingDto) {
        PagingDto<SaicStockHolderDto> saicStockHolderDtoPagingDto = saicStockHolderService.query(searchDto, pagingDto);
        return ResultDtoFactory.toAckData(saicStockHolderDtoPagingDto);
    }

    @RequestMapping("/beneficiary")
    public ResultDto listBeneficiaryResult(SaicBeneficiarySearchDto searchDto, PagingDto pagingDto) {
        PagingDto<SaicBeneficiaryDto> saicBeneficiaryDtoPagingDto = saicBeneficiarySerivce.query(searchDto, pagingDto);
        return ResultDtoFactory.toAckData(saicBeneficiaryDtoPagingDto);
    }

    @RequestMapping("/telecom")
    public ResultDto listTelecomResult(TelecomValidationSearchDto searchDto, PagingDto pagingDto) {
        PagingDto<TelecomValidationDto> telecomValidationDtoPagingDto = telecomValidationSerivce.query(searchDto, pagingDto);
        return ResultDtoFactory.toAckData(telecomValidationDtoPagingDto);
    }

    /**
     * 根据查询条件导出excel（受益人比对数据）
     * @param searchDto
     */
    @RequestMapping("/beneficiary/exportExcel")
    public void exportBeneficiaryExcel(SaicBeneficiarySearchDto searchDto, HttpServletResponse response) {
        List<SaicBeneficiaryDto> sbdList = saicBeneficiarySerivce.queryAll(searchDto);
        HSSFWorkbook wb = saicBeneficiarySerivce.exportExcel(sbdList);
        response.setHeader("Content-Disposition", "attachment;filename=beneficiary_export.xls");//指定下载的文件名
        response.setContentType("application/vnd.ms-excel");
        try {
            OutputStream fileOut = response.getOutputStream();
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据查询条件导出excel（受益人比对数据）
     * @param searchDto
     */
    @RequestMapping("/beneficiary/beneficiaryNameExportExcel")
    public void exportBeneficiaryNameExportExcel(SaicBeneficiarySearchDto searchDto, HttpServletResponse response) {
        List<SaicBeneficiaryDto> sbdList = saicBeneficiarySerivce.queryAll(searchDto);
        HSSFWorkbook wb = saicBeneficiarySerivce.exportBeneficiaryNameExcel(sbdList);
        response.setHeader("Content-Disposition", "attachment;filename=beneficiary_name_export.xls");//指定下载的文件名
        response.setContentType("application/vnd.ms-excel");
        try {
            OutputStream fileOut = response.getOutputStream();
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据查询条件导出excel（受益人采集数据）
     * @param searchDto
     */
    @RequestMapping("/beneficiary/exportCollectExcel")
    public void exportBeneficiaryExcel2(SaicBeneficiarySearchDto searchDto, HttpServletResponse response) {
        List<SaicBeneficiaryDto> sbdList = saicBeneficiarySerivce.queryAll(searchDto);
        HSSFWorkbook wb = saicBeneficiarySerivce.exportCollectExcel(sbdList);
        response.setHeader("Content-Disposition", "attachment;filename=beneficiary_collect_export.xls");//指定下载的文件名
        response.setContentType("application/vnd.ms-excel");
        try {
            OutputStream fileOut = response.getOutputStream();
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据查询条件导出excel（控股股东比对数据）
     * @param searchDto
     */
    @RequestMapping("/stockholders/exportExcel")
    public void exportStockholdersExcel(SaicStockHolderSearchDto searchDto, HttpServletResponse response) {
        List<SaicStockHolderDto> sshdList = saicStockHolderService.queryAll(searchDto);
        HSSFWorkbook wb = saicStockHolderService.exportExcel(sshdList);
        response.setHeader("Content-Disposition", "attachment;filename=stockholders_export.xls");//指定下载的文件名
        response.setContentType("application/vnd.ms-excel");
        try {
            OutputStream fileOut = response.getOutputStream();
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据查询条件导出excel（电信三要素校验数据）
     * @param searchDto
     */
    @RequestMapping("/telecom/exportExcel")
    public void exportTelecomExcel(TelecomValidationSearchDto searchDto, HttpServletResponse response) {
        List<TelecomValidationDto> tvdList = telecomValidationSerivce.queryAll(searchDto);
        HSSFWorkbook wb = telecomValidationSerivce.exportExcel(tvdList);
        response.setHeader("Content-Disposition", "attachment;filename=telecom_export.xls");//指定下载的文件名
        response.setContentType("application/vnd.ms-excel");
        try {
            OutputStream fileOut = response.getOutputStream();
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

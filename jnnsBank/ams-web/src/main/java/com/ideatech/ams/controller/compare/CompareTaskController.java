package com.ideatech.ams.controller.compare;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.compare.dto.*;
import com.ideatech.ams.compare.enums.CompareState;
import com.ideatech.ams.compare.executor.CompareCollectControllerExecutor;
import com.ideatech.ams.compare.executor.CompareResetCollectControllerExecutor;
import com.ideatech.ams.compare.executor.CompareTaskCompareExecutor;
import com.ideatech.ams.compare.processor.OnlineCollectionProcessor;
import com.ideatech.ams.compare.service.*;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.ApplicationContextUtil;
import com.ideatech.common.util.BrowserUtil;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/compareTask")
@Slf4j
public class CompareTaskController {
    @Autowired
    private CompareTaskService compareTaskService;

    @Autowired
    CompareRuleFieldsService compareRuleFieldsService;

    @Autowired
    CompareFieldService compareFieldService;

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    CompareDataService compareDataService;

    @Autowired
    private ThreadPoolTaskExecutor compareExecutor;

    @Autowired
    private CompareRuleDataSourceService compareRuleDataSourceService;

    @PostMapping("/")
    public ResultDto save(CompareTaskDto dto) {
        compareTaskService.saveTask(dto);
        return ResultDtoFactory.toAck();
    }

    @DeleteMapping("/{id}")
    public ResultDto del(@PathVariable("id") Long id) {
        compareTaskService.deleteTask(id);
        return ResultDtoFactory.toAck();
    }

    @PutMapping("/{id}")
    public ResultDto update(@PathVariable("id") Long id, CompareTaskDto dto) {
        dto.setId(id);
        compareTaskService.saveTask(dto);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/{id}")
    public ResultDto<CompareTaskDto> findById(@PathVariable(name = "id") Long id) {
        CompareTaskDto dto = compareTaskService.findById(id);
        return ResultDtoFactory.toAckData(dto);
    }

    @GetMapping("/list")
    public ResultDto list(CompareTaskSearchDto dto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDtoFactory.toAckData(compareTaskService.search(dto, pageable));
    }


    /**
     * 核心文件上传
     *
     * @param file
     * @param response
     * @throws IOException
     */
    @PostMapping(value = "/upload")
    @WebMethod(exclude = true)
    public void upload(Long taskId, Long dataSourceId, @RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        try {
            compareTaskService.importData(taskId, dataSourceId, file);
            dto.setCode(ResultCode.ACK);
            dto.setMessage("导入比对数据成功");
            log.info("返回数据到前端");
        } catch (Exception e) {
            log.error("导入比对数据失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage(e.getMessage());
        } finally {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
    }

    /**
     * 在线采集
     *
     * @param taskId
     * @param dataSourceId
     * @throws IOException
     */
    @PostMapping(value = "/onlineCollect")
    public ResultDto onlineCollect(Long taskId, Long dataSourceId) throws IOException {
        ResultDto dto = new ResultDto();
        Map<Long, CompareRuleDataSourceDto> compareRuleDataSourceDtoMap = compareTaskService.findCompareRuleDataSourceDtoMapByTaskId(taskId);
        if (!compareRuleDataSourceDtoMap.containsKey(dataSourceId)) {
            log.error("任务[{}]无法对应数据源数据[{}]", taskId, dataSourceId);
            return ResultDtoFactory.toNack("任务无法对应数据源数据");
        }
        DataSourceDto dataSourceDto = dataSourceService.findById(dataSourceId);
        if (dataSourceDto == null) {
            log.error("无对应任务[{}]的数据源数据[{}]", taskId, dataSourceId);
            return ResultDtoFactory.toNack("无对应任务的数据源数据");
        }
        try {
            OnlineCollectionProcessor onlineCollectionProcessor = (OnlineCollectionProcessor) ApplicationContextUtil.getBean(StringUtils.lowerCase(dataSourceDto.getCode()) + "OnlineCollectionProcessor");
            if (onlineCollectionProcessor.checkCollectTask(taskId, dataSourceDto)) {
                CompareCollectControllerExecutor compareCollectControllerExecutor = new CompareCollectControllerExecutor();
                compareCollectControllerExecutor.setTaskId(taskId);
                compareCollectControllerExecutor.setDataSourceDto(dataSourceDto);
                compareCollectControllerExecutor.setOnlineCollectionProcessor(onlineCollectionProcessor);
                compareExecutor.submit(compareCollectControllerExecutor);
            }
            return ResultDtoFactory.toAckData(dto);
        } catch (EacException ex) {
            log.error("在线采集启动异常:" + ex.getMessage());
            return ResultDtoFactory.toNack(ex.getMessage());
        } catch (Exception e) {
            log.error("在线采集启动异常:" + e.getMessage());
            return ResultDtoFactory.toNack("在线采集启动异常");
        }
    }


    /**
     * 在线重新采集
     *
     * @param taskId
     * @param dataSourceId
     * @throws IOException
     */
    @PostMapping(value = "/onlineResetCollect")
    public ResultDto onlineResetCollect(Long taskId, Long dataSourceId) throws IOException {
        ResultDto dto = new ResultDto();
        Map<Long, CompareRuleDataSourceDto> compareRuleDataSourceDtoMap = compareTaskService.findCompareRuleDataSourceDtoMapByTaskId(taskId);
        if (!compareRuleDataSourceDtoMap.containsKey(dataSourceId)) {
            log.error("任务[{}]无法对应数据源数据[{}]", taskId, dataSourceId);
            return ResultDtoFactory.toNack("任务无法对应数据源数据");
        }
        DataSourceDto dataSourceDto = dataSourceService.findById(dataSourceId);
        if (dataSourceDto == null) {
            log.error("无对应任务[{}]的数据源数据[{}]", taskId, dataSourceId);
            return ResultDtoFactory.toNack("无对应任务的数据源数据");
        }
        try {
            OnlineCollectionProcessor onlineCollectionProcessor = (OnlineCollectionProcessor) ApplicationContextUtil.getBean(StringUtils.lowerCase(dataSourceDto.getCode()) + "OnlineCollectionProcessor");
            if (onlineCollectionProcessor.checkResetCollectTask(taskId, dataSourceDto)) {
                CompareResetCollectControllerExecutor compareCollectControllerExecutor = new CompareResetCollectControllerExecutor();
                compareCollectControllerExecutor.setTaskId(taskId);
                compareCollectControllerExecutor.setDataSourceDto(dataSourceDto);
                compareCollectControllerExecutor.setOnlineCollectionProcessor(onlineCollectionProcessor);
                compareExecutor.submit(compareCollectControllerExecutor);
            }
            return ResultDtoFactory.toAckData(dto);
        } catch (EacException ex) {
            log.error("在线采集启动异常:" + ex.getMessage());
            return ResultDtoFactory.toNack(ex.getMessage());
        } catch (Exception e) {
            log.error("在线采集启动异常:" + e.getMessage());
            return ResultDtoFactory.toNack("在线采集启动异常");
        }
    }

    /**
     * 比对任务管理详情查询
     */

    @GetMapping(value = "/getCompareTaskDetails")
    public CompareTaskDto getCompareTaskDetails(Long taskId) {
        CompareTaskDto compareTaskDto = compareTaskService.getDetails(taskId);
        return compareTaskDto;
    }

//    /**
//     * 比对任务管理比对数据实时查询更新
//     */
//
//    @GetMapping(value = "/getCompareTaskDetails1")
//    public CompareTaskDto getCompareTaskDetails1(Long taskId) {
//        CompareTaskDto compareTaskDto = compareTaskService.findByTaskIdFromApplication(taskId);
//        return compareTaskDto;
//    }

    /**
     * 比对任务规则详情查询
     */
    @GetMapping(value = "/getCompareRuleDetails")
    public CompareRuleDto getCompareRuleDetails(Long taskId) {
        CompareRuleDto compareRuleDto = compareTaskService.getCompareRuleDetails(taskId);
        return compareRuleDto;
    }

    /**
     * 比对任务规则详情查询
     */
    @GetMapping(value = "/getCompareDataSourceDetails")
    public JSONArray getCompareDataSourceDetails(Long taskId) {
        JSONArray jsonArray = compareTaskService.getCompareDataSourceDetails(taskId);
        return jsonArray;
    }

    /**
     * 比对数据导入统计
     */
    @GetMapping(value = "/getCompareTaskCount")
    public String getCompareTaskCount(Long taskId) throws Exception {
        String compareCount = compareTaskService.getCompareTaskCount(taskId);
        return compareCount;
    }

    /**
     * 工商数据采集导出钱先判断是否有其他数据源的数据为依据
     *
     * @param taskId
     * @return
     */
    @GetMapping(value = "/checkDataSourceImporter")
    public ResultDto checkDataSourceImporter(Long taskId) {
        ResultDto resultDto = compareTaskService.checkDataSourceImporter(taskId);
        return resultDto;

    }


    /**
     * 工商数据手工导入，导出企业名称等详细列表
     *
     * @param taskId
     */
    @GetMapping(value = "/export")
    public void exportSaicCollectData(Long taskId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("导出工商采集明细" + taskId);
        IExcelExport exportSaicCompareData = compareTaskService.exportSaicCompareData(taskId);
        StringBuilder fileName = new StringBuilder();
        fileName.append(new Date().getTime());
        response.setHeader("Content-disposition", "attachment; filename=" + generateFileName("年检结果统计", request) + ".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(), "yyyy-MM-dd", exportSaicCompareData);
        toClient.flush();
        response.getOutputStream().close();
    }


    /**
     * 导入详情表头JSONArray
     *
     * @param taskId
     * @return
     */
    @GetMapping(value = "/dataDetailColumns")
    public JSONObject dataDetailColumns(Long taskId) {
        JSONObject jsonObject = compareTaskService.dataDetailColumns(taskId);
        return jsonObject;
    }


    /**
     * 导入明细
     *
     * @param taskId
     * @param dataSourceId
     * @return
     */
    @GetMapping(value = "/dataDetailList")
    public TableResultResponse<JSONObject> dataDetailList(Long taskId, Long dataSourceId, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        TableResultResponse<JSONObject> tableResultResponse = compareTaskService.dataDetailList(taskId, dataSourceId, pageable);
        return tableResultResponse;
    }


    /**
     * 导入结果查看
     *
     * @return
     */
    @GetMapping(value = "/importDetails")
    public TableResultResponse<CompareCollectRecordDto> importDetailList(Long dataSourceId, CompareCollectRecordDto compareCollectRecordDto, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        DataSourceDto dataSourceDto = dataSourceService.findById(dataSourceId);
        compareCollectRecordDto.setDataSourceType(dataSourceDto.getCode());
        TableResultResponse<CompareCollectRecordDto> tableResultResponse = compareTaskService.importDetailList(compareCollectRecordDto, pageable);
        return tableResultResponse;
    }


    /**
     * 生成导出文件名
     *
     * @param
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    private String generateFileName(String fileName1, HttpServletRequest request) throws UnsupportedEncodingException {
        StringBuilder fileName = new StringBuilder();
        fileName.append(BrowserUtil.generateFileName(fileName1, request))
                .append("-").append(DateUtils.DateToStr(new Date(), "yyyy-MM-dd"));
        return DateUtils.DateToStr(new Date(), "yyyy-MM-dd");
    }

    /**
     * 根据taskId开始比对
     * @param taskId
     * @return
     */
    @GetMapping(value = "/doCompare")
    public String doCompare(Long taskId) {
        CompareTaskCompareExecutor compareTaskCompareExecutor = new CompareTaskCompareExecutor();
        compareTaskCompareExecutor.setCompareTaskService(compareTaskService);
        compareTaskCompareExecutor.setTaskId(taskId);
        compareExecutor.submit(compareTaskCompareExecutor);
        return "比对开始，程序正在后台执行......";
    }


    /**
     * 重置
     * @return
     */
    @GetMapping(value = "/comapreReset")
    public ResultDto comapreReset(Long taskId){
        ResultDto resultDto = compareTaskService.comapreReset(taskId);
        return resultDto;
    }


    /**
     * 比对线程停止
     */
    @GetMapping(value = "/compareShutDown")
    public ResultDto compareShutDown(Long taskId){
        ResultDto resultDto = compareTaskService.compareShutDown(taskId);
        return resultDto;
    }


    /**
     * 点击比对时，校验比对数据源是否有数据  没有数据的数据源进行提示导入或采集数据
     * @param taskId
     * @return
     */
    @GetMapping(value = "/doCompareBefore")
    public ResultDto doCompareBefore(Long taskId){
        ResultDto resultDto = compareTaskService.doCompareBefore(taskId);
        return resultDto;
    }


    /**
     * 根据当前用户跟创建任务的用户做比较，如果不相等就不能有操作的权限
     * @param taskId
     * @return
     */
    @GetMapping(value = "/checkCompareTaskUser")
    public ResultDto checkCompareTaskUser(Long taskId){
        ResultDto resultDto = compareTaskService.checkCompareTaskUser(taskId);
        return resultDto;
    }
}

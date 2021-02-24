package com.ideatech.ams.controller.annual;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.annual.dto.*;
import com.ideatech.ams.annual.dto.poi.AnnualCorePoi;
import com.ideatech.ams.annual.enums.*;
import com.ideatech.ams.annual.executor.CollectControllerExecutor;
import com.ideatech.ams.annual.service.*;
import com.ideatech.ams.annual.service.export.AnnualResultExportService;
import com.ideatech.ams.annual.vo.CoreCollectionExcelRowVo;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.pbc.utils.NumberUtils;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.trace.aop.OperateLog;
import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.dto.TreeTable;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.ImportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.util.BrowserUtil;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import jxl.Sheet;
import jxl.Workbook;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.jws.WebMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/annualTask")
@Slf4j
public class AnnualTaskController {

    @Autowired
    private AnnualTaskService annualTaskService;

    @Autowired
    private CompareFieldsService compareFieldsService;

    @Autowired
    private CompareRuleService compareRuleService;

    @Autowired
    private CoreCollectionService coreCollectionService;

    @Autowired
    private CollectTaskService collectTaskService;

    @Autowired
    private CollectConfigService collectConfigService;

    @Autowired
    private PbcCollectionService pbcCollectionService;

    @Autowired
    private FetchPbcInfoService fetchPbcInfoService;

    @Autowired
    private SaicCollectionService saicCollectionService;

    @Autowired
    private FetchSaicInfoService fetchSaicInfoService;

    @Autowired
    private PbcCollectAccountService pbcCollectAccountService;

    @Autowired
    private PbcCollectOrganService pbcCollectOrganService;

    @Autowired
    private ThreadPoolTaskExecutor annualExecutor;

    @Autowired
    private CollectTaskErrorMessageService collectTaskErrorMessageService;

    @Autowired
    private AnnualResultService annualResultService;

    @Autowired
    private AnnualResultExportService annualResultExportService;

    @Value("${ams.annual.saic-file-location}")
    private String saicFileLocation;

    @Autowired
    private PbcAccountService pbcAccountService;



    /**
     * 获得当前年检任务id
     * @return
     */
    @RequestMapping(value = "/getAnnualTaskId", method = RequestMethod.GET)
    public String getAnnualTaskId() {
        Long taskId = annualTaskService.getAnnualCompareTaskId();
        if (taskId == null) {
            return "";
        } else {
            return taskId.toString();
        }

    }

    /**
     * 年检结果统计详情(异步加载)
     * @param pid 当前机构id
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/statistics/{taskId}", method = RequestMethod.GET)
    public TreeTable query(Long pid, @PathVariable Long taskId) {
        Long organId = null;
        if (pid == null) {
            organId = SecurityUtils.getCurrentUser().getOrgId();
        }
        return annualTaskService.getStatisticsInfo(pid, organId, taskId);
    }

    /**
     * 年检结果统计详情导出
     * @param taskId
     * @return
     */
    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.EXPORT,operateContent = "导出年检统计")
    @RequestMapping(value = "/statistics/export/{taskId}", method = RequestMethod.GET)
    public void exportXLS(@PathVariable Long taskId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long organId = SecurityUtils.getCurrentUser().getOrgId();
        IExcelExport iExcelExportExcel = annualTaskService.exportXLS(organId, taskId);

        response.setHeader("Content-disposition", "attachment; filename="+generateFileName("年检结果统计",request)+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",iExcelExportExcel);
        toClient.flush();
        response.getOutputStream().close();
    }

    /**
     * 年检结果统计详情(同步加载)
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/statisticss/{taskId}", method = RequestMethod.GET)
    public ResultDto querys(@PathVariable Long taskId) {
        Long organId = SecurityUtils.getCurrentUser().getOrgId();

        return ResultDtoFactory.toAckData(annualTaskService.getStatisticsInfos(organId, taskId));
    }


    /**
     * 年检人行数据列表-需要包含分页
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/pbcdata",method = RequestMethod.GET)
    public ResultDto bankdata(PbcCollectResultSearchDto pbcCollectResultSearchDto) throws IOException {
        Long taskId = annualTaskService.getAnnualCompareTaskId();
        pbcCollectResultSearchDto = pbcCollectAccountService.search(pbcCollectResultSearchDto, taskId);
        return ResultDtoFactory.toAckData(pbcCollectResultSearchDto);
    }


    /**
     * 人行异常展示
     * @return
     */
    @RequestMapping(value = "/pbc/errorMessage",method = RequestMethod.GET)
    public ResultDto pbcErrorMessage(CollectErrorMessageSearchDto collectErrorMessageSearchDto) {
        Long annualTaskId = annualTaskService.initAnnualTask();
        CollectTaskDto pbcTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.PBC, annualTaskId);
        if(pbcTaskDto != null){
            Long taskId = pbcTaskDto.getId();
            CollectErrorMessageSearchDto search = collectTaskErrorMessageService.search(collectErrorMessageSearchDto, taskId, annualTaskId);
            return ResultDtoFactory.toAckData(search);
        }else{
            return ResultDtoFactory.toAckData(null);

        }
    }

    /**
     * 年检任务状态
     * @return
     */
    @RequestMapping(value = "/datastatus",method = RequestMethod.GET)
    public ObjectRestResponse dataStatus() {
        JSONObject jsonObject = new JSONObject();
        //核心数据状态
        JSONObject coreStatus = new JSONObject();
        Long taskId = annualTaskService.initAnnualTask();
        CollectTaskDto coreTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.CORE, taskId);
        if(coreTaskDto == null){
            coreStatus.put("currentStatus","-2");
            coreStatus.put("dataTotal","0");
            coreStatus.put("dataComplete","0");
            coreStatus.put("dataFail","0");
            coreStatus.put("error","");
        }else{
            coreStatus.put("currentStatus",coreTaskDto.getCollectStatus().getFullName());
            coreStatus.put("dataTotal",coreTaskDto.getCount());
            coreStatus.put("dataComplete",coreTaskDto.getSuccessed());
            coreStatus.put("dataFail",coreTaskDto.getFailed());
            coreStatus.put("error",coreTaskDto.getExceptionReason());
        }
//        // -1 未开始 0 正在采集 1 导入成功
//        coreStatus.put("currentStatus","1");

        //人行状态
        JSONObject bankStatus = new JSONObject();
        // -1 未开始 0 正在采集 1 采集成功 2 采集暂停

        CollectTaskDto pbcTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.PBC, taskId);
        if(pbcTaskDto == null){
            bankStatus.put("currentStatus","-1");
            bankStatus.put("dataTotal","0");
            bankStatus.put("dataComplete","0");
            bankStatus.put("dataNoNeed","0");
            bankStatus.put("dataFail","0");
            bankStatus.put("isNeedConfig",true);
            bankStatus.put("error","");
        }else{
            bankStatus.put("currentStatus",pbcTaskDto.getCollectStatus().getFullName());
            bankStatus.put("dataTotal",pbcTaskDto.getCount());
            bankStatus.put("dataComplete",pbcTaskDto.getSuccessed());
            bankStatus.put("dataNoNeed",pbcTaskDto.getProcessed());
            bankStatus.put("dataFail",pbcTaskDto.getFailed());
            bankStatus.put("error",pbcTaskDto.getExceptionReason());
            bankStatus.put("isNeedConfig",false);
        }

        //工商配置
        JSONObject saicStatus = new JSONObject();

        CollectTaskDto saicTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.SAIC, taskId);
        // -1 未开始 0 正在采集 1 采集成功 2 采集暂停  3 工商文件下载 4 在线工商启动
        if(saicTaskDto == null){
            int status = saicCollectionService.checkSaicCollectTaskStatus(taskId);
            if(status>=3 && status<=4){
                saicStatus.put("currentStatus",status+"");
            }else{
                saicStatus.put("currentStatus","-2");
            }
            saicStatus.put("dataTotal","0");
            saicStatus.put("dataComplete","0");
            saicStatus.put("dataFail","0");
            saicStatus.put("isNeedConfig",true);
        }else{
            saicStatus.put("currentStatus",saicTaskDto.getCollectStatus().getFullName());
            saicStatus.put("dataTotal",saicTaskDto.getCount());
            saicStatus.put("dataComplete",saicTaskDto.getSuccessed());
            saicStatus.put("dataFail",saicTaskDto.getFailed());
            saicStatus.put("isNeedConfig",false);
        }

        jsonObject.put("core",coreStatus);
        jsonObject.put("bank",bankStatus);
        jsonObject.put("saic",saicStatus);


		AnnualTaskDto annualTaskDto = annualTaskService.findById(taskId);
		if (annualTaskDto.getStatus() == TaskStatusEnum.PROCESSING) {
			//更新条数
			annualTaskService.updateNumByTask(taskId);
		}

        //年检比对状态 0 未开始 2 正在进行中 3 完成 4 暂停
		annualTaskDto = annualTaskService.findById(taskId);
        jsonObject.put("status",annualTaskDto.getStatus().ordinal());
        jsonObject.put("dataTotal",annualTaskDto.getSum());
        jsonObject.put("dataComplete",annualTaskDto.getProcessedNum());
        jsonObject.put("taskId",taskId);

        return new ObjectRestResponse<String>().rel(true).msg("成功").result(jsonObject);
    }


    /**
     * 核心文件上传
     * @param file
     * @param response
     * @throws IOException
     */
    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.IMPORT,operateContent = "核心文件上传")
    @PostMapping(value = "/upload")
    @WebMethod(exclude = true)
    public void upload(@RequestParam("file") MultipartFile file,HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();

        String reUpload = request.getParameter("reUpload");
        Long annualTaskId = annualTaskService.initAnnualTask();
        //检查之前的采集任务是否完成
        CollectTaskDto collectTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum.CORE, annualTaskId);
        if (collectTaskDto != null) {
            dto.setCode(ResultCode.NACK);
            dto.setMessage("该数据源数据未（导入/采集）完成，请先重置！");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
            return;
        }

        try {
            List<CoreCollectionExcelRowVo> excelRowVoList = new ArrayList<>(16);
            //循环读取sheet内容
            CommonsMultipartFile cf= (CommonsMultipartFile)file;
            DiskFileItem fi = (DiskFileItem)cf.getFileItem();
            File f = fi.getStoreLocation();
            InputStream stream = new FileInputStream(f);
            Workbook rwb = Workbook.getWorkbook(stream);
            Sheet rss[] = rwb.getSheets();// excel多个sheet页面
            log.info("该导入excel拥有{}个sheet", rss.length);
            for(int i = 0 ; i < rss.length; i ++){
                Thread.sleep(1000 * 1);
                log.info("开始处理第{}个sheet", i+1);
                ImportExcel importExcel = new ImportExcel(file, 0, i);
                if (importExcel.getRow(0).getPhysicalNumberOfCells() != AnnualCorePoi.class.getDeclaredFields().length) {
                    ResultDtoFactory.toNack("导入失败，错误的模板");
                    dto.setCode(ResultCode.NACK);
                    dto.setMessage("导入失败，错误的模板");
                } else {
                    excelRowVoList.addAll(importExcel.getDataList(CoreCollectionExcelRowVo.class));
                }
            }
            //收集所有sheet数据开始

            log.info("获取年检任务ID{}", annualTaskId);
            if(excelRowVoList != null && excelRowVoList.size()>0){
                CollectControllerExecutor collectControllerExecutor = new CollectControllerExecutor();
                collectControllerExecutor.setCoreCollectionService(coreCollectionService);
                collectControllerExecutor.setCollectType(StringUtils.isBlank(reUpload) ? CollectType.AFRESH : CollectType.CONTINUE);
                collectControllerExecutor.setCoreType(1);
                collectControllerExecutor.setTaskId(annualTaskId);
                collectControllerExecutor.setDataList(excelRowVoList);
                annualExecutor.execute(collectControllerExecutor);
                    //coreCollectionService.collect(dataList,annualTaskId, CollectType.AFRESH);
                dto.setCode(ResultCode.ACK);
                dto.setMessage("导入成功");
            }else{
                ResultDtoFactory.toNack("导入失败，错误的模板");
                dto.setCode(ResultCode.NACK);
                dto.setMessage("导入失败，错误的模板");
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
     * 年检核心数据列表
     * @return
     */
    @RequestMapping(value = "/coredata",method = RequestMethod.GET)
    public ResultDto coredata(CoreCollectResultSearchDto coreCollectResultSearchDto) throws IOException {
        Long taskId = annualTaskService.getAnnualCompareTaskId();
        coreCollectResultSearchDto = coreCollectionService.search(coreCollectResultSearchDto, taskId);
        return ResultDtoFactory.toAckData(coreCollectResultSearchDto);
    }

    /**
     * 年检核心数据-单条
     * @return
     */
    @RequestMapping(value = "/core/{id}",method = RequestMethod.GET)
    public ObjectRestResponse annualCore(@PathVariable("id") Long id) throws IOException {
        CoreCollectionDto coreCollectionDto = coreCollectionService.findById(id);
        if(coreCollectionDto != null && coreCollectionDto.getRegisteredCapital() != null){
            coreCollectionDto.setRegisteredCapitalStr(NumberUtils.formatCapital(coreCollectionDto.getRegisteredCapital()).toString());
        }
        return new ObjectRestResponse<CoreCollectionDto>().rel(true).msg("成功").result(coreCollectionDto);
    }

    /**
     * 年检采集配置保存
     * @return
     */
    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.UPDATE,operateContent = "年检采集配置保存")
    @RequestMapping(value = "/save/dataconfig",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse updateDataConfig(CollectConfigDto configDto) throws IOException {
        collectConfigService.saveCollectConfig(configDto);
        return new ObjectRestResponse<String>().rel(true).msg("成功");
    }

    /**
     * 年检采集配置获取
     * @return
     */
    @RequestMapping(value = "/dataconfig",method = RequestMethod.GET)
    public ObjectRestResponse dataConfig() throws IOException {
        Long taskId = annualTaskService.getAnnualCompareTaskId();
        CollectConfigDto configDto = collectConfigService.findByAnnualTaskId(taskId);
        if(configDto == null){
            configDto = new CollectConfigDto();
        }
        return new ObjectRestResponse<CollectConfigDto>().rel(true).msg("成功").result(configDto);
    }
    /**
     * 年检人行采集任务开始
     * @return
     */
    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.START,operateContent = "年检人行在线采集任务启动")
    @RequestMapping(value = "/pbc/start", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse pbcStart() {
        Long taskId = annualTaskService.getAnnualCompareTaskId();
        CollectControllerExecutor collectControllerExecutor = new CollectControllerExecutor();
        collectControllerExecutor.setPbcCollectionService(pbcCollectionService);
        collectControllerExecutor.setCollectType(CollectType.AFRESH);
        collectControllerExecutor.setTaskId(taskId);
        annualExecutor.execute(collectControllerExecutor);
//        pbcCollectionService.collect(CollectType.AFRESH,taskId);
        return new ObjectRestResponse<String>().rel(true).msg("成功");
    }


    /**
     * 年检人行采集excel任务开始
     * @return
     */
    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.START,operateContent = "年检人行重新采集excel任务启动")
    @RequestMapping(value = "/pbc/start/excel/reset", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse pbcStartExcelReset() {
        Long taskId = annualTaskService.initAnnualTask();
        CollectTaskDto collectTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.PBC, taskId);
        if(collectTaskDto == null){//无人行采集任务
            return new ObjectRestResponse<String>().rel(false).msg("人行采集未开始，无法重新采集");
        }else if(collectTaskDto.getCollectStatus() == CollectTaskState.fail || collectTaskDto.getCollectStatus() == CollectTaskState.done){
//            long count = pbcCollectAccountService.countByCollectStateNot(CollectState.noNeed, CollectState.success);
            long count = pbcCollectOrganService.countByCollectStateNot(CollectState.success);
            if(count >0){
                CollectControllerExecutor collectControllerExecutor = new CollectControllerExecutor();
                collectControllerExecutor.setPbcCollectionService(pbcCollectionService);
                collectControllerExecutor.setCollectType(CollectType.CONTINUE);
                collectControllerExecutor.setTaskId(taskId);
                collectControllerExecutor.setPbcType(0);
                annualExecutor.execute(collectControllerExecutor);
                return new ObjectRestResponse<String>().rel(true).msg("成功");
            }else{
                return new ObjectRestResponse<String>().rel(false).msg("人行采集的机构为空，无需重新采集");
            }
        }else{
            return new ObjectRestResponse<String>().rel(false).msg("人行采集状态不是成功或者失败，无法重新采集");
        }
    }


    /**
     * 年检人行数据任务开始
     * @return
     */
    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.START,operateContent = "年检人行失败记录重新采集任务启动")
    @RequestMapping(value = "/pbc/start/reset", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse pbcStartReset() {
        Long taskId = annualTaskService.initAnnualTask();
        CollectTaskDto collectTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.PBC, taskId);
        if(collectTaskDto == null){//无人行采集任务
            return new ObjectRestResponse<String>().rel(false).msg("人行采集未开始，无法重新采集");
        }else if(collectTaskDto.getCollectStatus() == CollectTaskState.done){
            long count = pbcCollectAccountService.countByCollectStateNot(CollectState.success, CollectState.noNeed);
            if(count >0){
                CollectControllerExecutor collectControllerExecutor = new CollectControllerExecutor();
                collectControllerExecutor.setPbcCollectionService(pbcCollectionService);
                collectControllerExecutor.setCollectType(CollectType.CONTINUE);
                collectControllerExecutor.setTaskId(taskId);
                collectControllerExecutor.setPbcType(1);
                annualExecutor.execute(collectControllerExecutor);
                return new ObjectRestResponse<String>().rel(true).msg("成功");
            }else{
                return new ObjectRestResponse<String>().rel(false).msg("人行采集失败数据为空，无需重新采集");
            }
        }else{
            return new ObjectRestResponse<String>().rel(false).msg("人行采集状态不是成功，无法重新采集");
        }
    }

    /**
     * 核心数据本地任务失败数据重新采集
     * @return
     */
    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.START,operateContent = "核心数据本地任务失败数据重新采集启动")
    @RequestMapping(value = "/core/start/reset", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse coreStartReset() {
        Long taskId = annualTaskService.initAnnualTask();
        CollectTaskDto collectTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.CORE, taskId);
        if(collectTaskDto == null){//无核心采集任务
            return new ObjectRestResponse<String>().rel(false).msg("核心采集未开始，无法重新采集");
        }else if(collectTaskDto.getCollectStatus() == CollectTaskState.done){
            int count = coreCollectionService.countByCollectStateNot(taskId,CollectState.success, CollectState.noNeed);
            if(count >0){
                CollectControllerExecutor collectControllerExecutor = new CollectControllerExecutor();
                collectControllerExecutor.setCoreCollectionService(coreCollectionService);
                collectControllerExecutor.setCollectType(CollectType.CONTINUE);
                collectControllerExecutor.setTaskId(taskId);
                collectControllerExecutor.setPbcType(1);
                annualExecutor.execute(collectControllerExecutor);
                return new ObjectRestResponse<String>().rel(true).msg("成功");
            }else{
                return new ObjectRestResponse<String>().rel(false).msg("核心采集失败数据为空，无需重新采集");
            }
        }else{
            return new ObjectRestResponse<String>().rel(false).msg("核心采集状态不是成功，无法重新采集");
        }
    }

    /**
     * 年检人行数据-单条
     * @return
     */
    @RequestMapping(value = "/pbc/{id}",method = RequestMethod.GET)
    public ObjectRestResponse annualPbc(@PathVariable("id") Long id) throws IOException {
        List<FetchPbcInfoDto> list = fetchPbcInfoService.findByCollectAccountId(id);
        FetchPbcInfoDto fetchPbcInfoDto = new FetchPbcInfoDto();
        if(list != null && list.size()>0){
            fetchPbcInfoDto = list.get(0);
            //人行字段展现时显示账户性质
            fetchPbcInfoDto.setString020(fetchPbcInfoDto.getAcctType().getFullName());
        }
        return new ObjectRestResponse<FetchPbcInfoDto>().rel(true).msg("成功").result(fetchPbcInfoDto);
    }

    /**
     * 年检工商采集任务开始
     * @return
     */
    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.START,operateContent = "年检工商在线采集任务启动")
    @RequestMapping(value = "/saic/start", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse saicStart() {
        Long taskId = annualTaskService.initAnnualTask();
        int status = saicCollectionService.checkSaicCollectTaskStatus(taskId);
        if (status == 0) {
            return new ObjectRestResponse<String>().rel(false).msg("核心导入未完成");
        } else if (status == 1) {
            return new ObjectRestResponse<String>().rel(false).msg("人行数据未解析完成");
        } else if (status == 2) {
            return new ObjectRestResponse<String>().rel(false).msg("工商采集已经开启");
        } else if (status == 3) {
            return new ObjectRestResponse<String>().rel(false).msg("工商采集使用文件导入方式");
        } else if (status == 4) {
            CollectControllerExecutor collectControllerExecutor = new CollectControllerExecutor();
            collectControllerExecutor.setSaicCollectionService(saicCollectionService);
            collectControllerExecutor.setCollectType(CollectType.AFRESH);
            collectControllerExecutor.setTaskId(taskId);
            collectControllerExecutor.setSaicType(0);//在线采集
            annualExecutor.execute(collectControllerExecutor);
            return new ObjectRestResponse<String>().rel(true).msg("成功");
        } else if (status == 5) {
            return new ObjectRestResponse<String>().rel(false).msg("未配置工商采集方式");
        } else if (status == 7) {
            return new ObjectRestResponse<String>().rel(false).msg("正在执行定时采集操作");
        } else if (status == 8) {
            return new ObjectRestResponse<String>().rel(false).msg("定时采集任务正在等待执行");
        } else {
            return new ObjectRestResponse<String>().rel(false).msg("其他操作中，无法采集，请等待");
        }
    }


    /**
     * 年检工商失败重新采集任务开始
     * @return
     */
    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.START,operateContent = "年检工商失败重新采集任务重新启动")
    @RequestMapping(value = "/saic/start/reset", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse saicStartReset() {
        Long taskId = annualTaskService.initAnnualTask();
        int status = saicCollectionService.checkSaicCollectTaskStatus(taskId);
        if (status == 0) {
            return new ObjectRestResponse<String>().rel(false).msg("核心导入未完成");
        } else if (status == 1) {
            return new ObjectRestResponse<String>().rel(false).msg("人行数据未解析完成");
        } else if (status == 2) {
            return new ObjectRestResponse<String>().rel(false).msg("工商采集正在采集中，无法重新采集");
        } else if (status == 22) {
            CollectControllerExecutor collectControllerExecutor = new CollectControllerExecutor();
            collectControllerExecutor.setSaicCollectionService(saicCollectionService);
            collectControllerExecutor.setCollectType(CollectType.CONTINUE);
            collectControllerExecutor.setTaskId(taskId);
            collectControllerExecutor.setSaicType(0);//在线采集
            annualExecutor.execute(collectControllerExecutor);
            return new ObjectRestResponse<String>().rel(true).msg("成功");
        } else if (status == 5) {
            return new ObjectRestResponse<String>().rel(false).msg("未配置工商采集方式");
        } else if (status == 7) {
            return new ObjectRestResponse<String>().rel(false).msg("正在执行定时采集操作");
        } else if (status == 8) {
            return new ObjectRestResponse<String>().rel(false).msg("定时采集任务正在等待执行");
        } else {
            return new ObjectRestResponse<String>().rel(false).msg("其他操作中，无法重新采集，请等待");
        }
    }
    /**
     * 年检工商数据列表
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/saicdata",method = RequestMethod.GET)
    @ResponseBody
    public ResultDto saicdata(SaicCollectResultSearchDto saicCollectResultSearchDto) throws IOException {
        Long taskId = annualTaskService.getAnnualCompareTaskId();
        saicCollectResultSearchDto = saicCollectionService.search(saicCollectResultSearchDto, taskId);
        return ResultDtoFactory.toAckData(saicCollectResultSearchDto);
    }

    /**
     * 年检工商数据-单条
     * @return
     */
    @RequestMapping(value = "/saic/{id}",method = RequestMethod.GET)
    public ObjectRestResponse annualSaic(@PathVariable("id") Long id) throws IOException {
        FetchSaicInfoDto fetchSaicInfoDto = fetchSaicInfoService.findById(id);
        SaicIdpInfo resultData =null;
        if(fetchSaicInfoDto != null && StringUtils.isNotBlank(fetchSaicInfoDto.getIdpJsonStr())){
//            resultData = JSON.parse(fetchSaicInfoDto.getIdpJsonStr());
            resultData = JSON.parseObject(fetchSaicInfoDto.getIdpJsonStr(), SaicIdpInfo.class);
        }

        return new ObjectRestResponse<Object>().rel(true).msg("成功").result(resultData);
    }


    /**
     * 比对开启时的判断
     * @param taskId
     * @return
     */
    @GetMapping("/start/{taskId}/check")
    public ObjectRestResponse startCheck(@PathVariable("taskId") Long taskId) {

        CollectTaskDto collectTaskForCodeDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.CORE, taskId);
        CollectTaskDto collectTaskForPbcDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.PBC, taskId);
        CollectTaskDto collectTaskForSaicDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.SAIC, taskId);
        if(collectTaskForCodeDto == null || collectTaskForCodeDto.getCollectStatus() != CollectTaskState.done ){
            return new ObjectRestResponse<String>().rel(false).msg("核心数据采集未完成，无法开始比对");
        }
        if(collectTaskForPbcDto == null || collectTaskForPbcDto.getCollectStatus() != CollectTaskState.done ){
            return new ObjectRestResponse<String>().rel(false).msg("人行数据采集未完成，无法开始比对");
        }
        if(collectTaskForSaicDto == null || collectTaskForSaicDto.getCollectStatus() != CollectTaskState.done ){
            return new ObjectRestResponse<String>().rel(false).msg("工商数据采集未完成，无法开始比对");
        }
//
//        if(collectTaskForPbcDto.getCount() != collectTaskForPbcDto.getSuccessed()){
//            return new ObjectRestResponse<String>().rel(true).msg("人行采集有失败的记录，是否继续开始比对");
//        }

        if(collectTaskForSaicDto.getCount() != collectTaskForSaicDto.getSuccessed()){
            return new ObjectRestResponse<String>().rel(true).msg("工商采集有失败的记录，是否继续开始比对");
        }

        return new ObjectRestResponse<String>().rel(true);
    }

    /**
     * 开始比对
     * @param taskId
     * @return
     */
    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.START,operateContent = "年检比对启动")
    @GetMapping("/start/{taskId}")
	public ObjectRestResponse start(@PathVariable("taskId") Long taskId) {
        try {
            annualTaskService.start(taskId);
        }catch (BizServiceException e){
            log.error("该任务未设置比对规则", e);
            return new ObjectRestResponse<String>().rel(false).msg(e.getMessage());
        }catch (Exception e) {
            log.error("执行异常", e);
        }

        return new ObjectRestResponse<String>().rel(true).msg("任务开始");
	}

    @GetMapping("/statistsics/{taskId}")
    public void statistsics(@PathVariable("taskId") Long taskId) {
        log.info("开始检测年检任务是否完成");
        annualTaskService.compareCompleted(taskId);
        //导出
        log.info("开始生成年检导出内容");
        annualResultExportService.createAnnualResultExport(taskId);
        log.info("年检统计导出结束！");

    }

    @GetMapping("/{taskId}")
	public ObjectRestResponse getTask(@PathVariable("taskId") Long taskId) {
		return new ObjectRestResponse<AnnualTaskDto>().rel(true).result(annualTaskService.findById(taskId));
	}

    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.DELETE,operateContent = "重置:只清空结果数据")
	@GetMapping("/reset/{taskId}")
	public ObjectRestResponse reset(@PathVariable("taskId") Long taskId) {
		annualTaskService.reset(taskId);
		return new ObjectRestResponse<AnnualTaskDto>().rel(true);
	}

    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.DELETE,operateContent = "重置:同时清空数据")
	@GetMapping("/collect/reset/{taskId}")
	public ObjectRestResponse collectReset(@PathVariable("taskId") Long taskId) {
    	collectTaskService.deleteByTaskId(taskId);

        coreCollectionService.clearFuture();
        coreCollectionService.endFuture();
		coreCollectionService.deleteByTaskId(taskId);
		pbcCollectionService.clearFuture();
		pbcCollectionService.endFuture();
    	fetchPbcInfoService.deleteByTaskId(taskId);
    	saicCollectionService.clearFuture();
    	saicCollectionService.endFuture();
		fetchSaicInfoService.deleteByTaskId(taskId);

		//去掉年检结果的数据
        annualResultService.deleteAnnualResultByTaskId(taskId);

		annualTaskService.reset(taskId);
		return new ObjectRestResponse<AnnualTaskDto>().rel(true);
	}

    @GetMapping("/collect/resetSaic/{taskId}")
    public ObjectRestResponse collectResetSaic(@PathVariable("taskId") Long taskId) {
        collectTaskService.deleteByAnnualTaskIdAndCollectTaskType(taskId,DataSourceEnum.SAIC);
        saicCollectionService.clearFuture();
        saicCollectionService.endFuture();
        fetchSaicInfoService.deleteByTaskId(taskId);
        return new ObjectRestResponse<AnnualTaskDto>().rel(true);
    }

    @GetMapping("/collect/resetCore/{taskId}")
    public ObjectRestResponse collectResetCore(@PathVariable("taskId") Long taskId) {
        collectTaskService.deleteByAnnualTaskIdAndCollectTaskType(taskId,DataSourceEnum.CORE);
        coreCollectionService.clearFuture();
        coreCollectionService.endFuture();
        coreCollectionService.deleteByTaskId(taskId);
        return new ObjectRestResponse<AnnualTaskDto>().rel(true);
    }

    @GetMapping("/collect/resetPbc/{taskId}")
    public ObjectRestResponse collectResetPbc(@PathVariable("taskId") Long taskId) {
        collectTaskService.deleteByAnnualTaskIdAndCollectTaskType(taskId,DataSourceEnum.PBC);
        pbcCollectionService.clearFuture();
        pbcCollectionService.endFuture();
        fetchPbcInfoService.deleteByTaskId(taskId);
        return new ObjectRestResponse<AnnualTaskDto>().rel(true);
    }

    /**
     * 年检核心采集任务开始
     * @return
     */
    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.START,operateContent = "年检核心在线采集启动")
    @RequestMapping(value = "/core/start", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse coreStart() {
        Long taskId = annualTaskService.getAnnualCompareTaskId();
        CollectControllerExecutor collectControllerExecutor = new CollectControllerExecutor();
        collectControllerExecutor.setCoreCollectionService(coreCollectionService);
        collectControllerExecutor.setCollectType(CollectType.AFRESH);
        collectControllerExecutor.setCoreType(0);
        collectControllerExecutor.setTaskId(taskId);
        annualExecutor.execute(collectControllerExecutor);

        return new ObjectRestResponse<String>().rel(true).msg("成功");
    }


    /**
     * 核心模板文件下载
     * @param response
     * @throws IOException
     */
    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.EXPORT,operateContent = "核心模板文件下载")
    @GetMapping("/core/download")
    public void downloadCoreExcel(HttpServletResponse response) throws IOException {
        IExcelExport excelExport = coreCollectionService.generateAnnualCompanyReport();

        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode("年检核心模板文件", "UTF-8")+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",excelExport);
        toClient.flush();
        response.getOutputStream().close();
    }

    @GetMapping("/saic/download")
    public void downloadSaicExcel(HttpServletResponse response) throws IOException {
        Long taskId = annualTaskService.getAnnualCompareTaskId();
        IExcelExport excelExport = saicCollectionService.generateAnnualCompanyReport(taskId);

        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode("年检企业列表-"+DateUtils.getDateTime(), "UTF-8")+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",excelExport);
        toClient.flush();
        response.getOutputStream().close();
    }

    @GetMapping("/saic/fileCounts")
    public ObjectRestResponse checkSaicFileCounts(){
        Integer count = saicCollectionService.saicFileCounts();
        return new ObjectRestResponse<Integer>().rel(true).result(count);
    }

    @GetMapping("/saic/clearFiles")
    public ObjectRestResponse clearSaicFiles(){
        saicCollectionService.clearFiles();
        return new ObjectRestResponse<Integer>().rel(true).msg("清空文件成功");
    }

    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.START,operateContent = "年检工商文件采集启动")
    @RequestMapping(value = "/saic/startFile", method = RequestMethod.GET)
    public ObjectRestResponse startFile(){
        Long taskId = annualTaskService.getAnnualCompareTaskId();
        int status = saicCollectionService.checkSaicCollectTaskStatus(taskId);
        if(status == 0){
            return new ObjectRestResponse<String>().rel(false).msg("核心导入未完成");
        }else if(status == 1){
            return new ObjectRestResponse<String>().rel(false).msg("人行数据未解析完成");
        }else if(status == 2){
            return new ObjectRestResponse<String>().rel(false).msg("工商采集已经开启");
        }else if(status ==3 ){
            Integer count = saicCollectionService.saicFileCounts();
            if(count >0){
                CollectControllerExecutor collectControllerExecutor = new CollectControllerExecutor();
                collectControllerExecutor.setSaicCollectionService(saicCollectionService);
                collectControllerExecutor.setCollectType(CollectType.AFRESH);
                collectControllerExecutor.setTaskId(taskId);
                collectControllerExecutor.setSaicType(1);//文件导入采集
                annualExecutor.execute(collectControllerExecutor);
                return new ObjectRestResponse<String>().rel(true).msg("采集开始");
            }else{
                return new ObjectRestResponse<String>().rel(false).msg("请先导入工商采集的文件");
            }
        }else if(status == 4){
            return new ObjectRestResponse<String>().rel(false).msg("工商采集使用在线方式采集");
        }else if(status == 5){
            return new ObjectRestResponse<String>().rel(false).msg("未配置工商采集方式");
        } else if (status == 7) {
            return new ObjectRestResponse<String>().rel(false).msg("正在执行定时采集操作");
        } else if (status == 8) {
            return new ObjectRestResponse<String>().rel(false).msg("定时采集任务正在等待执行");
        }else{
            return new ObjectRestResponse<String>().rel(false).msg("其他操作中，无法采集，请等待");
        }
    }


    @PostMapping(value = "/saic/upload")
    public void saicUpload(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        ResultDto dto = new ResultDto();
        try {
            if(FilenameUtils.isExtension(file.getOriginalFilename(), SaicCollectionServiceImpl.FILE_EXTENSION)){
                String filename = saicFileLocation + File.separator + file.getOriginalFilename();
                File fileNew = new File(filename);
                if(fileNew.exists()){
                    filename = saicFileLocation + File.separator + RandomStringUtils.randomNumeric(5)+file.getOriginalFilename();
                    fileNew = new File(filename);
                }
                FileUtils.copyInputStreamToFile(file.getInputStream(),fileNew);
                dto.setCode(ResultCode.ACK);
                dto.setMessage("上传文件成功");
            }else{
                dto.setCode(ResultCode.NACK);
                dto.setMessage("上传的文件类型必须为json结尾");
            }
        } catch (IOException e) {
            log.error("上传文件失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("上传文件失败");
        }finally {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
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
        return DateUtils.DateToStr(new Date(),"yyyy-MM-dd");
    }

    @GetMapping(value = "/comparefieldsconfig")
    public ObjectRestResponse compareFieldsConfig(){
        Long taskId = annualTaskService.getAnnualCompareTaskId();
        log.info("当前年检任务的id：" + taskId);
        //保证数据库数据与CompareFieldEnum枚举数据一致（缺少的枚举进行补充）
        compareFieldsService.saveDefaultCompareFieldsRules(taskId);

        List<CompareFieldsDto> compareFieldsDtoList = compareFieldsService.listCompareRulesByTakId(taskId);
        log.info("compareFieldsDtoList"+compareFieldsDtoList);

        List<CompareRuleDto> compareRuleDtoList= compareRuleService.listCompareRulesByTakId(taskId);
        log.info("compareRuleDtoList"+compareRuleDtoList);

        String fields = compareFieldsService.getLockField();
        List<CompareFieldsResultDto> compareFieldsResultDtoList = new ArrayList<>();
        for (CompareFieldsDto compareFieldsDto : compareFieldsDtoList) {
            //前端不展示核心机构选项
            if (compareFieldsDto.getCompareFieldEnum().equals(CompareFieldEnum.ORGAN_CODE)) {
                continue;
            }
            CompareFieldsResultDto compareFieldsResultDto = new CompareFieldsResultDto();
            compareFieldsResultDto.setActive(compareFieldsDto.isActive());
            compareFieldsResultDto.setId(compareFieldsDto.getId());
            compareFieldsResultDto.setCompareFieldEnum(compareFieldsDto.getCompareFieldEnum());
            compareFieldsResultDto.setFieldName(compareFieldsDto.getCompareFieldEnum().getValue());
            compareFieldsResultDto.setTaskId(compareFieldsDto.getTaskId());
            if(StringUtils.isNotBlank(fields) && fields.contains(compareFieldsDto.getCompareFieldEnum().getField())){
                compareFieldsResultDto.setLockFiled(true);
                compareFieldsService.setLock(compareFieldsDto.getCompareFieldEnum(),taskId);
            }
            for (CompareRuleDto compareRuleDto: compareRuleDtoList) {
                if (compareRuleDto.getCompareFieldEnum()==compareFieldsDto.getCompareFieldEnum()){
                    switch (compareRuleDto.getDataSourceEnum()){
                        case CORE:
                            compareFieldsResultDto.setCoreActive(compareRuleDto.isActive());
                            compareFieldsResultDto.setCoreNullpass(compareRuleDto.isNullpass());
                            break;
                        case PBC:
                            compareFieldsResultDto.setPbcActive(compareRuleDto.isActive());
                            compareFieldsResultDto.setPbcNullpass(compareRuleDto.isNullpass());
                            break;
                        case SAIC:
                            compareFieldsResultDto.setSaicActive(compareRuleDto.isActive());
                            compareFieldsResultDto.setSaicNullpass(compareRuleDto.isNullpass());
                            break;
                        default:
                            break;
                    }

                }
            }
            compareFieldsResultDtoList.add(compareFieldsResultDto);
        }

        return new ObjectRestResponse<Object>().rel(true).msg("成功").result(compareFieldsResultDtoList);
    }

    @OperateLog(operateModule = OperateModule.ANNUAL,operateType = OperateType.UPDATE,operateContent = "比对字段配置修改")
    @GetMapping(value = "/comparefieldsconfig/update")
    public boolean compareFieldsConfigUpdate(@RequestParam("field")String field,@RequestParam("rule")String rule,@RequestParam("checked")boolean checked){
        Long taskId = annualTaskService.getAnnualCompareTaskId();

        if ("active".equals(rule) && rule!=null){
            if (field!=null){
                List<CompareFieldsDto> compareFieldsDtoList = compareFieldsService.listCompareRulesByTakId(taskId);
                for (CompareFieldsDto compareFieldsDto : compareFieldsDtoList) {
                    if (compareFieldsDto.getCompareFieldEnum().toString().equals(field)){
                        compareFieldsDto.setActive(checked);
                        if (compareFieldsService.saveCompareRules(compareFieldsDto)){
                            log.info("修改成功");
                            return true;
                        }else {
                            log.info("修改失败");
                            return false;
                        }
                    }
                }
            }
            log.info("field为空或没有匹配字段");
            return false;
        }else {
            if (field!=null){
                DataSourceEnum dataSource=null;
                if(rule.contains("core")){
                    dataSource=DataSourceEnum.CORE;
                }else if(rule.contains("pbc")){
                    dataSource=DataSourceEnum.PBC;
                }else if(rule.contains("saic")){
                    dataSource=DataSourceEnum.SAIC;
                }
                CompareRuleDto compareRuleDto = compareRuleService.findCompareRulesByfieldsAndDataSource(taskId,CompareFieldEnum.str2enum(field),dataSource);
                if(compareRuleDto!=null){
                    if (compareRuleDto.getCompareFieldEnum().name().equals(field)){
                        if (rule.indexOf("core")!=-1 ){
                            if (rule.indexOf("Nullpass")!=-1){
                                compareRuleDto.setNullpass(checked);
                            }else if(rule.indexOf("Active")!=-1){
                                compareRuleDto.setActive(checked);
                            }
                        }else if (rule.indexOf("pbc")!=-1 ){
                            if (rule.indexOf("Nullpass")!=-1){
                                compareRuleDto.setNullpass(checked);
                            }else if(rule.indexOf("Active")!=-1){
                                compareRuleDto.setActive(checked);
                            }
                        }else if (rule.indexOf("saic")!=-1){
                            if (rule.indexOf("Nullpass")!=-1){
                                compareRuleDto.setNullpass(checked);
                            }else if(rule.indexOf("Active")!=-1){
                                compareRuleDto.setActive(checked);
                            }
                        }
                        if (compareRuleService.saveCompareRules(compareRuleDto)){
                            return true;
                        }else {
                            return false;
                        }
                    }
                }else{
                    compareRuleDto = new CompareRuleDto();
                    //compareRuleDto.setActive(checked);
                    if (ArrayUtils.contains(CompareFieldEnum.values(), CompareFieldEnum.str2enum(field))) {
                        compareRuleDto.setCompareFieldEnum(CompareFieldEnum.str2enum(field));
                    }
                    if (rule.indexOf("Nullpass")!=-1){
                        compareRuleDto.setNullpass(checked);
                    }else if(rule.indexOf("Active")!=-1){
                        compareRuleDto.setActive(checked);
                    }
                    if (rule.indexOf("core")!=-1){
                        compareRuleDto.setDataSourceEnum(DataSourceEnum.CORE);
                    }else if (rule.indexOf("pbc")!=-1){
                        compareRuleDto.setDataSourceEnum(DataSourceEnum.PBC);
                    }else if (rule.indexOf("saic")!=-1){
                        compareRuleDto.setDataSourceEnum(DataSourceEnum.SAIC);
                    }

                    compareRuleDto.setTaskId(taskId);
                    if (compareRuleService.saveCompareRules(compareRuleDto)){
                        return true;
                    }else {
                        return false;
                    }
                }
            }
            log.info("field为空或没有匹配字段");
            return false;
        }

    }

    /**
     * 年检人行采集检查人行用户名登录情况（所有机构重新登录一次检查）
     */
    @GetMapping(value = "/annualCheckAmsPassword")
    public ResultDto annualCheckAmsPassword(){
        log.info("年检数据采集人行账管登录校验...");
        Set<String> amsLoginErrorList = annualTaskService.annualCheckAmsPassword();
        return ResultDtoFactory.toAckData(amsLoginErrorList);
    }

}

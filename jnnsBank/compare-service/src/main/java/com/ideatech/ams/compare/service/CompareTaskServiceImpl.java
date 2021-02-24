package com.ideatech.ams.compare.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.compare.dao.CompareRuleDao;
import com.ideatech.ams.compare.dao.CompareTaskDao;
import com.ideatech.ams.compare.dao.data.CompareDataRepository;
import com.ideatech.ams.compare.dao.jpa.CompareRepositoryFinder;
import com.ideatech.ams.compare.dao.spec.CompareTaskSpec;
import com.ideatech.ams.compare.dto.*;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.entity.CompareResultSaicCheck;
import com.ideatech.ams.compare.entity.CompareRule;
import com.ideatech.ams.compare.entity.CompareTask;
import com.ideatech.ams.compare.entity.data.CompareData;
import com.ideatech.ams.compare.enums.*;
import com.ideatech.ams.compare.executor.CompareDataExecutor;
import com.ideatech.ams.compare.poi.SaicInfoCompareExport;
import com.ideatech.ams.compare.poi.SaicInfoComparePoi;
import com.ideatech.ams.compare.spi.comparator.Comparator;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.system.blacklist.service.BlackListService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.org.utils.OrganUtils;
import com.ideatech.ams.system.user.dao.UserDao;
import com.ideatech.ams.system.user.entity.UserPo;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.zip.ZipEntry;
import com.ideatech.common.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
//@Transactional
@Slf4j
public class CompareTaskServiceImpl extends BaseServiceImpl<CompareTaskDao, CompareTask, CompareTaskDto> implements CompareTaskService {

    @Value("${ams.compare.compare-executor-num:300}")
    private Integer compareExecutorNum;

    @Value("${ams.illegalImport.executor.num:10}")
    private int illegalImportExecutorNum;

    @Autowired
    private UserDao userDao;
    @Autowired
    private CompareRuleDao compareRuleDao;

    @Autowired
    private CompareRuleService compareRuleService;

    @Autowired
    private CompareRuleFieldsService compareRuleFieldsService;

    @Autowired
    private CompareFieldService compareFieldService;

    @Autowired
    private CompareRuleDataSourceService compareRuleDataSourceService;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private CompareDataService compareDataService;

    @Autowired
    private CompareResultService compareResultService;

    @Autowired
    private CompareRepositoryFinder compareRepositoryFinder;

    @Autowired
    private CompareCollectTaskService compareCollectTaskService;

    @Autowired
    protected Map<String, DataImporter> dataImporters;

    @Autowired
    private ThreadPoolTaskExecutor compareExecutor;

    @Autowired
    private Map<String, Comparator> comparators;

    @Autowired
    private CompareDefineService compareDefineService;

    @Autowired
    private TransactionUtils transactionUtils;

    @Autowired
    private CompareCollectRecordService compareCollectRecordService;

    @Autowired
    private CompareExportService compareExportService;

    @Autowired
    private HttpSession session;

    @Autowired
    private BlackListService blackListService;

    private List<Future<Long>> futureList;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private CompareStatisticsService compareStatisticsService;

    @Autowired
    private SaicInfoService saicInfoService;

    @Autowired
    private CompareResultSaicCheckService compareResultSaicCheckService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CustomerAbnormalService customerAbnormalService;

    @Value("${compare.saicCheck.filePath:/home/weblogic/idea/saicCheck/export}")
    private String filePath;

    @Autowired
    private CompareDataConvertService compareDataConvertService;

    @Override
    public void saveTask(CompareTaskDto dto) {
        CompareTask task = new CompareTask();
        if (dto.getId() == null) {
            if (dto.getTaskType() == null || "".equals(dto.getTaskType())) {
                throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "比对任务类型未输入！");
            }
            if (dto.getName() == null || "".equals(dto.getName())) {
                throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "比对任务名称未输入！");
            }
            if (dto.getName().contains("工商异常校验")){
                throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "包含“工商异常校验”关键字！");
            }
            if (dto.getCompareRuleId() == null) {
                throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "比对任务规则未输入！");
            }
            CompareTask isHave = getBaseDao().findByName(dto.getName());
            if (isHave != null) {
                throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "比对任务名称已存在！");
            }
            BeanCopierUtils.copyProperties(dto, task);
            UserPo user = userDao.findOne(SecurityUtils.getCurrentUserId());
            task.setCreatedBy(String.valueOf(SecurityUtils.getCurrentUserId()));
            task.setCreateName(user.getCname());
            task.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
            task.setTime(DateUtils.getNowDateShort());
            task.setCreatedDate(new Date());
            task.setState(CompareState.INIT);
            task.setStateBeforePause(CompareState.INIT);
            task.setIsRunning(false);
            task.setIsPause(false);
            task.setCount(0);
            task.setProcessed(0);
        } else {
            task = getBaseDao().findOne(dto.getId());
            CompareTask isHave = getBaseDao().findByNameAndIdNot(dto.getName(), dto.getId());
            if (isHave != null) {
                throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "比对任务名称已存在！");
            }
            task.setState(dto.getState() == null ? task.getState() : dto.getState());
            task.setRate(dto.getRate() == null ? task.getRate() : dto.getRate());
            task.setStartTime(dto.getStartTime() == null ? task.getStartTime() : dto.getStartTime());
            task.setName(dto.getName());
            task.setTaskType(dto.getTaskType());
            task.setCompareRuleId(dto.getCompareRuleId());
            task.setCount(task.getCount() == null ? 0 : task.getCount());
            task.setProcessed(task.getProcessed() == null ? 0 : task.getProcessed());
            task.setIsPause(task.getIsPause() == null ? false : task.getIsPause());
            task.setIsRunning(task.getIsRunning() == null ? false : task.getIsRunning());
            task.setLastStartedTime(dto.getLastStartedTime());
        }
        getBaseDao().save(task);
    }

    @Override
    public CompareTaskSearchDto search(CompareTaskSearchDto dto, Pageable pageable) {
        try {
            if (StringUtils.isNotBlank(dto.getBeginDateStr())) {
                String begin = dto.getBeginDateStr() + " 00:00:00";
                dto.setBeginDate(DateUtils.parse(begin, "yyyy-MM-dd HH:mm:ss"));
            }
            if (StringUtils.isNotBlank(dto.getEndDateStr())) {
                String end = dto.getEndDateStr() + " 23:59:59";
                dto.setEndDate(DateUtils.parse(end, "yyyy-MM-dd HH:mm:ss"));
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        List<CompareTaskDto> data = new ArrayList<>();
        List<String> fullIdList = OrganUtils.getFullIdsByFullId(SecurityUtils.getCurrentOrgFullId());
        if (CollectionUtils.isNotEmpty(fullIdList)) {
            dto.setFullIdList(fullIdList);
        }

        //过虚拟用户创建的数据
        dto.setCreatedBy("2");

        Page<CompareTask> page = getBaseDao().findAll(new CompareTaskSpec(dto), pageable);
        List<CompareTaskDto> list = ConverterService.convertToList(page.getContent(), CompareTaskDto.class);
        for (CompareTaskDto compareTaskDto : list) {
            CompareRule compareRule = compareRuleDao.findOne(compareTaskDto.getCompareRuleId());
            if (compareRule != null) {
                compareTaskDto.setCompareRuleName(compareRule.getName());
            } else {
                compareTaskDto.setCompareRuleName("");
            }
            compareTaskDto.setCreateName(userDao.findOne(Long.valueOf(compareTaskDto.getCreatedBy())).getCname());
            data.add(compareTaskDto);
        }
        dto.setList(data);
        dto.setTotalRecord(page.getTotalElements());
        dto.setTotalPages(page.getTotalPages());
        return dto;
    }

    @Override
    public CompareTaskDto getDetails(Long taskId) {
        CompareTaskDto compareTaskDto = ConverterService.convert(getBaseDao().findOne(taskId), CompareTaskDto.class);
        if (compareTaskDto == null) {
            log.info("根据taskId：" + taskId + "查找比对任务失败，无此比对任务......");
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "根据taskId：" + taskId + "查找比对任务失败，无此比对任务......");
        }
        //根据compareRuleId找到CompareRuleName
        CompareRuleDto compareRuleDto = compareRuleService.findById(compareTaskDto.getCompareRuleId());
        if (compareRuleDto != null) {
            compareTaskDto.setCompareRuleName(compareRuleDto.getName());
        }
        compareTaskDto.setCompareStateCN(compareTaskDto.getState().getName());
        compareTaskDto.setTaskTypeCN(compareTaskDto.getTaskType().getFullName());
        return compareTaskDto;
    }

    @Override
    public CompareRuleDto getCompareRuleDetails(Long taskId) {
        CompareTaskDto compareTaskDto = findById(taskId);
        if (compareTaskDto == null) {
            log.info("根据taskId：" + taskId + "查找比对任务失败，无此比对任务......");
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "根据taskId：" + taskId + "查找比对任务失败，无此比对任务......");
        }
        //根据compareRuleId找到CompareRuleName
        CompareRuleDto compareRuleDto = compareRuleService.findById(compareTaskDto.getCompareRuleId());
        if (compareRuleDto != null) {
            String compareFields = "";
            //根据比对规则找出比对字段
            List<CompareRuleFieldsDto> compareRuleFieldsDtoList = compareRuleFieldsService.findByCompareRuleId(compareRuleDto.getId());
            if (CollectionUtils.isNotEmpty(compareRuleFieldsDtoList)) {
                for (CompareRuleFieldsDto compareRuleFieldsDto : compareRuleFieldsDtoList) {
                    CompareFieldDto compareFieldDto = compareFieldService.findById(compareRuleFieldsDto.getCompareFieldId());
                    compareFields += compareFieldDto.getName() + ",";
                }
                compareFields = compareFields.substring(0, compareFields.length() - 1);
                if (StringUtils.isNotBlank(compareFields)) {
                    compareRuleDto.setCompareFields(compareFields);
                } else {
                    compareRuleDto.setCompareFields("");
                }
            }
            return compareRuleDto;
        }
        return new CompareRuleDto();
    }

    @Override
    public JSONArray getCompareDataSourceDetails(Long taskId) {

        JSONArray jsonArray = new JSONArray();

        CompareTaskDto compareTaskDto = ConverterService.convert(getBaseDao().findOne(taskId), CompareTaskDto.class);
        if (compareTaskDto == null) {
            log.info("根据taskId：" + taskId + "查找比对任务失败，无此比对任务......");
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "根据taskId：" + taskId + "查找比对任务失败，无此比对任务......");
        }
        //根据compareRuleId找到CompareRuleName
        CompareRuleDto compareRuleDto = compareRuleService.findById(compareTaskDto.getCompareRuleId());
        if (compareRuleDto != null) {
            //根据规则找到对应的数据源
            List<CompareRuleDataSourceDto> compareRuleDataSourceDtoList = compareRuleDataSourceService.findByCompareRuleId(compareRuleDto.getId());
            if (CollectionUtils.isNotEmpty(compareRuleDataSourceDtoList)) {
                for (CompareRuleDataSourceDto compareRuleDataSourceDto : compareRuleDataSourceDtoList) {
                    if (compareRuleDataSourceDto.getActive()) {
                        DataSourceDto dataSourceDto = dataSourceService.findById(compareRuleDataSourceDto.getDataSourceId());
                        if (dataSourceDto != null) {
                            CompareCollectTaskDto compareTaskIdAndCollectTaskType = compareCollectTaskService.findByCompareTaskIdAndDataSourceId(taskId, dataSourceDto.getId());
                            JSONObject json = new JSONObject();
                            json.put("id", dataSourceDto.getId());
                            json.put("name", dataSourceDto.getName());
                            json.put("type", dataSourceDto.getCollectType());
                            if (compareTaskIdAndCollectTaskType != null) {
                                json.put("state", compareTaskIdAndCollectTaskType.getCollectStatus().getFullName());
                                json.put("stateName", compareTaskIdAndCollectTaskType.getCollectStatus().getChName());
                            } else {
                                json.put("state", "-1");
                                json.put("stateName", "未开始");
                            }
                            jsonArray.add(json);
                        }
                    }
                }
            }
        }

        return jsonArray;
    }

    @Override
    public void start(Long taskId) {
        try {
            final CompareTaskDto compareTaskDto = getDetails(taskId);
            //1.检查数据是否全部完成
            checkData(compareTaskDto);

            //1.2更新CompareRule比对规则使用次数。
            CompareRule compareRule = compareRuleDao.findOne(compareTaskDto.getCompareRuleId());
            if (compareRule != null) {
                compareRule.setCount(compareRule.getCount() + 1);
                compareRuleDao.save(compareRule);
            }

            //2.修改状态为比对中  新建事物进行提交修改状态
            transactionUtils.executeInNewTransaction(new TransactionCallback() {
                @Override
                public void execute() throws Exception {
                    changeTaskStatus(compareTaskDto, CompareState.COMPARING);
                }
            });


            //3.根据任务获取该任务对应的数据
            //3.1所有以账号为KEY的数据
            Map<String, Map<Long, CompareDataDto>> dataMaps = getData(compareTaskDto);
            int compareDataSum = dataMaps.size();

            //3.2获取比对需要用的数据（数据源、字段、规则）
            List<CompareRuleDataSourceDto> compareRuleDataSourceDtos = compareRuleDataSourceService.findByCompareRuleId(compareTaskDto.getCompareRuleId());
            List<CompareRuleFieldsDto> compareRuleFieldsDtos = compareRuleFieldsService.findByCompareRuleId(compareTaskDto.getCompareRuleId());
            List<CompareDefineDto> compareDefineDtos = compareDefineService.findByCompareRuleId(compareTaskDto.getCompareRuleId());
            Map<Long, List<CompareDefineDto>> compareDefineMaps = getCompareDefineMaps(compareDefineDtos);

            //4.开始分批次多线程比对
            //包含比对
            Map<String, Map> batchMaps = getBatch(dataMaps);
            dataMaps.clear();

            //5.设置变量用来保存比对结果和计数
            BlockingQueue<CompareResultDto> resultQueue = new LinkedBlockingQueue<>();
            AtomicInteger compareNum = new AtomicInteger(0);

            //6.执行比对线程
            for (Map.Entry<String, Map> mapEntry : batchMaps.entrySet()) {
                CompareDataExecutor taskExecutor = getTaskExecutor(compareTaskDto, mapEntry.getKey(), mapEntry.getValue(), resultQueue, compareNum);
                taskExecutor.setCompareRuleDataSourceDtos(compareRuleDataSourceDtos);
                taskExecutor.setCompareRuleFieldsDtos(compareRuleFieldsDtos);
                taskExecutor.setCompareDefineDtos(compareDefineDtos);
                taskExecutor.setCompareDefineMaps(compareDefineMaps);
                taskExecutor.setBlackListService(blackListService);
                taskExecutor.setTransactionUtils(transactionUtils);
                taskExecutor.setDataSourceService(dataSourceService);
                compareExecutor.execute(taskExecutor);
            }
            batchMaps.clear();
            //7.检查比对是否完成
            compareCompleted(compareTaskDto, resultQueue, compareNum, compareDataSum);

            //如果比对任务完成则开始统计和导出
            if (getCompareStatus(taskId) == CompareState.SUCCESS) {

                //工商异常校验excel打包
                if (compareTaskDto.getName().contains("工商异常校验")){
                    packExcel(compareTaskDto.getId());
                    log.info("Excel打包完成");
                }else {
                    //8.统计
                    log.info("比对完成，按机构进行统计......");
                    compareStatisticsService.statistics(taskId);

                    log.info("比对完成，进行结果导出......");
                    //9.结果导出
                    compareExportService.batchCreateCompareResult(taskId);
                }
            }
        } catch (Exception e) {
            log.error("比对任务开始异常", e);
        }
    }

    private Map<Long, List<CompareDefineDto>> getCompareDefineMaps(List<CompareDefineDto> compareDefineDtos) {
        Map<Long, List<CompareDefineDto>> compareDefineMaps = new HashMap<>(16);
        for (CompareDefineDto compareDefineDto : compareDefineDtos) {
            if (compareDefineMaps.containsKey(compareDefineDto.getCompareFieldId())) {
                List<CompareDefineDto> list = compareDefineMaps.get(compareDefineDto.getCompareFieldId());
                list.add(compareDefineDto);
                compareDefineMaps.put(compareDefineDto.getCompareFieldId(), list);
            } else {
                List<CompareDefineDto> list = new ArrayList<>();
                list.add(compareDefineDto);
                compareDefineMaps.put(compareDefineDto.getCompareFieldId(), list);
            }
        }
        return compareDefineMaps;
    }

    /**
     * 按每个单个线程跑批数量切分总量
     *
     * @param dataMaps
     * @return 返回(线程名, 比对内容)的数据
     */
    private Map<String, Map> getBatch(Map<String, Map<Long, CompareDataDto>> dataMaps) {
        Map<String, Map> resultMap = new HashMap<>(16);
        if (dataMaps != null && dataMaps.size() > 0) {
            int sum = dataMaps.size();
            //任务数
            int executorNum = (sum / compareExecutorNum) + ((sum % compareExecutorNum) == 0 ? 0 : 1);

            int batchNum = 0;

            for (int i = 1; i <= executorNum; i++) {
                int start = (i - 1) * compareExecutorNum;
                int end = i == executorNum ? sum : (i) * compareExecutorNum;
                resultMap.put("第" + (batchNum + 1) + "线程", subMap(dataMaps, start, end));
                batchNum++;
            }
        }
        return resultMap;
    }

    private Map subMap(Map map, int beginIndex, int endIndex) {
        Map result = new HashMap(16);
        int i = 0;
        for (Object o : map.keySet()) {
            if (i >= beginIndex && i < endIndex) {
                result.put(o, map.get(o));
            }
            i++;
        }
        return result;
    }

    /**
     * 比对是否完成校验,递归调用
     * 此处去更新结果与比对任务条数
     *
     * @param task
     * @param resultQueue
     * @param compareNum
     * @param compareDataSum
     * @throws Exception
     */
    private void compareCompleted(final CompareTaskDto task, final BlockingQueue<CompareResultDto> resultQueue, final AtomicInteger compareNum, int compareDataSum) throws Exception {
        log.info("比对任务" + task.getName() + "已比对" + compareNum + "条(共" + compareNum.get() + "条)账户数据;此任务剩余正在比对线程数量：" + compareExecutor.getActiveCount());

        //查询数据库中比对状态是否还在进行中,如果被取消或者重置，则直接暂停
        if (getCompareStatus(task.getId()) != CompareState.COMPARING) {
            log.info("比对任务" + task.getName() + "被取消");
            return;
        }

        // 数量不相同，则比对未完成
        boolean isCompareTaskCompleted = (compareNum.get() == compareDataSum || compareExecutor.getActiveCount() == 0) && resultQueue.isEmpty();
        if (isCompareTaskCompleted) {
            finishTask(task, compareDataSum);
            log.info("比对任务" + task.getName() + "执行完毕");
        } else {
            //5秒轮询一次
            TimeUnit.SECONDS.sleep(5);
            //更新比对任务数据
            transactionUtils.executeInNewTransaction(new TransactionCallback() {
                @Override
                public void execute() throws Exception {
                    updateTaskCount(task.getId(), compareNum.get());
                    while (!resultQueue.isEmpty()) {
                        //保存比对结果
                        CompareResultDto compareResultDto = compareResultService.save(resultQueue.take());
                        //应该在新建比对任务时，防止比对名称包含工商异常校验
                        if (task.getName().contains("工商异常校验")){ //工商异常校验任务 name初始化为工商异常校验+时间戳
                            saicAbnormalCheck(compareResultDto);
                        }
                    }
                }
            });
            compareCompleted(task, resultQueue, compareNum, compareDataSum);
        }
    }


    private CompareDataExecutor getTaskExecutor(CompareTaskDto taskDto, String executorName, Map dataMaps, BlockingQueue<CompareResultDto> resultQueue, AtomicInteger compareNum) {
        CompareDataExecutor taskExecutoer = new CompareDataExecutor(taskDto, dataMaps, resultQueue, compareNum);
        taskExecutoer.setComparators(comparators);
        taskExecutoer.setCompareTaskDto(taskDto);
        taskExecutoer.setTaskName(executorName);
        return taskExecutoer;
    }

    @Override
    public void changeTaskStatus(CompareTaskDto compareTaskDto, CompareState compareState) {
        CompareTask compareTask = getBaseDao().findOne(compareTaskDto.getId());
        if (compareTask != null) {
            BeanUtils.copyProperties(compareTaskDto, compareTask);
            compareTask.setState(compareState);
            getBaseDao().save(compareTask);
        }
    }

    @Override
    public void finishTask(CompareTaskDto taskDto, int processedNum) {
        taskDto.setProcessed(processedNum);
        changeTaskStatus(taskDto, CompareState.SUCCESS);

    }

    /**
     * 检查数据是否全部完成
     * 如果数据未就绪则抛出异常返回到前台
     */
    private void checkData(CompareTaskDto taskDto) {

    }

    /**
     * 根据任务获取该任务对应的数据
     *
     * @param taskDto
     * @return
     */
    private Map<String, Map<Long, CompareDataDto>> getData(CompareTaskDto taskDto) {
        List<CompareRuleDataSourceDto> dataSourceList = compareRuleDataSourceService.findByCompareRuleId(taskDto.getCompareRuleId());

        //所有去重的账号
        Set<String> acctNos = new HashSet<>(16);

        Map<String, Map<Long, CompareDataDto>> dataMaps = new HashMap<>(16);

        //获取不同数据源的数据
        for (CompareRuleDataSourceDto compareRuleDataSourceDto : dataSourceList) {
            final Long dataSourceId = compareRuleDataSourceDto.getDataSourceId();
            if (!compareRuleDataSourceDto.getActive()) {
                continue;
            }
            List<CompareDataDto> compareDataDtos = compareDataService.getCompareData(taskDto.getId(), dataSourceService.findById(dataSourceId));
            for (final CompareDataDto compareDataDto : compareDataDtos) {
                if (StringUtils.isNotBlank(compareDataDto.getAcctNo())) {
                    if (dataMaps.containsKey(compareDataDto.getAcctNo())) {
                        dataMaps.get(compareDataDto.getAcctNo()).put(dataSourceId, compareDataDto);
                    } else {
                        dataMaps.put(compareDataDto.getAcctNo(), new HashMap<Long, CompareDataDto>(3) {
                            {
                                put(dataSourceId, compareDataDto);
                            }
                        });
                    }
                }
                acctNos.add(compareDataDto.getAcctNo());
            }
            compareDataDtos.clear();
        }
        return dataMaps;
    }


    @Override
    public String getCompareTaskCount(Long taskId) throws Exception {

        //先根据比对任务找出需要比对是数据源进行数据导入的统计
        Set<String> acctNo = new HashSet<>();
        final CompareTask compareTask = getBaseDao().findOne(taskId);
        //根据比对任务找出比对规则
        Long ruleId = compareTask.getCompareRuleId();
        List<CompareRuleDataSourceDto> compareRuleDataSourceDtoList = compareRuleDataSourceService.findByCompareRuleId(ruleId);
        //找出账号进行查重统计
        for (CompareRuleDataSourceDto compareRuleDataSourceDto : compareRuleDataSourceDtoList) {
            if (compareRuleDataSourceDto.getActive()) {
                DataSourceDto dataSourcedto = dataSourceService.findById(compareRuleDataSourceDto.getDataSourceId());
                CompareDataRepository<CompareData> repository = compareRepositoryFinder.getRepository(dataSourcedto.getDomain());
                List<String> acctNoList = repository.getAcctNoByTaskId(taskId);
                acctNo.addAll(acctNoList);
            }
        }
        compareTask.setCount(acctNo.size());

        //查找出比对任务勾选的数据源
        boolean res = true;
        List<CompareRuleDataSourceDto> compareRuleDataSourceDtos = new ArrayList<>();
        //找出比对任务涉及到的数据源
        CompareTaskDto compareTaskDto = findById(taskId);
        List<CompareRuleDataSourceDto> compareRuleDataSources = compareRuleDataSourceService.findByCompareRuleId(compareTaskDto.getCompareRuleId());
        for(CompareRuleDataSourceDto compareRuleDataSourceDtom: compareRuleDataSources){
            if(compareRuleDataSourceDtom.getActive()){
                compareRuleDataSourceDtos.add(compareRuleDataSourceDtom);
            }
        }
        log.info("比对任务涉及的数据源数量为：" + compareRuleDataSourceDtos.size());

        List<CompareCollectTaskDto> compareCollectTaskDtos = compareCollectTaskService.findByCompareTaskId(taskId);
        log.info("比对任务采集表中的数量为：" + compareCollectTaskDtos.size());
        if(compareCollectTaskDtos.size() != compareRuleDataSourceDtos.size()){
            res = false;
        }else{
            for(CompareCollectTaskDto compareCollectTaskDto : compareCollectTaskDtos){
                if(compareCollectTaskDto.getCollectStatus() != CollectTaskState.done){
                    res = false;
                    break;
                }
            }
        }

        if(res){
            compareTask.setState(CompareState.COLLECTSUCCESS);
        }

        //多个数据源同时导入数据新开事务
        transactionUtils.executeInNewTransaction(new TransactionCallback() {
            @Override
            public void execute() {
                getBaseDao().save(compareTask);
            }
        });

        return acctNo.size() + "";
    }

//    @Transactional
    @Override
    public void importData(Long taskId, Long dataSourceId, MultipartFile file) {
        CompareTaskDto compareTaskDto = findById(taskId);
        DataSourceDto dataSourceDto = dataSourceService.findById(dataSourceId);
        DataImporter dataImporter;
        if (StringUtils.isBlank(dataSourceDto.getCode())) {
            dataImporter = dataImporters.get("default" + "Compare" + DataImporter.class.getSimpleName());
        } else {
            dataImporter = dataImporters.get(dataSourceDto.getCode() + "Compare" + DataImporter.class.getSimpleName());
            if (dataImporter == null) {
                dataImporter = dataImporters.get("default" + "Compare" + DataImporter.class.getSimpleName());
            }
        }
        dataImporter.importData(compareTaskDto, dataSourceDto, file);
    }

    @Override
    public IExcelExport exportSaicCompareData(Long taskId) {
        //根据任务找出其他比对的数据源
        //根据其他数据源的信息找出账号，企业名称   导出企业名称
        //如果没有其他的数据源提示报错   有其他的数据源并没有数据的导入进行报错，或导出空excel表格
        IExcelExport saicInfoCompareExport = new SaicInfoCompareExport();
        List<SaicInfoComparePoi> SaicInfoComparePoiList = new ArrayList<SaicInfoComparePoi>();
        CompareTaskDto compareTaskDto = findById(taskId);
        compareDataConvertService.update();
        List<CompareRuleDataSourceDto> compareRuleDataSourceDtoList = compareRuleDataSourceService.findByCompareRuleId(compareTaskDto.getCompareRuleId());
        if (compareRuleDataSourceDtoList.size() == 1) {
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "请添加其他数据源并导入数据......");
        }
        DataSourceDto saicDataSource = null;
        for (CompareRuleDataSourceDto compareRuleDataSourceDto : compareRuleDataSourceDtoList) {
            DataSourceDto dataSourceDto = dataSourceService.findById(compareRuleDataSourceDto.getDataSourceId());
            if (dataSourceDto.getName().contains("工商")) {
                saicDataSource = dataSourceDto;
                break;
            }
        }

        for (CompareRuleDataSourceDto compareRuleDataSourceDto : compareRuleDataSourceDtoList) {
            DataSourceDto dataSourceDto = dataSourceService.findById(compareRuleDataSourceDto.getDataSourceId());
            List<CompareData> list = null;
            if (compareRuleDataSourceDto.getActive()) {
                CompareDataRepository<CompareData> repository = compareRepositoryFinder.getRepository(dataSourceDto.getDomain());
                list = repository.findByTaskId(taskId);
                repository.delete(list);
                for (CompareData compareData : list) {
                    SaicInfoComparePoi saicInfoComparePoi = new SaicInfoComparePoi();
                    String acctNo = compareData.getAcctNo();
                    String depositorName = compareData.getDepositorName();
                    saicInfoComparePoi.setDepositorName(depositorName);
                    SaicInfoComparePoiList.add(saicInfoComparePoi);

                    //先查找该比对任务是否已经存在导入的工商数据，没有进行插入操作，存在进行acctNo，depositorName的覆盖更新
//                    CompareDataRepository<CompareData> saicRepositor = compareRepositoryFinder.getRepository(saicDataSource.getDomain());
//                    CompareData compareData1 = saicRepositor.findByTaskIdAndAcctNo(taskId, acctNo);
//                    if (compareData1 != null) {
//                        compareData1.setAcctNo(acctNo);
//                        compareData1.setDepositorName(depositorName);
//                        compareData1.setTaskId(taskId);
//                        saicRepositor.save(compareData1);
//                    } else {
                    CompareDataDto compareDataDto = new CompareDataDto();
                    compareDataDto.setAcctNo(acctNo);
                    compareDataDto.setDepositorName(depositorName);
                    compareDataDto.setTaskId(taskId);
                    compareDataDto.setOrganCode(compareData.getOrganCode());
                    compareDataService.saveCompareData(compareDataDto, saicDataSource);
//                    }
                }
            }
            //除工商数据源外的其他数据源已经导入数据
            if (list != null && list.size() > 0) {
                break;
            }
        }
        saicInfoCompareExport.setPoiList(SaicInfoComparePoiList);
        return saicInfoCompareExport;
    }

    @Override
    public void updateTaskCount(Long taskId, int count) {
        CompareTask task = getBaseDao().findOne(taskId);
        task.setCount(count);
        getBaseDao().save(task);
    }

    @Override
    public List<CompareTaskDto> findByCompareRuleId(Long ruleId) {
        List<CompareTaskDto> list = ConverterService.convertToList(getBaseDao().findByCompareRuleId(ruleId), CompareTaskDto.class);
        return list;
    }

    @Override
    public CompareTaskDto findByTaskIdFromApplication(Long taskId) {
        CompareTaskDto taskDto = new CompareTaskDto();
        ServletContext application = session.getServletContext();
        Object num = application.getAttribute("compare-num-" + taskId);
        Object status = application.getAttribute("compare-num-" + taskId);
        taskDto.setProcessed(num == null ? 0 : (int) num);
        taskDto.setState(status == null ? null : CompareState.valueOf(status.toString()));
        return taskDto;
    }

    @Override
    public ResultDto comapreReset(Long taskId) {
        ResultDto resultDto = new ResultDto();
        CompareTaskDto compareTaskDto = findById(taskId);
        log.info("重置比对任务......");
        if (compareTaskDto != null) {
            try {
                if (compareTaskDto.getState() == CompareState.INIT || compareTaskDto.getState() == CompareState.COLLECTING
                        || compareTaskDto.getState() == CompareState.PAUSE || compareTaskDto.getState() == CompareState.COMPARING) {
                    resultDto.setCode(ResultCode.NACK);
                    resultDto.setMessage("任务比对状态无法重置！");
                    return resultDto;
                }
                compareTaskDto.setState(CompareState.INIT);
                compareTaskDto.setCompareStateCN(CompareState.INIT.getName());
                compareTaskDto.setProcessed(0);
                saveTask(compareTaskDto);
                //重置删除比对结果表数据
                compareResultService.deleteAllByCompareTaskId(taskId);
                resultDto.setCode(ResultCode.ACK);
                resultDto.setMessage("重置完成！");
            } catch (Exception e) {
                log.error("比对任务重置失败！", e);
                resultDto.setCode(ResultCode.NACK);
                resultDto.setMessage("比对任务重置失败！");
            }
        } else {
            resultDto.setCode(ResultCode.NACK);
            resultDto.setMessage("无法找到此比对任务！");
        }
        return resultDto;
    }

    @Override
    public ResultDto compareShutDown(Long taskId) {
        log.info("取消比对任务");
        ResultDto resultDto = new ResultDto();
        CompareTaskDto compareTaskDto = findById(taskId);
        try {
            if (compareTaskDto != null) {
                compareTaskDto.setState(CompareState.CANCEL);
                compareTaskDto.setCompareStateCN(CompareState.CANCEL.getName());
                compareTaskDto.setProcessed(0);
                saveTask(compareTaskDto);
                resultDto.setCode(ResultCode.ACK);
            } else {
                resultDto.setCode(ResultCode.NACK);
                resultDto.setMessage("无法找到此比对任务！");
            }
        } catch (Exception e) {
            log.error("比对任务取消失败！", e);
            resultDto.setCode(ResultCode.NACK);
        }
        return resultDto;
    }

    @Override
    public ResultDto doCompareBefore(Long taskId) {
        ResultDto resultDto = new ResultDto();
        CompareTaskDto compareTaskDto = findById(taskId);
        Long compareRuleId = compareTaskDto.getCompareRuleId();
        //找出比对任务所涉及的数据源
        List<CompareRuleDataSourceDto> ruleDataSourceDtos = compareRuleDataSourceService.findByCompareRuleId(compareRuleId);
        //循环使用数据源  没有数据的数据源提示采集或导入数据
        for (CompareRuleDataSourceDto compareRuleDataSourceDto : ruleDataSourceDtos) {
            if (compareRuleDataSourceDto.getActive()) {
                DataSourceDto dataSourceDto = dataSourceService.findById(compareRuleDataSourceDto.getDataSourceId());
                CompareDataRepository<CompareData> repository = compareRepositoryFinder.getRepository(dataSourceDto.getDomain());
                List<CompareData> dataList = repository.findByTaskId(taskId);
                if (CollectionUtils.isEmpty(dataList)) {
                    //20190605 海峡银行要求数据没有也可以比对，如果恢复则直接去掉注释
                    //resultDto.setCode(ResultCode.NACK);
                    //resultDto.setMessage("数据源" + dataSourceDto.getName() + "还未导入或采集数据！");
                    //return resultDto;
                    log.info("数据源【{}】还未导入或采集数据,目前数据量为：{}",dataSourceDto.getName(),0);
                }
            }
        }
        List<CompareCollectTaskDto> compareCollectTaskDtoList = compareCollectTaskService.findByCompareTaskId(taskId);
        for (CompareCollectTaskDto c : compareCollectTaskDtoList){
            if (c.getCollectStatus()!= CollectTaskState.done){
                resultDto.setCode(ResultCode.NACK);
                resultDto.setMessage("采集数据未完成！");
                return resultDto;
            }
        }
        resultDto.setCode(ResultCode.ACK);
        return resultDto;
    }

    @Override
    public ResultDto checkCompareTaskUser(Long taskId) {
        ResultDto resultDto = new ResultDto();
        CompareTaskDto compareTaskDto = findById(taskId);
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId.equals(Long.parseLong(compareTaskDto.getCreatedBy()))) {
            resultDto.setCode(ResultCode.ACK);
            return resultDto;
        }
        return ResultDtoFactory.toNack("创建用户跟登录用户不是同一个用户,不允许操作......");
    }

    @Override
    public void deleteTask(Long taskId) {
        CompareTaskDto compareTaskDto = findById(taskId);
        Long userId = SecurityUtils.getCurrentUserId();
        if(userId.equals(Long.parseLong(compareTaskDto.getCreatedBy()))){
            deleteById(taskId);
        }else {
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR,  "创建用户跟登录用户不是同一个用户,不允许操作......");
        }
    }

    @Override
    public void updateCompareTaskState(Long taskId) {
        //查找出比对任务勾选的数据源
        boolean res = true;
        List<CompareRuleDataSourceDto> compareRuleDataSourceDtos = new ArrayList<>();
        //找出比对任务涉及到的数据源
        CompareTaskDto compareTaskDto = findById(taskId);
        List<CompareRuleDataSourceDto> compareRuleDataSources = compareRuleDataSourceService.findByCompareRuleId(compareTaskDto.getCompareRuleId());
        for(CompareRuleDataSourceDto compareRuleDataSourceDtom: compareRuleDataSources){
            if(compareRuleDataSourceDtom.getActive()){
                compareRuleDataSourceDtos.add(compareRuleDataSourceDtom);
            }
        }
        log.info("比对任务涉及的数据源数量为：" + compareRuleDataSourceDtos.size());

        List<CompareCollectTaskDto> compareCollectTaskDtos = compareCollectTaskService.findByCompareTaskId(taskId);
        log.info("比对任务采集表中的数量为：" + compareCollectTaskDtos.size());
        if(compareCollectTaskDtos.size() != compareRuleDataSourceDtos.size()){
            res = false;
        }else{
            for(CompareCollectTaskDto compareCollectTaskDto : compareCollectTaskDtos){
                if(compareCollectTaskDto.getCollectStatus() != CollectTaskState.done){
                    res = false;
                    break;
                }
            }
        }
        //如果采集或导入的数据咩有采集完成，比对任务状态不变，等待采集完成后进行比对任务状态的变更
        if(res){
            CompareTaskDto compareTaskDto1 = findById(taskId);
            if(compareTaskDto1 != null){
                compareTaskDto1.setState(CompareState.COLLECTSUCCESS);
                saveTask(compareTaskDto1);
            }
        }
    }

    @Override
    public Long createSaicCheakTask(TaskType taskType, TaskRate taskRate ,String startTime) {
        CompareTask task = new CompareTask();
        task.setCreatedBy("2");
        task.setCreateName("虚拟用户");
        task.setOrganFullId("1");
        task.setTime(DateUtils.getNowDateShort());
        task.setCreatedDate(new Date());
        task.setState(CompareState.INIT);
        task.setStateBeforePause(CompareState.INIT);
        task.setIsRunning(false);
        task.setIsPause(false);
        task.setCount(0);
        task.setProcessed(0);

        task.setTaskType(taskType);
        task.setRate(taskRate);
        task.setStartTime(DateUtils.DateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
        if (startTime!=null){
            task.setStartTime(startTime);
        }

        task.setName("工商异常校验"+System.currentTimeMillis());
        task.setCompareRuleId(1001L);//改成新的规则ID

        getBaseDao().save(task);
        log.info("创建工商异常校验任务[{}]成功",task.getId());
        return task.getId();
    }

    @Override
    public boolean deleteTimingTaskByNameLikeAndCreateName(String taskName,String createName){
        List<CompareTask> compareTaskList = getBaseDao().findAllByNameLikeAndCreateNameOrderByCreatedDateDesc(taskName,createName);
        getBaseDao().delete(compareTaskList);
        return true;
    }

    @Override
    public String getSaicStateForState(String state) {
        return SaicStateEnum.saicState2Enum(state).getName();
    }

    @Override
    public JSONObject dataDetailColumns(Long taskId) {

        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        CompareTaskDto compareTaskDto = findById(taskId);
        if (compareTaskDto == null) {
            log.info("根据taskId：" + taskId + "查找比对任务失败，无此比对任务......");
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "根据taskId：" + taskId + "查找比对任务失败，无此比对任务......");
        }
        //根据compareRuleId找到CompareRuleName
        CompareRuleDto compareRuleDto = compareRuleService.findById(compareTaskDto.getCompareRuleId());
        if (compareRuleDto != null) {
            String compareFields = "";
            //根据比对规则找出比对字段
            List<CompareRuleFieldsDto> compareRuleFieldsDtoList = compareRuleFieldsService.findByCompareRuleId(compareRuleDto.getId());
            if (CollectionUtils.isNotEmpty(compareRuleFieldsDtoList)) {
                for (CompareRuleFieldsDto compareRuleFieldsDto : compareRuleFieldsDtoList) {
                    CompareFieldDto compareFieldDto = compareFieldService.findById(compareRuleFieldsDto.getCompareFieldId());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", compareFieldDto.getId());
                    jsonObject.put("name", compareFieldDto.getName());
                    jsonObject.put("fieldName", compareFieldDto.getField());
                    array.add(jsonObject);
                }
            }
            obj.put("list", array);
        }
        return obj;
    }

    @Override
    public TableResultResponse<JSONObject> dataDetailList(Long taskId, Long dataSourceId, Pageable pageable) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> list = new ArrayList<String>();
        List<JSONObject> arrayList = new ArrayList<>();
        CompareTaskDto compareTaskDto = findById(taskId);
        CompareRuleDto compareRuleDto = compareRuleService.findById(compareTaskDto.getCompareRuleId());
        List<CompareRuleFieldsDto> compareRuleFieldsDtoList = compareRuleFieldsService.findByCompareRuleId(compareRuleDto.getId());
        if (CollectionUtils.isNotEmpty(compareRuleFieldsDtoList)) {
            for (CompareRuleFieldsDto compareRuleFieldsDto : compareRuleFieldsDtoList) {
                CompareFieldDto compareFieldDto = compareFieldService.findById(compareRuleFieldsDto.getCompareFieldId());
                list.add(compareFieldDto.getField());
            }
        }


        DataSourceDto dataSourceDto = dataSourceService.findById(dataSourceId);
        long count = compareDataService.getCompareData(taskId, dataSourceDto).size();
        List<CompareDataDto> dataList = compareDataService.getCompareData(taskId, dataSourceDto, pageable);
        if (CollectionUtils.isNotEmpty(list) && CollectionUtils.isNotEmpty(dataList)) {
            for (CompareDataDto compareData : dataList) {
                try {
                    dataMap = objectToMap(compareData);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", compareData.getId());
                    for (String field : list) {
                        jsonObject.put(field, dataMap.get(field));
                    }
                    arrayList.add(jsonObject);
                } catch (Exception e) {

                }
            }
        }
        return new TableResultResponse<JSONObject>((int) count, arrayList);
    }

    @Override
    public ResultDto checkDataSourceImporter(Long taskId) {
        boolean res = false;
        ResultDto resultDto = new ResultDto();
        CompareTaskDto compareTaskDto = findById(taskId);
        List<CompareRuleDataSourceDto> compareRuleDataSourceDtoList = compareRuleDataSourceService.findByCompareRuleId(compareTaskDto.getCompareRuleId());
        if (compareRuleDataSourceDtoList.size() == 1) {
            throw new BizServiceException(EErrorCode.SYSTEM_ERROR, "请添加其他数据源并导入数据......");
        }
        List<CompareData> list = null;
        for (CompareRuleDataSourceDto compareRuleDataSourceDto : compareRuleDataSourceDtoList) {
            DataSourceDto dataSourceDto = dataSourceService.findById(compareRuleDataSourceDto.getDataSourceId());
            if (dataSourceDto.getName().contains("工商")) {
                continue;
            }
            if (compareRuleDataSourceDto.getActive()) {
                CompareDataRepository<CompareData> repository = compareRepositoryFinder.getRepository(dataSourceDto.getDomain());
                list = repository.findByTaskId(taskId);
                if (list.size() > 0) {
                    res = true;
                    break;
                }
            }
        }
        if (res) {
            resultDto.setCode(ResultCode.ACK);
        } else {
            resultDto.setCode(ResultCode.NACK);
            resultDto.setMessage("请先导入或采集除工商数据源外的其他数据......");
        }
        return resultDto;
    }

    @Override
    public TableResultResponse<CompareCollectRecordDto> importDetailList(CompareCollectRecordDto
                                                                                 compareCollectRecordDto, Pageable pageable) {
        List<CompareCollectRecordDto> list = compareCollectRecordService.findByTaskId(compareCollectRecordDto, pageable);
        long count = compareCollectRecordService.findByTaskId(compareCollectRecordDto);
        return new TableResultResponse<CompareCollectRecordDto>((int) count, list);
    }

    @Override
    public List<CompareTaskDto> findByTaskTypeAndStateIn(TaskType taskType, CompareState... state) {
        return ConverterService.convertToList(getBaseDao().findByTaskTypeAndStateIn(taskType, state), CompareTaskDto.class);
    }

    @Override
    public Map<Long, CompareRuleDataSourceDto> findCompareRuleDataSourceDtoMapByTaskId(Long taskId) {
        CompareTaskDto compareTaskDto = findById(taskId);
        List<CompareRuleDataSourceDto> compareRuleDataSourceDtosList = compareRuleDataSourceService.findByCompareRuleId(compareTaskDto.getCompareRuleId());
        Map<Long, CompareRuleDataSourceDto> map = new HashMap<>();
        for (CompareRuleDataSourceDto compareRuleDataSourceDto : compareRuleDataSourceDtosList) {
            map.put(compareRuleDataSourceDto.getDataSourceId(), compareRuleDataSourceDto);
        }
        return map;
    }

    @Override
    public List<DataSourceDto> findParentDataSourceDtoByTaskIdAndDataSourceId(Long taskId, Long dataSourceId) {
        CompareTaskDto compareTaskDto = findById(taskId);
        CompareRuleDataSourceDto compareRuleDataSourceDto = compareRuleDataSourceService.findByCompareRuleIdAndDataSourceId(compareTaskDto.getCompareRuleId(), dataSourceId);
        List<DataSourceDto> dataSourceDtos = new ArrayList<>();
        String parentDataSourceIdAll = compareRuleDataSourceDto.getParentDataSourceIds();
        if (!StringUtils.isBlank(parentDataSourceIdAll)) {
            String[] parentDataSourceIds = StringUtils.split(parentDataSourceIdAll, ",");
            for (String parentDataSourceId : parentDataSourceIds) {
                DataSourceDto dataSourceDtoParent = dataSourceService.findById(Long.valueOf(parentDataSourceId));
                dataSourceDtos.add(dataSourceDtoParent);
            }
        }
        return dataSourceDtos;
    }

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    /**
     * 获取比对任务的状态
     *
     * @param taskId
     * @return
     */
    private CompareState getCompareStatus(Long taskId) {
        CompareTask compareTask = getBaseDao().findOne(taskId);
        log.info("比对任务当前状态：" + compareTask.getState());
        return compareTask.getState();
    }

    /**
     *  工商异常校验,同时保存校验结果。
     *  校验内容：
     * 1、（是否）严重违法、
     * 2、（是否）经营异常 、
     * 3、（是否）营业到期、
     * 4、 工商状态 、
     * 5、（是否）工商登记信息异动
     * 6、客户是否异常
     */
    private void saicAbnormalCheck(CompareResultDto compareResultDto) {

        SaicIdpInfo saicInfo = null;
        Boolean flag = false;//客户是否异常标记
        if(StringUtils.isNotBlank(compareResultDto.getDepositorName())){//先取本地的存款人
            saicInfo = saicInfoService.getSaicInfoBaseLocal(compareResultDto.getDepositorName());
        }
        if (saicInfo!=null){
            CompareResultSaicCheck compareResultSaicCheck = new CompareResultSaicCheck(compareResultDto.getCompareTaskId(),compareResultDto.getId(),saicInfo.getId());
            //1、（是否）严重违法、
            if (null!=saicInfo.getIllegals() && saicInfo.getIllegals().size()!= 0){
                compareResultSaicCheck.setIllegal(true);
                flag = true;
            }
            //2、（是否）经营异常 、
            if (null != saicInfo.getChangemess() && saicInfo.getChangemess().size()!= 0){
                compareResultSaicCheck.setChangeMess(true);
                flag = true;
            }
            //3、（是否）营业到期、
            try {
                if (DateUtils.isMinuteAfter(DateUtils.parseDate(saicInfo.getEnddate()))){
                    compareResultSaicCheck.setBusinessExpires(true);
                    flag = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                log.warn("是否营业到期校验失败");
            }
            //4、工商状态 、
            if (null != saicInfo.getState()){
                compareResultSaicCheck.setSaicState(SaicStateEnum.saicState2Enum(saicInfo.getState()));
                if (compareResultSaicCheck.getSaicState()!=SaicStateEnum.SUBSIST && compareResultSaicCheck.getSaicState()!=SaicStateEnum.EMPLOYED){
                    flag = true;
                    compareResultSaicCheck.setAbnormalState(true);
                }
            }

            //5、工商登记信息异动
            if (!compareResultDto.getMatch()){
                compareResultSaicCheck.setChanged(true);
                flag = true;
            }

            //6、客户是否异常
            if (flag){
                compareResultSaicCheck.setAbnormal(true);
            }

            //7、保存工商异常校验结果信息
            compareResultSaicCheck.setDepositorName(compareResultDto.getDepositorName());//存款人名称、企业名称
            compareResultSaicCheck.setOrganFullId(compareResultDto.getOrganFullId());//组织机构id
            compareResultSaicCheck.setAbnormalTime(DateUtils.getNowDateShort());

            OrganizationDto organizationDto = organizationService.findByOrganFullId(compareResultDto.getOrganFullId());
            if (organizationDto!=null){
                compareResultSaicCheck.setOrganName(organizationDto.getName());//组织机构名称
                compareResultSaicCheck.setCode(organizationDto.getCode());//组织机构代码
            }
            CompareResultSaicCheckDto checkDto = new CompareResultSaicCheckDto();
            ConverterService.convert(compareResultSaicCheck,checkDto);
            checkDto = compareResultSaicCheckService.save(checkDto);

            //8、生成工商异常校验结果信息Excel

            if (saveSaicAbnormalCheckExcel(checkDto,compareResultDto,saicInfo)){
                log.info(compareResultDto.getDepositorName()+"工商异常校验Excel生成成功");
            }else {
                log.warn(compareResultDto.getDepositorName()+"工商异常校验Excel生成失败");
            }

        }else {
            log.info("企业："+compareResultDto.getDepositorName()+"，本地无工商信息，无法进行工商异常校验。");
        }

    }

    /**
     * 本地生成客户的工商校验结果的Excel
     */
    private boolean saveSaicAbnormalCheckExcel(CompareResultSaicCheckDto compareResultSaicCheckDto,CompareResultDto compareResultDto,SaicIdpInfo saicInfo){

        try {
            HSSFWorkbook workbook = customerAbnormalService.getBaseInfoWorkbook(saicInfo,null);
            workbook = customerAbnormalService.getIllegalsWorkbook(saicInfo,workbook);
            workbook = customerAbnormalService.getChangeMessWorkbook(saicInfo,workbook);
            workbook = customerAbnormalService.getBusinessExpiresWorkbook(saicInfo,compareResultSaicCheckDto,workbook);
            workbook = customerAbnormalService.getSaicStateWorkbook(compareResultSaicCheckDto,workbook);
            workbook = customerAbnormalService.getChangedWorkbook(compareResultSaicCheckDto,compareResultDto,workbook);

            //判断路径是否存在
            File path = new File(filePath+File.separator + compareResultDto.getCompareTaskId());
            if (!path.exists()){
                path.mkdirs();
            }

            File file = new File(filePath+File.separator + compareResultDto.getCompareTaskId()+File.separator+saicInfo.getName()+".xls");
            workbook.write(file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void packExcel(Long taskId) {

        File[] listFiles = new File(filePath+File.separator + taskId).listFiles();
        List<File> listFile = new ArrayList<File>();
        ZipOutputStream zipOutputStream = null;
        BufferedInputStream bis = null;
        String fileName = "";
        try {
            if (listFiles == null){
                log.warn("没有客户异动信息，无法打包！");
                return;
            }
            for (File f : listFiles) {
                if (f.getName().contains("xls")) {
                    listFile.add(f);
                }
            }

            String zipName = filePath+File.separator + taskId + File.separator+System.currentTimeMillis()+".zip";
            zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(zipName)));
            int buffersize = 8 * 1024;
            byte[] data = new byte[buffersize];

            for (File file : listFile) {
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                bis = new BufferedInputStream(new FileInputStream(file));
                while ((bis.read(data)) != -1) {
                    zipOutputStream.write(data, 0, data.length);
                }
                bis.close();
            }

            zipOutputStream.close();

        } catch (Exception e) {
            log.warn("生成压缩文件异常！");
        }finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(zipOutputStream);
        }
    }
}



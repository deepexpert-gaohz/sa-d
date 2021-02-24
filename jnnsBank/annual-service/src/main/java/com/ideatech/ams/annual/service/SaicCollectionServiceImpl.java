package com.ideatech.ams.annual.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.ideatech.ams.system.proof.service.ProofReportService;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.annual.dao.*;
import com.ideatech.ams.annual.dto.*;
import com.ideatech.ams.annual.dto.poi.AnnualCompanyPoi;
import com.ideatech.ams.annual.entity.*;
import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.ams.annual.enums.CollectTaskState;
import com.ideatech.ams.annual.enums.CollectType;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.ams.annual.executor.SaicCollectTaskExecutor;
import com.ideatech.ams.annual.service.poi.AnnualCompanyRecordExport;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.kyc.dto.OutInfoOne;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.kyc.service.SaicRequestService;
import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.BeanUtil;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.criteria.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Description 工商数据采集
 * @Author wanghongjie
 * @Date 2018/8/8
 **/
@Service
//@Transactional
@Slf4j
public class SaicCollectionServiceImpl implements SaicCollectionService{


    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private FetchSaicInfoService fetchSaicInfoService;

    @Autowired
    private CollectTaskDao collectTaskDao;

    @Autowired
    private ConfigService configService;

    @Autowired
    private FetchPbcDao fetchPbcDao;

    @Autowired
    private TransactionUtils transactionUtils;

    @Autowired
    private SaicRequestService saicRequestService;

    @Autowired
    private SaicInfoService saicInfoService;

    @Autowired
    private ThreadPoolTaskExecutor annualExecutor;

    @Autowired
    private PbcCollectAccountDao pbcCollectAccountDao;

    @Autowired
    private CoreCollectionDao coreCollectionDao;

    @Autowired
    private CollectConfigService collectConfigService;

    @Autowired
    private SaicCollectionDao saicCollectionDao;

    @Autowired
    private OrganizationService organizationService;

    /**
     * 工商数据最大采集线程数
     */
    @Value("${ams.saicCollection.executor.num:5}")
    private int saicCollectExecutorNum;

    @Value("${ams.saicCollection.retryLimit:3}")
    private int retryLimit = 3;

    @Value("${ams.annual.saic-file-location}")
    private String saicFileLocation;

    @Value("${ams.annual.saic-file-location-finish}")
    private String saicFileLocationFinish;

    @Value("${ams.annual.saicLocal:true}")
    private Boolean saicLocal;

    private Map<String, Object> existSaicMap = null;

    public static boolean manualPause = false;

    private Boolean saicCollectPause;

    private CollectConfigDto collectConfig;

    private List<Future<Long>> futureList;

    @Autowired
    private AnnualTaskDao annualTaskDao;

    @Autowired
    private AnnualResultDao annualResultDao;

    @Autowired
    private AnnualResultService annualResultService;

    @Autowired
    private CollectTaskService collectTaskService;

    @Autowired
    private AnnualTaskService annualTaskService;

    @Autowired
    private SaicMonitorService saicMonitorService;

    @Autowired
    private UserService userService;

    private boolean endFutureFlag;

    @Value("${ams.company.writeMoney}")
    private boolean writeMoney;
    @Autowired
    private ProofReportService proofReportService;

    public static final String FILE_EXTENSION[] = {"json","JSON"};

    @Override
    public List<FetchSaicInfoDto> getAll(Long taskId) {
        List<FetchSaicInfo> list = saicCollectionDao.findByAnnualTaskId(taskId);
        List<FetchSaicInfoDto> dtoList = new ArrayList<FetchSaicInfoDto>();
        if (CollectionUtils.isNotEmpty(list)) {
            FetchSaicInfoDto dto = null;
            for (FetchSaicInfo info : list) {
                dto = new FetchSaicInfoDto();
                BeanCopierUtils.copyProperties(info, dto);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    public void collect(Long annualTaskId) {
        collect(CollectType.AFRESH,annualTaskId);
    }

    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void collect(CollectType collectType,Long annualTaskId) {
        endFutureFlag = false;
        CollectConfigDto collectConfigDto = collectConfigService.findByAnnualTaskId(annualTaskId);
        if(collectConfigDto != null){
            saicCollectPause = collectConfigDto.isSaicUnlimited();
        }else{
            saicCollectPause = false;
        }

        // 如果不是继续采集，则重新生成采集列表
        if (collectType != CollectType.CONTINUE) {
            createNewCollectTask(collectType,annualTaskId);

            //清除线程
            clearFuture();
        }
        CollectTaskDto collectTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum.SAIC,annualTaskId);
        if (collectTaskDto != null) {
            Long collectTaskId = collectTaskDto.getId();
            // 采集任务状态设置为开始
            saveTaskCollecting(collectTaskId);
            try {
                startFetchSaicInfo(collectTaskId, collectType,annualTaskId);
                // 状态修改保存
                if(!endFutureFlag){
                    saveTask(collectTaskId);
                }else{
                    log.info("强制重置");
                }
//                // 保存
            } catch (Exception e) {
                log.error("在线采集工商数据异常", e);
            }
        } else {
            throw new EacException("当前没有采集工商任务，请先创建任务。。");
        }
    }

    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void collectReset(CollectType collectType,Long annualTaskId) {
        endFutureFlag = false;
        CollectConfigDto collectConfigDto = collectConfigService.findByAnnualTaskId(annualTaskId);
        saicCollectPause = false;

        CollectTaskDto collectTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.SAIC, annualTaskId);

        if(collectTaskDto == null || collectTaskDto.getIsCompleted() == CompanyIfType.No){
            throw new EacException("工商采集任务未完成，无法进行失败重新采集。。。");
        }
        //清除线程
        clearFuture();

        if (collectTaskDto != null) {
            Long collectTaskId = collectTaskDto.getId();
            // 采集任务状态设置为开始
            saveTaskCollecting(collectTaskId);
            // 清空fail的数量
            clearFailCount(collectTaskId);
            try {
                startFetchSaicInfo(collectTaskId, collectType,annualTaskId);
                // 状态修改保存
                if(!endFutureFlag){
                    saveTask(collectTaskId);
                }else{
                    log.info("强制重置");
                }
//                // 保存
            } catch (Exception e) {
                log.error("在线采集工商数据异常", e);
            }
        } else {
            throw new EacException("当前没有采集工商任务，请先创建任务。。");
        }
    }

    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void collectByFile(CollectType collectType,Long annualTaskId) {
        endFutureFlag = false;
        // 如果不是继续采集，则重新生成采集列表
        if (collectType != CollectType.CONTINUE) {
            createNewCollectTask(collectType,annualTaskId);
        }
        CollectTaskDto collectTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum.SAIC,annualTaskId);
        if (collectTaskDto != null) {
            Long collectTaskId = collectTaskDto.getId();
            // 采集任务状态设置为开始
            saveTaskCollecting(collectTaskId);
            try {
                startFetchSaicInfoByFile(collectTaskId, collectType,annualTaskId);
                // 状态修改保存
                if(!endFutureFlag){
                    saveTask(collectTaskId);
                }else{
                    log.info("强制重置");
                }
//                // 保存
            } catch (Exception e) {
                log.error("采集工商数据异常", e);
            }
        } else {
            throw new EacException("当前没有采集工商任务，请先创建任务。。");
        }
    }


    @Override
    public SaicCollectResultSearchDto search(final SaicCollectResultSearchDto saicCollectResultSearchDto, final Long taskId) {
        Specification<FetchSaicInfo> specification = new Specification<FetchSaicInfo>() {
            @Override
            public Predicate toPredicate(Root<FetchSaicInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (null != saicCollectResultSearchDto) {

                    if(saicCollectResultSearchDto.getCollectState() != null){
                        expressions.add(cb.equal(root.get("collectState"),saicCollectResultSearchDto.getCollectState()));
                    }

                    if (StringUtils.isNotBlank(saicCollectResultSearchDto.getCustomerName())) {
                        expressions.add(cb.like(root.<String>get("customerName"), "%" + saicCollectResultSearchDto.getCustomerName() + "%"));
                    }

                    if (StringUtils.isNotBlank(saicCollectResultSearchDto.getState())) {
                        expressions.add(cb.like(root.<String>get("state"), "%" + saicCollectResultSearchDto.getState() + "%"));
                    }
                }

                expressions.add(cb.equal(root.<Long>get("annualTaskId"),taskId));
                return predicate;
            }
        };
        Page<FetchSaicInfo> all = saicCollectionDao.findAll(specification, new PageRequest(Math.max(saicCollectResultSearchDto.getOffset(), 0), saicCollectResultSearchDto.getLimit(),new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate"))));
        List<FetchSaicInfoDto> fetchSaicInfoDtos = ConverterService.convertToList(all.getContent(), FetchSaicInfoDto.class);
        saicCollectResultSearchDto.setList(fetchSaicInfoDtos);
        saicCollectResultSearchDto.setTotalRecord(all.getTotalElements());
        saicCollectResultSearchDto.setTotalPages(all.getTotalPages());
        return saicCollectResultSearchDto;
    }
    @Override
    public int checkSaicCollectTaskStatus(Long taskId) {
        CollectTaskDto collectTaskForCodeDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.CORE, taskId);
        CollectTaskDto collectTaskForPbcDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.PBC, taskId);
        CollectTaskDto collectTaskForSaicDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.SAIC, taskId);
        boolean flag = false;
        if(collectTaskForCodeDto == null || collectTaskForCodeDto.getCollectStatus() != CollectTaskState.done){
            return 0;
//            return new ObjectRestResponse<String>().rel(false).msg("核心导入未完成");
        }
        if(collectTaskForPbcDto == null || collectTaskForPbcDto.getCollectStatus() == CollectTaskState.init || collectTaskForPbcDto.getCollectStatus() == CollectTaskState.fail ||
                (collectTaskForPbcDto.getCollectStatus() == CollectTaskState.collecting && collectTaskForPbcDto.getCount()==0) ||
                (PbcCollectionServiceImpl.finishFirstCollecting == false && collectTaskForPbcDto.getCollectStatus() == CollectTaskState.collecting
                ) ){
            return 1;
//            return new ObjectRestResponse<String>().rel(false).msg("人行数据未解析完成");
        }
        if(collectTaskForSaicDto != null){
            if(collectTaskForSaicDto.getIsCompleted() == CompanyIfType.No){//未完成
                return 2;
            }else{//已完成
                return 22;
            }
//            return new ObjectRestResponse<String>().rel(false).msg("工商采集已经开启");
        }
        CollectConfigDto collectConfigDto = collectConfigService.findByAnnualTaskId(taskId);
        if (collectConfigDto == null) {
            return 5;//未配置工商采集方式
        }
        if (collectConfigDto.isSaicUnlimited()) {
            return 3;//可以导入文件采集
        } else {
            if (StringUtils.isBlank(collectConfigDto.getSaicStartDate())) {
                return 4;//可以在线采集（手动点击开始）
            } else {
                if (DateUtils.isDayAfter(collectConfigDto.getSaicStartDate(), "yyyy-MM-dd HH:mm:ss")) {
                    return 7;//定时执行时间小于当前时间，系统正在执行采集操作或已执行完毕
                } else {
                    return 8;//定时采集任务正在等待执行
                }
            }
        }
//        if (collectConfigDto != null) {
//            if (!collectConfigDto.isSaicUnlimited() && StringUtils.isNotBlank(collectConfigDto.getSaicStartDate()) && DateUtils.isDayAfter(collectConfigDto.getSaicStartDate(), "yyyy-MM-dd HH:mm:ss")) {
//                return 7;//定时执行时间小于当前时间，系统正在执行采集操作或已执行完毕
//            } else if (!collectConfigDto.isSaicUnlimited() && StringUtils.isNotBlank(collectConfigDto.getSaicStartDate()) && !DateUtils.isDayAfter(collectConfigDto.getSaicStartDate(), "yyyy-MM-dd HH:mm:ss")) {//定时执行时间大于当前时间
//                return 8;//定时采集任务正在等待执行
//            } else if (!collectConfigDto.isSaicUnlimited() && StringUtils.isBlank(collectConfigDto.getSaicStartDate())) {
//                return 4;//可以在线采集（手动点击开始）
//            } else if (collectConfigDto.isSaicUnlimited()) {
//                return 3;//可以导入文件采集
//            } else {
//                return 6;//其他，不可做任何
//            }
//        } else {
//            return 5;//未配置
//        }
    }

    @Override
    public void clearFuture(){
        if(futureList !=null && futureList.size()>0){
            for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext();) {
                Future<Long> future = iterator.next();
                if(future.isDone()){
                    iterator.remove();
                }else{
                    future.cancel(true);
                    iterator.remove();
                }
            }
        }

        futureList = new ArrayList<Future<Long>>();
    }

    @Override
    public void endFuture(){
        endFutureFlag = true;
    }

    private void saveTaskCollecting(Long collectTaskId){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CollectTask collectTask = collectTaskDao.findById(collectTaskId);
        collectTask.setCollectStatus(CollectTaskState.collecting);
        collectTask.setIsCompleted(CompanyIfType.No);
        collectTaskDao.save(collectTask);
        transactionManager.commit(transaction);
    }

    /**
     * 清空数据
     * @param collectTaskId
     */
    private void clearFailCount(Long collectTaskId){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CollectTask collectTask = collectTaskDao.findById(collectTaskId);
        collectTask.setFailed(0);
        collectTaskDao.save(collectTask);
        transactionManager.commit(transaction);
    }

    private void createNewCollectTask(CollectType collectType,Long annualTaskId) {

        Long startTime = System.currentTimeMillis();
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        // 开始新任务前需要删除原先采集任务列表
        cleanAllCollectList();
        log.info("开始新任务前删除原先工商采集列表");
        // 结束之前所有的采集任务
        finishAllCollectTask();
        log.info("结束之前的工商采集任务");
        transactionManager.commit(transaction);
        transaction = transactionManager.getTransaction(definition);
        // 开启一个采集任务
        CollectTask collectTask = new CollectTask();
        AnnualTask annualTask = annualTaskDao.findById(annualTaskId);
        collectTask.setName(annualTask.getYear()+"年检工商数据采集任务");
        collectTask.setCollectTaskType(DataSourceEnum.SAIC);
        collectTask.setAnnualTaskId(annualTaskId);
        collectTask.setCollectStatus(CollectTaskState.init);
        collectTask.setStartTime(DateUtils.getDateTime());
        collectTask.setCount(0);
        collectTask.setProcessed(0);
        collectTask.setSuccessed(0);
        collectTask.setFailed(0);
        collectTaskDao.save(collectTask);
        log.info("创建一个工商采集任务");
        transactionManager.commit(transaction);
//        transaction = transactionManager.getTransaction(definition);
        // 将采集token存入采集列表里

        Map<String,String> map = new HashMap<String,String>();
        Map<String,String> otherMap = new HashMap<String,String>();
        log.info("开始融合核心和人行的数据");
        mixCoreAndPbc(annualTaskId,map,otherMap,true);
        log.info("融合核心和人行的数据(即核心与人行的并集)共：{}条",map.size());
        log.info("融合核心和人行的账户名称不一致数据(即核心与人交集的部分数据)共：{}条",otherMap.size());
//        transactionManager.commit(transaction);
//        System.gc();
        int successCount = 0;
        Set<String> acctNameSet = new HashSet<String>();
        FetchSaicInfoDto fetchSaicInfoDto = null;

        transaction = transactionManager.getTransaction(definition);
        int repeat = 0;
        for (Map.Entry<String, String> entry : map.entrySet()){
            String[] depositorNameAndRegNo = getDepositorNameAndRegNo(entry.getValue());
            try {
                // 如果已经存在则不保存
                if (acctNameSet.contains(depositorNameAndRegNo[0])) {
                    log.info("企业名称重复："+depositorNameAndRegNo[0]);
                    repeat++;
                    continue;
                }

                fetchSaicInfoDto = new FetchSaicInfoDto();
                fetchSaicInfoDto.setCollectTaskId(collectTask.getId());
                fetchSaicInfoDto.setCollectState(CollectState.init);
                fetchSaicInfoDto.setCustomerName(depositorNameAndRegNo[0]);
                fetchSaicInfoDto.setRegNo(depositorNameAndRegNo[1]);
                if(otherMap.containsKey(entry.getKey())){//当核心和人行的存款人不同时，插入到customerNameOther中
                    fetchSaicInfoDto.setCustomerNameOther(otherMap.get(entry.getKey()));
                }
//                fetchSaicInfoDto.setRegNo(result.getFileNo());
                fetchSaicInfoService.save(fetchSaicInfoDto);
                // 将企业名称保存到set里，以免加载重复的账户名称
                acctNameSet.add(depositorNameAndRegNo[0]);
                successCount++;
                if (successCount > 0 && successCount % 30 == 0) {
                    transactionManager.commit(transaction);
                    transaction = transactionManager.getTransaction(definition);
                    log.info("已保存工商数据" + successCount + "条");
                }

            } catch (Exception e) {
                log.error("收集需采集工商企业异常，企业名称" + depositorNameAndRegNo[0], e);
                if(!transaction.isCompleted()){
                    try{
                        transactionManager.rollback(transaction);
                    }catch (Exception e1){
                        log.error("收集需采集工商企业异常,回滚失败，企业名称" + depositorNameAndRegNo[0], e);
                    }
                }
            }
        }
        transactionManager.commit(transaction);
        transaction = transactionManager.getTransaction(definition);
        collectTask.setCount(acctNameSet.size());
        log.info("收集需采集工商企业数量" + acctNameSet.size()+",重复数据"+repeat);
        collectTaskDao.save(collectTask);
        transactionManager.commit(transaction);

        Long endTime = System.currentTimeMillis();
        log.info("采集初始化的时间耗时{}秒",(endTime - startTime)/1000);
    }

    private void cleanAllCollectList() {
        try {
            log.info("开始删除工商采集列表");
            fetchSaicInfoService.deleteAllInBatch();
            log.info("删除工商采集列表结束");
        } catch (Exception e) {
            log.error("删除工商采集列表异常", e);
        }
    }
    private void finishAllCollectTask() {
        try {
            List<CollectTask> collectTaskList = collectTaskDao.findByCollectTaskType(DataSourceEnum.SAIC);
            for (CollectTask collectTask : collectTaskList) {
                collectTask.setCollectStatus(CollectTaskState.done);
                collectTask.setIsCompleted(CompanyIfType.Yes);
                collectTaskDao.save(collectTask);
            }
        } catch (Exception e) {
            log.error("修改采集任务异常", e);
        }
    }


    private void startFetchSaicInfo(Long collectTaskId, CollectType collectType,Long annualTaskId) throws Exception {
        // 找出需采集任务里的列表
        List<FetchSaicInfoDto> fetchFailSaicInfoList = fetchSaicInfoService.findByCollectTaskIdAndCollectStateNot(collectTaskId, CollectState.success);

        Map<String, Object> fetchSaicMap = new HashMap<String, Object>(16);
        Set<String> tokens = new HashSet<String>(16);
        for (FetchSaicInfoDto fetchSaicInfoDto : fetchFailSaicInfoList) {
            fetchSaicMap.put(fetchSaicInfoDto.getCustomerName(), fetchSaicInfoDto);
            tokens.add(fetchSaicInfoDto.getCustomerName());
        }
        String price = "";
        ConfigDto configDto = configService.findOneByConfigKey("saicMoney");
        if(configDto!=null){
            price = configDto.getConfigValue();
        }

        if (CollectionUtils.isNotEmpty(tokens) && tokens.size() > 0) {
            Map<String, Set<String>> batchTokens = getBatchTokens(tokens);
            if (MapUtils.isNotEmpty(batchTokens) && batchTokens.size() > 0) {
                SaicCollectTaskExecutor executor = null;
                Map<String, Integer> fieldLengthForStringMap = BeanUtil.fieldMapForErrorAccount(FetchSaicInfo.class);
                for (String batch : batchTokens.keySet()) {
                    executor = new SaicCollectTaskExecutor(batchTokens.get(batch));
                    executor.setBatch(batch);
                    executor.setCollectType(collectType);
                    executor.setExistSaicMap(existSaicMap);
                    executor.setFetchSaicMap(fetchSaicMap);
                    executor.setRetryLimit(retryLimit);
                    executor.setSaicRequestService(saicRequestService);
                    executor.setSaicInfoService(saicInfoService);
//                    executor.setTokens(batchToken);
                    executor.setTransactionUtils(transactionUtils);
                    executor.setFetchSaicInfoService(fetchSaicInfoService);
                    executor.setCollectTaskId(collectTaskId);
                    executor.setCollectTaskDao(collectTaskDao);
                    executor.setTransactionManager(transactionManager);
                    executor.setAnnualTaskId(annualTaskId);
                    executor.setSaicCollectPause(saicCollectPause);
                    executor.setCollectConfig(collectConfig);
                    executor.setAnnualResultService(annualResultService);
                    executor.setStrLengthMap(fieldLengthForStringMap);
                    executor.setSaicLocal(saicLocal);
                    executor.setSaicMonitorService(saicMonitorService);
                    executor.setUserService(userService);
                    executor.setConfigService(configService);
                    executor.setProofReportService(proofReportService);
                    executor.setWriteMoney(writeMoney);
//                    executor = getExecutor(batchTokens.get(batch), batch, collectTaskId, collectType, existSaicMap, fetchSaicMap,annualTaskId,annualResultDao);
                    futureList.add(annualExecutor.submit(executor));
//                    taskExecutor.execute(executor);
                    log.info(batch + "启动，开始采集企业工商详细信息==================");
                }
            }
            valiCollectCompleted(collectTaskId);
            log.info("全部线程执行结束");
            tokens.clear();
            batchTokens.clear();
            System.gc();
        }
    }


    private void startFetchSaicInfoByFile(Long collectTaskId, CollectType collectType,Long annualTaskId) throws Exception {
        File folder = new File(saicFileLocation);
        if(folder.listFiles().length>0) {
            // 找出需采集任务里的列表
            List<FetchSaicInfoDto> fetchFailSaicInfoList = fetchSaicInfoService.findByCollectTaskIdAndCollectStateNot(collectTaskId, CollectState.success);

            Map<String, Object> fetchSaicMap = new HashMap<String, Object>(16);
            Set<String> tokens = new HashSet<String>(16);
            for (FetchSaicInfoDto fetchSaicInfoDto : fetchFailSaicInfoList) {
                String customerName = fetchSaicInfoDto.getCustomerName();
                fetchSaicMap.put(StringUtils.isNotBlank(customerName) ? customerName.trim() : customerName, fetchSaicInfoDto);
            }
            System.out.println("===============formMap    start======================");
            Iterator<Map.Entry<String, Object>> entries = fetchSaicMap.entrySet().iterator();
            log.info("fetchSaicMap:" + fetchSaicMap.size());
            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                log.info("Key = " + entry.getKey());
            }
            System.out.println("===============formMap    end======================");
            List<FetchSaicInfoDto> fetchSaicList = new ArrayList<FetchSaicInfoDto>(20);
            try {
                int i = 0;
                for (File file : folder.listFiles()) {
                    if (FilenameUtils.isExtension(file.getName(), FILE_EXTENSION)) {
                        log.info("--开始导入工商采集文件[" + file.getName() + "]--");
                        JSONReader reader = new JSONReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                        reader.startArray();
                        SaicIdpInfo saicInfo = null;
                        TransactionDefinition definition = new DefaultTransactionDefinition(
                                TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                        TransactionStatus transaction = transactionManager.getTransaction(definition);
                        while (reader.hasNext()) {
                            if (fetchSaicList.size() == 20) {
                                try {
                                    log.info("已经导入工商数据" + i + "条");
                                    for(FetchSaicInfoDto fetchSaicInfoDto : fetchSaicList){
                                        fetchSaicInfoService.save(fetchSaicInfoDto);
                                        annualResultService.updateAnnualResultData(annualTaskId, fetchSaicInfoDto);
                                    }
                                    CollectTask collectTaskNew = collectTaskDao.findOne(collectTaskId);
                                    collectTaskNew.setProcessed(i);
                                    collectTaskNew.setSuccessed(i);
                                    collectTaskDao.save(collectTaskNew);
                                    transactionManager.commit(transaction);
                                    fetchSaicList.clear();
                                } catch (Exception e) {
                                    log.error("提交工商数据失败", e);
                                } finally {
                                    transaction = transactionManager.getTransaction(definition);
                                }
                            }
                            try {
                                OutInfoOne outInfoOne = reader.readObject(OutInfoOne.class);
                                if(StringUtils.equalsIgnoreCase(outInfoOne.getStatus(), "success")){
                                    saicInfo = outInfoOne.getResult();
                                    FetchSaicInfoDto fetchSaicInfoDto = (FetchSaicInfoDto) fetchSaicMap.get(saicInfo.getName());
                                    if (fetchSaicMap.containsKey(saicInfo.getName())) {
                                        fetchSaicInfoDto.setEnddate(saicInfo.getEnddate());
                                        fetchSaicInfoDto.setState(saicInfo.getState());
                                        fetchSaicInfoDto.setUnitycreditcode(saicInfo.getUnitycreditcode());
                                        fetchSaicInfoDto.setCollectState(CollectState.success);
                                        fetchSaicInfoDto.setIdpJsonStr(JSON.toJSONString(saicInfo));
                                        i++;
                                        fetchSaicInfoDto.setAnnualTaskId(annualTaskId);
                                        fetchSaicList.add(fetchSaicInfoDto);
                                        fetchSaicMap.remove(saicInfo.getName());
//                                        fetchSaicInfoService.save(fetchSaicInfoDto);
//                                        annualResultService.updateAnnualResultData(annualTaskId, fetchSaicInfoDto);
                                    }else{
                                        log.info("saicInfoName：" + saicInfo.getName() + "无法找到对应的企业信息,内容为");
//                                        log.error("无法找到对应的企业信息，内容为" + jsonString);
                                    }
                                } else {
                                    log.error("获得工商信息失败，内容为" + reader.readString());
                                }
                            } catch (Exception e) {
                                log.error("导入工商数据处理异常", e);
                                transactionManager.rollback(transaction);
                                transaction = transactionManager.getTransaction(definition);
                            }

                        }
                        reader.endArray();
                        reader.close();
                        if(fetchSaicList.size()>0){
                            for(FetchSaicInfoDto fetchSaicInfoDto : fetchSaicList){
                                fetchSaicInfoService.save(fetchSaicInfoDto);
                                annualResultService.updateAnnualResultData(annualTaskId, fetchSaicInfoDto);
                            }
                            fetchSaicList.clear();
                        }
                        CollectTask collectTaskNew = collectTaskDao.findOne(collectTaskId);
                        collectTaskNew.setProcessed(collectTaskNew.getCount());
                        collectTaskNew.setSuccessed(i);
                        collectTaskNew.setFailed(collectTaskNew.getCount() - i);
                        collectTaskDao.save(collectTaskNew);
                        transactionManager.commit(transaction);
                    }

                    try {
                        FileUtils.moveFile(file, new File(saicFileLocationFinish + File.separator + file.getName()));
                    } catch (IOException e) {
                        try {
                            FileUtils.moveFile(file, new File(saicFileLocationFinish + File.separator + RandomStringUtils.randomNumeric(5) + file.getName()));
                        } catch (IOException e1) {
                            log.error("文件移动异常",e);
                        }
                    }
                }

                //所有文件采集完成,更新剩余数据
                fetchSaicInfoService.updateRemaining(collectTaskId,annualTaskId,"无工商数据导入");
            } catch (Exception e) {
                log.error("导入工商数据处理异常", e);
            }
        }else{
            throw new EacException("无采集文件，请先导入文件。。");
        }
    }


    /**
     * 判断采集是否完成
     *
     * @param taskId
     * @throws Exception
     */
    private void valiCollectCompleted(final Long taskId) throws Exception {
        while(futureList.size()>0){
            for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext();) {
                Future<Long> future = iterator.next();
                if(future.isDone()){
                    iterator.remove();
                }
            }
            log.info("是否采集结束判断，暂停1分钟......");
            // 暂停1分钟
            TimeUnit.MINUTES.sleep(1);
        }
    }


    private Map<String, Set<String>> getBatchTokens(Set<String> tokens) {
        Map<String, Set<String>> returnMap = new HashMap<String, Set<String>>(16);
        if (tokens != null && tokens.size() > 0) {
            int allLeafSum = tokens.size();
            int tokensNum = (allLeafSum / saicCollectExecutorNum) + 1;
            int num = 0;
            int batchNum = 0;
            Set<String> batchTokens = new HashSet<String>();
            for (String token : tokens) {
                if (num > 0 && num % tokensNum == 0) {
                    batchNum++;
                    returnMap.put("第" + batchNum + "线程", batchTokens);
                    batchTokens = new HashSet<String>();
                }
                batchTokens.add(token);
                num++;
            }
            returnMap.put("第" + (batchNum + 1) + "线程", batchTokens);
        }
        return returnMap;
    }

    private SaicCollectTaskExecutor getExecutor(Set<String> batchToken, String batch, Long collectTaskId, CollectType collectType, Map<String, Object> existSaicMap, Map<String, Object> fetchSaicMap,Long annualTaskId,AnnualResultDao annualResultDao) {
        SaicCollectTaskExecutor executor = new SaicCollectTaskExecutor(batchToken);
        executor.setBatch(batch);
        executor.setCollectType(collectType);
        executor.setExistSaicMap(existSaicMap);
        executor.setFetchSaicMap(fetchSaicMap);
        executor.setRetryLimit(retryLimit);
        executor.setSaicRequestService(saicRequestService);
        executor.setSaicInfoService(saicInfoService);
        executor.setTokens(batchToken);
        executor.setTransactionUtils(transactionUtils);
        executor.setFetchSaicInfoService(fetchSaicInfoService);
        executor.setCollectTaskId(collectTaskId);
        executor.setCollectTaskDao(collectTaskDao);
        executor.setTransactionManager(transactionManager);
        executor.setAnnualTaskId(annualTaskId);
        executor.setSaicCollectPause(saicCollectPause);
        executor.setCollectConfig(collectConfig);
        executor.setAnnualResultService(annualResultService);
        executor.setUserService(userService);
        return executor;
    }

    private void saveTask(Long collectTaskId) {
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//        ((DefaultTransactionDefinition) definition).setIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CollectTask collectTask = collectTaskDao.findOne(collectTaskId);
        collectTask.setCollectStatus(CollectTaskState.done);
        collectTask.setEndTime(DateUtils.getDateTime());
        collectTask.setIsCompleted(CompanyIfType.Yes);
//        if (collectTask.getCount() > 0 && collectTask.getProcessed() == collectTask.getCount()) {
//            collectTask.setIsCompleted(CompanyIfType.Yes);
//        }
        collectTaskDao.save(collectTask);
        transactionManager.commit(transaction);
    }

    /**
     * 计算导入文件的数量
     * @return
     */
    public Integer saicFileCounts(){
        File folder = new File(saicFileLocation);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File folderFinish = new File(saicFileLocationFinish);
        if (!folderFinish.exists()) {
            folderFinish.mkdirs();
        }

        int count=0;
        if(folder.listFiles().length>0){
            for (File file : folder.listFiles()) {
                if(FilenameUtils.isExtension(file.getName(), FILE_EXTENSION)){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 清空文件
     * @return
     */
    public void clearFiles() {
        File folder = new File(saicFileLocation);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File folderFinish = new File(saicFileLocationFinish);
        if (!folderFinish.exists()) {
            folderFinish.mkdirs();
        }

        if(folder.listFiles().length>0){
            for (File file : folder.listFiles()) {
                try {
                    FileUtils.moveFile(file, new File(saicFileLocationFinish + File.separator + file.getName()));
                } catch (IOException e) {
                    try {
                        FileUtils.moveFile(file, new File(saicFileLocationFinish + File.separator + RandomStringUtils.randomNumeric(5) + file.getName()));
                    } catch (IOException e1) {
                        log.error("文件移动异常",e);
                    }
                }
            }
        }
    }

    /**
     * 年检企业列表excel
     * @param taskId
     * @return
     */
    public IExcelExport generateAnnualCompanyReport(Long taskId){

        Map<String,String> map = new HashMap<String,String>();
        Map<String,String> otherMap = new HashMap<String,String>();
        mixCoreAndPbc(taskId,map,otherMap,false);

        IExcelExport excelExport = new AnnualCompanyRecordExport();
        List<AnnualCompanyPoi> recordPoiList = new ArrayList<AnnualCompanyPoi>();

        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            String[] depositorNameAndRegNo = getDepositorNameAndRegNo(next.getValue());
            AnnualCompanyPoi annualCompanyPoi = new AnnualCompanyPoi();
            annualCompanyPoi.setCompany(depositorNameAndRegNo[0]);
            recordPoiList.add(annualCompanyPoi);
        }

        Iterator<Map.Entry<String, String>> iteratorOther = otherMap.entrySet().iterator();

        while(iteratorOther.hasNext()){
            Map.Entry<String, String> next = iteratorOther.next();
            AnnualCompanyPoi annualCompanyPoi = new AnnualCompanyPoi();
            annualCompanyPoi.setCompany(next.getValue());
            recordPoiList.add(annualCompanyPoi);
        }

        excelExport.setPoiList(recordPoiList);
        return excelExport;
    }

    /**
     * 融合核心和人行的数据
     *
     */
    private void mixCoreAndPbc(Long taskId,Map<String,String> map,Map<String,String> otherMap,boolean updateFlag){
        //人行的excel分解的全部数据
        TransactionDefinition definition = null;
        TransactionStatus transaction = null;
        List<PbcCollectAccount> pbcCollectAccountList = pbcCollectAccountDao.findByAnnualTaskId(taskId);
        List<CoreCollection> coreCollectionList = coreCollectionDao.findByAnnualTaskIdAndCollectState(taskId,CollectState.success);
        Map<String, OrganizationDto> organInMap = null;
        if(updateFlag){
            definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            transaction = transactionManager.getTransaction(definition);
            organInMap = organizationService.findAllInMap();
        }
        int count=0;
        int coreCount = 0;
        for(CoreCollection coreCollection : coreCollectionList){
            //采集状态成功的才能入库
            if (CollectState.success == coreCollection.getCollectState()) {
                //当工商注册编号不为空时，在map中增加工商注册编号
                if(StringUtils.isNotBlank(coreCollection.getRegNo())){
                    map.put(coreCollection.getAcctNo(),coreCollection.getDepositorName()+"||"+coreCollection.getRegNo());
                }else{
                    map.put(coreCollection.getAcctNo(),coreCollection.getDepositorName());
                }
                CoreCollectionDto coreCollectionDto = new CoreCollectionDto();
                BeanUtils.copyProperties(coreCollection,coreCollectionDto);
                if(updateFlag) {
                    count++;
                    annualResultService.updateAnnualResultData(taskId,coreCollectionDto,organInMap);
                    if(count %100 ==0){
                        transactionManager.commit(transaction);
                        transaction = transactionManager.getTransaction(definition);
                    }
                }
                coreCount++;
            }
        }
        log.info("融合核心和人行的数据，核心有效数据共：{}条。",coreCount);

        if(updateFlag){
            if(!transaction.isCompleted()){
                transactionManager.commit(transaction);
            }
        }
//        String lastYearLastDay = DateUtils.lastYearLastDay();
        //2018.10.26增加7.1之前的需要年检
        String lastYearLastDay = DateUtils.thisYearMidDay();

        CollectConfigDto configDto = collectConfigService.findByAnnualTaskId(taskId);
        if(configDto != null && StringUtils.isNotBlank(configDto.getPbcEndDate())){
            lastYearLastDay = configDto.getPbcEndDate();
        }

        int pbcCount=0;
        for(PbcCollectAccount pbcCollectAccount : pbcCollectAccountList){//人行数据融合
            if(checkPbcCollectIfAvailable(pbcCollectAccount,lastYearLastDay)){
                if(map.containsKey(pbcCollectAccount.getAcctNo())){
                    String depositorName = map.get(pbcCollectAccount.getAcctNo());
                    if(StringUtils.isNotBlank(pbcCollectAccount.getDepositorName()) && !pbcCollectAccount.getDepositorName().equals(depositorName)){
                        otherMap.put(pbcCollectAccount.getAcctNo(),pbcCollectAccount.getDepositorName());
                    }
                }else{
                    map.put(pbcCollectAccount.getAcctNo(),pbcCollectAccount.getDepositorName());
                    pbcCount++;
                }
            }
        }
        log.info("融合核心和人行的数据，人行单边数据共：{}条。",pbcCount);
    }

    private boolean checkPbcCollectIfAvailable(PbcCollectAccount pbcCollectAccount,String lastYearLastDay){
        if (pbcCollectAccount.getAccountStatus() != AccountStatus.normal) {
            return false;
        }
        // 不在年检时间内
        String openDate = pbcCollectAccount.getAcctCreateDate();
        if (StringUtils.isNotBlank(openDate)) {
            try {
                if (!DateUtils.dateIsLarge(openDate, lastYearLastDay)) {
                    return false;
                }
            } catch (Exception e) {
                log.error("日期比较异常", e);
            }
        }
        return true;
    }


    /**
     * 分解企业名称和工商注册号
     * @param value
     * @return
     */
    private String[] getDepositorNameAndRegNo(String value){
        if(StringUtils.isBlank(value)){
            return new String[]{"",""};
        }else{
            String[] split = StringUtils.split(value, "||");
            if(split.length>1){
                return new String[]{split[0],split[1]};
            }else{
                return new String[]{split[0],""};
            }
        }
    }
}

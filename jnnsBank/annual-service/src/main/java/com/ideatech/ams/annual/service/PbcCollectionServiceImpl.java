package com.ideatech.ams.annual.service;

import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.annual.dao.*;
import com.ideatech.ams.annual.dto.*;
import com.ideatech.ams.annual.entity.AnnualTask;
import com.ideatech.ams.annual.entity.CollectTask;
import com.ideatech.ams.annual.entity.CollectTaskErrorMessage;
import com.ideatech.ams.annual.entity.FetchPbcInfo;
import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.ams.annual.enums.CollectTaskState;
import com.ideatech.ams.annual.enums.CollectType;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.ams.annual.executor.PbcCollectTaskExecutor;
import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.ams.pbc.enums.AccountType;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.NumberUtils;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Description 人行数据采集
 * @Author wanghongjie
 * @Date 2018/8/8
 **/
@Service
@Transactional
@Slf4j
public class PbcCollectionServiceImpl implements PbcCollectionService{

    @Value("${ams.pbcCollection.executor.num:20}")
    protected int amsAccountExecutorNum;

    @Autowired
    private AnnualTaskService annualTaskService;

    @Autowired
    private PbcAmsService pbcAmsService;

    @Autowired
    protected TransactionUtils transactionUtils;

    @Autowired
    private PbcCollectAccountService pbcCollectAccountService;

    @Autowired
    private PbcCollectOrganService pbcCollectOrganService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private PbcAccountCollectionService pbcAccountCollectionService;

    @Autowired
    private PbcFileExcelService pbcFileExcelService;

    @Autowired
    private FetchPbcDao fetchPbcDao;

    @Autowired
    private FetchPbcInfoService fetchPbcInfoService;

    @Autowired
    private ThreadPoolTaskExecutor annualExecutor;

    @Autowired
    private ConfigService configService;

    @Autowired
    private AnnualTaskDao annualTaskDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private CollectTaskDao collectTaskDao;

    @Autowired
    private CollectTaskService collectTaskService;

    @Autowired
    private CollectConfigService collectConfigService;

    @Autowired
    private CollectTaskErrorMessageDao collectTaskErrorMessageDao;

    private static boolean isRun = false;

    public static boolean manualPause = false;

    private Boolean pbcCollectPause;

    private CollectConfigDto collectConfig;

    private List<Future<Long>> futureList;

    @Autowired
    private AnnualResultService annualResultService;

    @Autowired
    private  PbcCollectionDao pbcCollectionDao;

    /**
     * 是否启用人行限制采集机制
     */
    @Value("${ams.company.pbcCollectionLimit.use:false}")
    private Boolean pbcCollectionLimitUse;

    /**
     * 人行限制采集数量
     */
    @Value("${ams.company.pbcCollectionLimit.num:10000}")
    private Long pbcCollectionLimitNum;

    private boolean pbcIgnore;

    private boolean endFutureFlag;

    public static boolean finishFirstCollecting = false;

    @Override
    public List<FetchPbcInfoDto> getAll(Long taskId) {
        List<FetchPbcInfo> list = pbcCollectionDao.findByAnnualTaskId(taskId);
        List<FetchPbcInfoDto> dtoList = new ArrayList<FetchPbcInfoDto>();
        if (CollectionUtils.isNotEmpty(list)) {
            FetchPbcInfoDto dto = null;
            for (FetchPbcInfo info : list) {
                dto = new FetchPbcInfoDto();
                BeanCopierUtils.copyProperties(info, dto);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void collect(Long annualTaskId) {
        collect(CollectType.AFRESH,annualTaskId);
    }

    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void collect(CollectType collectType,Long annualTaskId) {
        endFutureFlag = false;
        List<ConfigDto> pbcIgnoreList = configService.findByKey("pbcIgnore");
        if(pbcIgnoreList.size()>0){
            pbcIgnore = true;
        }else{
            pbcIgnore = false;
        }
        CollectConfigDto collectConfigDto = collectConfigService.findByAnnualTaskId(annualTaskId);
        if(collectConfigDto != null){
            pbcCollectPause = collectConfigDto.isPbcUnlimited();
        }else{
            pbcCollectPause = false;
        }
        collectConfig = collectConfigDto;
        try {
            if (!isRun) {
                isRun = true;
                String collectBatch = "";
                CollectTaskDto collectTaskDto = new CollectTaskDto();
                if (collectType == CollectType.AFRESH) {
                    // 保存要采集的机构列表
                    log.info("开始收集要采集的机构");
                    // 清空账户列表
                    delCollectAccount();
                    // 清空机构列表
                    deleteCollectOrgan();
                    // 清空人行采集结果表
                    deleteFetchPbc();
                    // 重新获取机构批次
                    collectBatch = saveOrganList();
                    // 生成新的任务数据
                    createNewCollectTask(annualTaskId);
                    //获取未完成的任务对象
                    collectTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum.PBC,annualTaskId);
                } else {
                    collectBatch = getMaxCollectBatch();
                    log.info("获取到当前任务的批次为->" + collectBatch);
                    if (StringUtils.isBlank(collectBatch)) {
                        collectBatch = saveOrganList();
                    }
                    collectTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.PBC,annualTaskId);

                }

                finishFirstCollecting =false;

                //清除线程
                clearFuture();

                if (collectTaskDto != null) {
                    Long collectTaskId = collectTaskDto.getId();
                    // 采集任务状态设置为开始
                    log.info("采集任务状态设置为开始");
                    saveTaskCollecting(collectTaskId);
                    if(pbcIgnore){
                        ignorePbc(collectTaskId,annualTaskId);
                    }else{
                        // 根据机构下载excel
                        log.info("开始下载并保存机构对应人行excel文件");
                        downExcelFileAndSaveExcel(collectBatch, collectType,annualTaskId,collectTaskId);
                        log.info("下载并保存机构对应人行excel文件结束");
                        // 根据机构分页查询待采集的账户列表进行逐条账户采集
                        log.info("开始采集账户详细信息");
                        // 状态修改保存
                        if(!endFutureFlag){
                            collectAcctNoByExcel(collectBatch,annualTaskId,collectTaskId,true);
                        }else{
                            log.info("强制重置");
                        }
                        log.info("账户详细信息采集结束");
                        // 状态修改保存
                        if(!endFutureFlag){
                            saveTask(collectTaskId);
                        }else{
                            log.info("强制重置");
                        }
                        log.info("更新采集任务结果");
                    }

                    log.info("人行数据采集结束后，创建工商采集任务");
                    boolean createSaicTimedTaskResult = annualTaskService.createSaicTimedTask(annualTaskId);
                    log.info("人行数据采集结束后，创建工商采集任务，结果为{}", createSaicTimedTaskResult);
                }else{
                    throw new EacException("当前没有采集核心任务，请先创建任务。。");
                }
            } else {
                throw new EacException("人行数据正在采集。。。");
            }
        } catch (Exception e) {
            log.error("账户采集异常", e);
        }

        isRun = false;
    }


    @Override
//    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void collectReset(Long annualTaskId) {
        endFutureFlag = false;
        CollectConfigDto collectConfigDto = collectConfigService.findByAnnualTaskId(annualTaskId);
        if(collectConfigDto != null){
            pbcCollectPause = collectConfigDto.isPbcUnlimited();
        }else{
            pbcCollectPause = false;
        }
        collectConfig = collectConfigDto;
        try {
            if (!isRun) {
                isRun = true;
                String collectBatch = "";
                log.info("开始重新采集失败的人行数据");
                CollectTaskDto collectTaskDto = collectTaskService.findLastTaskByTypeAndAnnualTaskId(DataSourceEnum.PBC,annualTaskId);

                collectBatch = getMaxCollectBatch();

                finishFirstCollecting =false;

                //清除线程
                clearFuture();

                if (collectTaskDto != null) {
                    Long collectTaskId = collectTaskDto.getId();
                    // 采集任务状态设置为开始
                    log.info("采集任务状态设置为开始");
                    saveTaskCollecting(collectTaskId);
                    // 根据机构分页查询待采集的账户列表进行逐条账户采集
                    log.info("开始采集账户详细信息");
                    // 根据机构下载excel
                    log.info("开始下载失败机构并保存机构对应人行excel文件");
                    downExcelFileAndSaveExcel(collectBatch,CollectType.CONTINUE,annualTaskId,collectTaskId);
                    log.info("开始下载失败机构并保存机构对应人行excel文件结束");
                    // 状态修改保存
                    if(!endFutureFlag){
                        collectAcctNoByExcel(collectBatch,annualTaskId,collectTaskId,false);
                    }else{
                        log.info("强制重置");
                    }
                    log.info("账户详细信息采集结束");
                    // 状态修改保存
                    if(!endFutureFlag){
                        saveTask(collectTaskId);
                    }else{
                        log.info("强制重置");
                    }
                    log.info("更新采集任务结果");

                    log.info("人行数据采集结束后，创建工商采集任务");
                    boolean createSaicTimedTaskResult = annualTaskService.createSaicTimedTask(annualTaskId);
                    log.info("人行数据采集结束后，创建工商采集任务，结果为{}", createSaicTimedTaskResult);
                }else{
                    throw new EacException("当前没有采集核心任务，请先创建任务。。");
                }
            } else {
                throw new EacException("人行数据正在采集。。。");
            }
        } catch (Exception e) {
            log.error("账户采集异常", e);
        }

        isRun = false;
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

    private void ignorePbc(Long collectTaskId,Long annualTaskId){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CollectTask collectTaskOld = collectTaskDao.findById(collectTaskId);
        collectTaskOld.setCollectStatus(CollectTaskState.done);
        collectTaskOld.setCount(1);
        collectTaskOld.setProcessed(0);
        collectTaskOld.setSuccessed(1);
        collectTaskOld.setFailed(0);
        collectTaskOld.setIsCompleted(CompanyIfType.Yes);
        collectTaskOld.setEndTime(DateUtils.getDateTime());
        collectTaskDao.save(collectTaskOld);
        PbcCollectAccountDto pca = new PbcCollectAccountDto();
        pca.setAnnualTaskId(annualTaskId);
        pca.setCollectState(CollectState.success);
        pca.setAccountStatus(AccountStatus.normal);
        pca.setAcctNo("11");
        pca.setDepositorName("浙江省易得融信软件有限公司");
        pca.setCollectTaskId(collectTaskId);
        pca.setAcctName("浙江省易得融信软件有限公司");
        pca.setAcctCreateDate("2017-10-10");
        pca.setCollectOrganId(313192001024L);
        pbcCollectAccountService.save(pca);
        FetchPbcInfo fetchPbcInfo = new FetchPbcInfo();
        fetchPbcInfo.setCollectAccountId(pca.getId());
        fetchPbcInfo.setCollectTaskId(collectTaskId);
        fetchPbcInfo.setAnnualTaskId(annualTaskId);
        fetchPbcInfo.setAccountStatus(AccountStatus.normal);
        fetchPbcInfo.setDepositorName("浙江省易得融信软件有限公司");
        fetchPbcInfo.setAcctNo("1");
        fetchPbcInfo.setOrgCode("595386167");
        fetchPbcInfo.setOrganFullId("1");
        fetchPbcInfo.setFileNo("913302115953861679");
        fetchPbcInfo.setCollectState(CollectState.success);
        fetchPbcInfo.setLegalName("徐涛");
        fetchPbcInfo.setAcctType(AccountType.jiben);
        System.out.println(fetchPbcInfo.getAcctType().getFullName());
        System.out.println(fetchPbcInfo.getAcctType().getValue());
        fetchPbcInfo.setRegisteredCapital(NumberUtils.formatCapital(NumberUtils.convertCapital("20000000.00")).toString());
        fetchPbcInfo.setRegAddress("宁波市镇海区庄市街道中官西路777号创e慧谷12号1-1、2-1、3-1、4-1");
        fetchPbcInfo.setBusinessScope("1122334455667788");
        fetchPbcDao.save(fetchPbcInfo);
        FetchPbcInfoDto fetchPbcInfoDto = new FetchPbcInfoDto();
        BeanUtils.copyProperties(fetchPbcInfo,fetchPbcInfoDto);
        annualResultService.updateAnnualResultData(annualTaskId,fetchPbcInfoDto);
        transactionManager.commit(transaction);
    }

    /**
     * 修改状态为collecting
     * @param collectTaskId
     */
    private void saveTaskCollecting(Long collectTaskId){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CollectTask collectTask = collectTaskDao.findById(collectTaskId);
        collectTask.setCollectStatus(CollectTaskState.collecting);
        collectTask.setExceptionReason(null);
        collectTaskDao.save(collectTask);
        transactionManager.commit(transaction);
    }

    private void createNewCollectTask(final Long annualTaskId) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {
            @Override
            public void execute() throws Exception {
                finishAllCollectTask();
                AnnualTask annualTask = annualTaskDao.findById(annualTaskId);
                // 开启一个采集任务
                CollectTask collectTask = new CollectTask();
                collectTask.setName(annualTask.getYear()+"年检人行数据采集任务");
                collectTask.setCollectTaskType(DataSourceEnum.PBC);
                collectTask.setAnnualTaskId(annualTaskId);
                collectTask.setCollectStatus(CollectTaskState.init);
                collectTask.setStartTime(DateUtils.getDateTime());
                collectTask.setCount(0);
                collectTask.setProcessed(0);
                collectTask.setSuccessed(0);
                collectTask.setFailed(0);
                collectTaskDao.save(collectTask);
            }
        });
    }

    private void finishAllCollectTask() {
        try {
            List<CollectTask> collectTaskList = collectTaskDao.findByCollectTaskType(DataSourceEnum.PBC);
            for (CollectTask collectTask : collectTaskList) {
                collectTask.setCollectStatus(CollectTaskState.done);
                collectTask.setIsCompleted(CompanyIfType.Yes);
                collectTaskDao.save(collectTask);
            }
        } catch (Exception e) {
            log.error("修改采集任务异常", e);
        }
    }


    /**
     * 下载excel并保存excel账户
     *
     * @param collectBatch
     * @throws Exception
     */

    public void downExcelFileAndSaveExcel(String collectBatch, CollectType collectType,Long annualTaskId,Long collectTaskId) throws Exception {
        List<PbcCollectOrganDto> list = null;

        if (collectType == CollectType.AFRESH)
            list = pbcCollectOrganService.findByCollectBatch(collectBatch);
        else
            // 获取未下载成功的机构
            list = pbcCollectOrganService.findByCollectBatchAndCollectStateNot(collectBatch, CollectState.success);

        if(list.size()==0){
            updateCollectTaskFailed(collectTaskId,"人行机构不能为空",CollectTaskState.fail);
            throw new EacException("人行机构不能为空");
        }

        List<PbcAccountExcelInfo> excelInfoList = null;
        // 用以存放暂停的list
        List<PbcCollectOrganDto> pauseList = new ArrayList<PbcCollectOrganDto>();
        Map<String, String> errorMap = new HashMap<>();
        for (PbcCollectOrganDto pbcCollectOrganDto : list) {
            log.info("开始采集机构 [" + pbcCollectOrganDto.getBankName() + "] excel文件");
            if (manualPause || pbcCollectPause) {//手动暂停
//             if (pbcCollectPause) {
                pauseList.add(pbcCollectOrganDto);
                continue;
            }
            excelInfoList = getAccountListByExcel(pbcCollectOrganDto, 0,errorMap,collectTaskId);
            savePbcCollectAccount(excelInfoList, pbcCollectOrganDto,annualTaskId);
        }

        // 2017年8月22日19:12:40 添加暂停
        for (PbcCollectOrganDto pbcCollectOrganDto : pauseList) {
            log.info("开始采集暂停机构 [" + pbcCollectOrganDto.getBankName() + "] excel文件");
            manualPause();
            excelInfoList = getAccountListByExcel(pbcCollectOrganDto, 0,errorMap,collectTaskId);
            savePbcCollectAccount(excelInfoList, pbcCollectOrganDto,annualTaskId);
        }

        //删除该taskId对应的error message
        clearErrorMessage(collectTaskId,annualTaskId);

//        Long count = pbcCollectAccountService.count();
        Long count = pbcCollectAccountService.countByCollectStateNot(CollectState.noNeed, CollectState.success);;
        Iterator<Map.Entry<String, String>> iterator = errorMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            if(StringUtils.isNotBlank(key)){
                String[] split = StringUtils.split(key, "||");
                if(split.length==3){
                    CollectTaskErrorMessage collectTaskErrorMessage = new CollectTaskErrorMessage();
                    collectTaskErrorMessage.setAnnualTaskId(annualTaskId);
                    collectTaskErrorMessage.setTaskId(collectTaskId);
                    collectTaskErrorMessage.setBankName(split[0]);
                    collectTaskErrorMessage.setOrganizationId(Long.valueOf(split[1]));
                    collectTaskErrorMessage.setCollectTaskType(DataSourceEnum.PBC);
                    collectTaskErrorMessage.setPbcCode(split[2]);
                    collectTaskErrorMessage.setError(next.getValue());
                    saveCollectTaskErrorMsg(collectTaskErrorMessage);
                }
            }
        }
        if(count.intValue() ==0){//无采集的人行账号
            updateCollectTaskFailed(collectTaskId,"人行采集的数量为0",CollectTaskState.fail);
            throw new EacException("人行采集的数量为0");
        }else{//有需采集的人行账号
            if(!errorMap.isEmpty()){//有异常
                updateCollectTaskFailed(collectTaskId,"人行机构采集部分有异常",CollectTaskState.collecting);
            }
        }

    }

    /**
     * 更新Excel表的采集错误信息
     * @param collectTaskErrorMessage
     * @throws Exception
     */
    private void saveCollectTaskErrorMsg(final CollectTaskErrorMessage collectTaskErrorMessage) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {
            @Override
            public void execute() throws Exception {
                collectTaskErrorMessageDao.save(collectTaskErrorMessage);
            }
        });
    }

    /**
     * 更新采集机构表为fail
     */
    private void saveCollectOrganErrorMsg(final PbcCollectOrganDto pbcCollectOrganDto,final String errorMsg){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        pbcCollectOrganDto.setCollectState(CollectState.fail);
        pbcCollectOrganDto.setParErrorMsg(errorMsg);
        pbcCollectOrganService.save(pbcCollectOrganDto);
        transactionManager.commit(transaction);
    }

    private void manualPause() {
        while(manualPause||isInInvalidTime()) {
            log.info("手动暂停采集人行数据, 10分钟后重试");

            try {
                TimeUnit.MINUTES.sleep(10L);
            } catch (InterruptedException var1) {
                log.error("线程暂停异常", var1);
            }
        }
    }

    //在当前设置的时间
    private boolean isInInvalidTime(){
                /*if(collectConfig ==null){
                        return false;
                }else{
                        return !DateUtils.isInInvalidTime(collectConfig.getPbcStartTime(),collectConfig.getPbcEndTime());
                }*/
        if(collectConfig ==null){
        if(isWorkDay()){
            //判断当前是否是周六日日，如果是，则设置成采集时间为全天采集
            log.info("当前是周六日，设置采集时间为全天采集------");
            return false;
        }else{
            log.info("当前不是周六日，设置成页面配置的采集时间采集-----开始时间{},结束时间{}",collectConfig.getPbcStartTime(),collectConfig.getPbcEndTime());
            return !DateUtils.isInInvalidTime(collectConfig.getPbcStartTime(),collectConfig.getPbcEndTime());
        }
    }else{
        if(isWorkDay()){
            //判断当前是否是周六日日，如果是，则设置成采集时间为全天采集
            log.info("null当前是周六日，设置采集时间为全天采集------");
            return false;
        }else{
            log.info("null当前不是周六日，设置成页面配置的采集时间采集-----开始时间{},结束时间{}",collectConfig.getPbcStartTime(),collectConfig.getPbcEndTime());
            return !DateUtils.isInInvalidTime(collectConfig.getPbcStartTime(),collectConfig.getPbcEndTime());
        }
    }
}


    private boolean isWorkDay(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        String str= formatter.format(new Date());
        log.info("当前采集人行的时间为："+str);
        if(str.contains("六") || str.contains("日") || str.contains("天")){
            return true;
        }else{
            return false;
        }
    }

    private List<PbcAccountExcelInfo> getAccountListByExcel(PbcCollectOrganDto pbcCollectOrganDto, int tryNum,Map<String, String> errorMap,Long collectTaskId) {
        OrganizationDto organizationDto = organizationService.findById(pbcCollectOrganDto.getOrganizationId());
        if (tryNum > 5) {// 递归出口
            saveCollectOrganErrorMsg(pbcCollectOrganDto,errorMap.get(pbcCollectOrganDto.getBankName()+"||"+pbcCollectOrganDto.getOrganizationId()+"||"+organizationDto.getPbcCode()));
            return null;
        }
        try {
            String path = pbcAccountCollectionService.downRHAccount(pbcCollectOrganDto.getOrganizationId());
            List<PbcAccountExcelInfo> pbcInfoXlsAccounts = pbcFileExcelService.getPbcInfoXlsAccounts(path);
            //成功后去掉错误信息
            if(errorMap.containsKey(pbcCollectOrganDto.getBankName()+"||"+pbcCollectOrganDto.getOrganizationId()+"||"+organizationDto.getPbcCode())){
                errorMap.remove(pbcCollectOrganDto.getBankName()+"||"+pbcCollectOrganDto.getOrganizationId()+organizationDto.getPbcCode());
            }
            return pbcInfoXlsAccounts;
        } catch (Exception e) {
            if(e instanceof SyncException){//增加错误信息
                errorMap.put(pbcCollectOrganDto.getBankName()+"||"+pbcCollectOrganDto.getOrganizationId()+"||"+organizationDto.getPbcCode(),e.getMessage());
            }else{
                errorMap.put(pbcCollectOrganDto.getBankName()+"||"+pbcCollectOrganDto.getOrganizationId()+"||"+organizationDto.getPbcCode(),"人行采集Excel失败");
            }
            return processException(e, pbcCollectOrganDto, tryNum,errorMap,collectTaskId);
        }
    }

    private void delCollectAccount() throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {
            @Override
            public void execute() throws Exception {
                pbcCollectAccountService.deleteAll();
            }
        });
    }

    private void deleteCollectOrgan() throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {
            @Override
            public void execute() throws Exception {
                pbcCollectOrganService.deleteAll();
            }
        });
    }

    private void deleteFetchPbc() throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {
            @Override
            public void execute() throws Exception {
                fetchPbcDao.deleteAll();
            }
        });
    }


    // 保存要采集的机构列表
    private String saveOrganList() throws Exception {
        List<OrganizationDto> list = organizationService.getAllLeafByCache();
        String collectbatch = DateUtils.getNowDateShort("yyyyMMdd");
        for (OrganizationDto organInfo : list) {
            saveCollectOrgan(organInfo, collectbatch);
        }
        return collectbatch;
    }


    private void saveCollectOrgan(final OrganizationDto organInfo, final String collectbatch) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {
            @Override
            public void execute() throws Exception {
                PbcCollectOrganDto collectOrganDto = new PbcCollectOrganDto();
                collectOrganDto.setBankName(organInfo.getName());
                collectOrganDto.setCollectBatch(collectbatch);
                collectOrganDto.setOrganizationId(organInfo.getId());
                // 采集时需要为以下3个字段赋值
                collectOrganDto.setCollectState(CollectState.init);
                collectOrganDto.setParDate("");
                collectOrganDto.setParErrorMsg("");
                pbcCollectOrganService.save(collectOrganDto);
            }
        });
    }


    private List<PbcAccountExcelInfo> processException(Exception e, PbcCollectOrganDto pbcCollectOrganDto, int tryNum,Map<String, String> errorMap,Long collectTaskId) {
//        pbcCollectOrganDto.setParErrorMsg(e.getMessage());
//        pbcCollectOrganDto.setCollectState(CollectState.fail);
        if (ExceptionUtils.isNetExption(e)) {// 网络问题则重新调用
            return getAccountListByExcel(pbcCollectOrganDto, tryNum + 1,errorMap,collectTaskId);
        } else {
            OrganizationDto organizationDto = organizationService.findById(pbcCollectOrganDto.getOrganizationId());
            if (e.getMessage().contains("服务已关闭")) {
                updateCollectTaskProcessed(collectTaskId,CollectTaskState.waitToOpen);
                sleep(30);// 暂停30分钟后 重复调用
                updateCollectTaskProcessed(collectTaskId,CollectTaskState.collecting);
                return getAccountListByExcel(pbcCollectOrganDto, 1,errorMap,collectTaskId);
            } else if (e.getMessage().contains("用户登录信息失效")) {
                return getAccountListByExcel(pbcCollectOrganDto, tryNum + 1,errorMap,collectTaskId);
            } else if (e.getMessage().contains("已经超出系统限定的最大值")) {// excel最大只能下载1万条数据
                log.info("已经超出系统限定的最大值，将采用分割下载。");
                return getPbcAccountExcelInfoByMaxLimit(pbcCollectOrganDto,organizationDto, 0,errorMap,collectTaskId);
            } else {
                log.info("机构名称：" + pbcCollectOrganDto.getBankName() + "人行机构号：" + organizationDto.getPbcCode() + "下载excel失败,失败原因：" + e.getMessage());
                errorMap.put(pbcCollectOrganDto.getBankName()+"||"+pbcCollectOrganDto.getOrganizationId()+"||"+organizationDto.getPbcCode(),e.getMessage());
                saveCollectOrganErrorMsg(pbcCollectOrganDto,errorMap.get(pbcCollectOrganDto.getBankName()+"||"+pbcCollectOrganDto.getOrganizationId()+"||"+organizationDto.getPbcCode()));
                return null;
            }
        }
    }


    /**
     * 根据excel保存带采集的账户列表
     *
     * @param excelInfoList
     * @param pbcCollectOrganDto
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePbcCollectAccount(final List<PbcAccountExcelInfo> excelInfoList, final PbcCollectOrganDto pbcCollectOrganDto,final Long taskId) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {
            @Override
            public void execute() throws Exception {
                if (excelInfoList != null && excelInfoList.size() > 0) {
                    for (PbcAccountExcelInfo pbcAccountExcelInfo : excelInfoList) {
                        // 保留销户数据
                        if (pbcAccountExcelInfo.getAcctStatus().equals("撤销") || pbcAccountExcelInfo.getAcctStatus().equals("久悬")) {
                            continue;
                        }
                        saveAmsCollect(pbcAccountExcelInfo, pbcCollectOrganDto,taskId);
                    }
                    pbcCollectOrganDto.setCollectState(CollectState.success);
                    pbcCollectOrganDto.setParErrorMsg("");
                    pbcCollectOrganService.save(pbcCollectOrganDto);
                }
            }
        });
    }

    /**
     * 从excel中保存账户信息
     *
     * @param pbcAccountExcelInfo
     * @param pbcCollectOrganDto
     */
    private void saveAmsCollect(PbcAccountExcelInfo pbcAccountExcelInfo, PbcCollectOrganDto pbcCollectOrganDto,Long taskId) {
        PbcCollectAccountDto collectAccount = null;
        try {
            collectAccount = pbcCollectAccountService.findByAcctNo(pbcAccountExcelInfo.getAcctNo());
        } catch (Exception e) {
            // 可能存在多条不做处理
        }
        if (collectAccount != null) {
            AccountStatus excelAccountStatus = AccountStatus.str2enumByAmsAcctStatus(pbcAccountExcelInfo.getAcctStatus());
            if (excelAccountStatus.ordinal() > collectAccount.getAccountStatus().ordinal())
                return;
        } else {
            collectAccount = new PbcCollectAccountDto();
        }
        collectAccount.setAcctNo(pbcAccountExcelInfo.getAcctNo());
        collectAccount.setCollectOrganId(pbcCollectOrganDto.getId());
        collectAccount.setCollectState(CollectState.init);
        collectAccount.setParDate(DateFormatUtils.ISO_DATETIME_FORMAT.format(Calendar.getInstance()));
        collectAccount.setParErrorMsg("");
        collectAccount.setAccountStatus(AccountStatus.str2enumByAmsAcctStatus(pbcAccountExcelInfo.getAcctStatus()));
        collectAccount.setAcctCreateDate(pbcAccountExcelInfo.getAcctOpenDate());
        collectAccount.setAcctName(pbcAccountExcelInfo.getAcctName());
        collectAccount.setDepositorName(pbcAccountExcelInfo.getDepositorName());
        collectAccount.setAnnualTaskId(taskId);
        pbcCollectAccountService.save(collectAccount);
    }

    /**
     * 暂停
     *
     * @param min
     */
    private void sleep(int min) {
        try {
            Thread.sleep(1000 * 60 * min);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 若下载的
     *
     * @param organizationDto
     * @param tryNum
     * @return
     * @throws InterruptedException
     */
    protected List<PbcAccountExcelInfo> getPbcAccountExcelInfoByMaxLimit(PbcCollectOrganDto pbcCollectOrganDto,OrganizationDto organizationDto, int tryNum,Map<String, String> errorMap,Long collectTaskId) {
        log.info("进入方法getPbcAccountExcelInfoByMaxLimit");
        if (tryNum > 5) {// 递归出口
            saveCollectOrganErrorMsg(pbcCollectOrganDto,errorMap.get(organizationDto.getName()+"||"+organizationDto.getId()+"||"+organizationDto.getPbcCode()));
            return null;
        }
        List<PbcAccountExcelInfo> resultList = new ArrayList<PbcAccountExcelInfo>();
        String[] startDateArray = { "1900-01-01", "2006-01-01", "2009-01-01", "2013-01-01", "2016-01-01" };
        String[] endDateArray = { "2005-12-31", "2008-12-31", "2012-12-31", "2015-12-31", "2099-12-31" };
        try {
            if (tryNum > 0) {// 递归调用时暂停1分钟
                Thread.sleep(1000 * 60 * 1);
            }
            for (int i = 0; i < startDateArray.length; i++) {
                log.info("开始分批年份下载.....................................");
                if (startDateArray[i].equals("2099-12-31")) {
                    endDateArray[i] = DateUtils.getNowDateShort();
                }
                String amsFilePath = pbcAccountCollectionService.downRHAccount(organizationDto.getId(), startDateArray[i], endDateArray[i]);
                log.info("下载成功，开始日期：{}，结束时间：{}",startDateArray[i], endDateArray[i]);
                List<PbcAccountExcelInfo> tempList = pbcFileExcelService.getPbcInfoXlsAccounts(amsFilePath);
                resultList.addAll(tempList);
            }
            //成功后去掉错误信息
            if(errorMap.containsKey(organizationDto.getName()+"||"+organizationDto.getId()+"||"+organizationDto.getPbcCode())){
                errorMap.remove(organizationDto.getName()+"||"+organizationDto.getId()+"||"+organizationDto.getPbcCode());
            }
            return resultList;
        } catch (Exception e) {
            if(e instanceof SyncException){//增加错误信息
                errorMap.put(organizationDto.getName()+"||"+organizationDto.getId()+"||"+organizationDto.getPbcCode(),e.getMessage());
            }else{
                errorMap.put(organizationDto.getName()+"||"+organizationDto.getId()+"||"+organizationDto.getPbcCode(),"人行采集Excel表失败");
            }
            return processException(e, pbcCollectOrganDto,organizationDto, tryNum,errorMap,collectTaskId);
        }
    }

    private List<PbcAccountExcelInfo> processException(Exception e, PbcCollectOrganDto pbcCollectOrganDto,OrganizationDto organizationDto, int tryNum,Map<String, String> errorMap,Long collectTaskId) {
        if (e.getMessage().contains("服务已关闭")) { // 暂停30分钟后 重复调用
            updateCollectTaskProcessed(collectTaskId,CollectTaskState.waitToOpen);
            sleep(30);// 暂停30分钟后 重复调用
            updateCollectTaskProcessed(collectTaskId,CollectTaskState.collecting);
            return getPbcAccountExcelInfoByMaxLimit(pbcCollectOrganDto,organizationDto, 1,errorMap,collectTaskId);
        } else if (e.getMessage().contains("用户登录信息失效")) {
            return getPbcAccountExcelInfoByMaxLimit(pbcCollectOrganDto,organizationDto, tryNum + 1,errorMap,collectTaskId);
        } else if (e.getMessage().contains("异地征询") || e.getMessage().contains("回执信息的查询")) {
            log.info("e.getMessage().contains(异地征询) || 回执信息的查询)");
            return getPbcAccountExcelInfoByMaxLimit(pbcCollectOrganDto,organizationDto, tryNum + 1,errorMap,collectTaskId);
        } else {
            log.info("机构名称：" + organizationDto.getName() + "人行机构号：" + organizationDto.getPbcCode() + "分割下载excel失败,失败原因：" + e.getMessage());
            errorMap.put(organizationDto.getName()+"||"+organizationDto.getId()+"||"+organizationDto.getPbcCode(),e.getMessage());
            saveCollectOrganErrorMsg(pbcCollectOrganDto,errorMap.get(organizationDto.getName()+"||"+organizationDto.getId()+"||"+organizationDto.getPbcCode()));
            return null;
        }
    }


    private void collectAcctNoByExcel(String collectBatch, Long annualTaskId,Long collectTaskId,boolean countAddFlag) throws Exception {
        // 获取采集成功的机构
        List<PbcCollectOrganDto> list = pbcCollectOrganService.findByCollectBatchAndCollectState(collectBatch, CollectState.success);
        PbcCollectTaskExecutor executor = null;
//        String lastYearLastDay = DateUtils.lastYearLastDay();
        //2018.10.26增加7.1之前的需要年检
//        String lastYearLastDay = DateUtils.thisYearMidDay();
        //年检人行时间配置，如果不进行配置的话，默认选择年检开始的时间为截止日期
        String lastYearLastDay = DateUtils.thisYearNowDay();
        Long taskId = annualTaskService.getAnnualCompareTaskId();
        CollectConfigDto configDto = collectConfigService.findByAnnualTaskId(taskId);
        if(configDto != null && StringUtils.isNotBlank(configDto.getPbcEndDate())){
            lastYearLastDay = configDto.getPbcEndDate();
        }
        log.info("lastYearLastDay:{}",lastYearLastDay);
        Map<Long,List<PbcCollectAccountDto>> map = new HashMap<>();
        int allSize=0;
        int noNeedSize =0;
        if(list == null || list.size()==0){
            finishCollectTaskByNull(collectTaskId,annualTaskId);
        }else{
            for (PbcCollectOrganDto pbcCollectOrganDto : list) {
                // 获取未成功的账户
                List<PbcCollectAccountDto> accountList = null;
                if(countAddFlag){//首次采集和Excel重新采集
                    accountList = pbcCollectAccountService.findByCollectOrganIdAndCollectStateNot(pbcCollectOrganDto.getId(), CollectState.success,CollectState.noNeed,CollectState.fail);
                }else{//详细信息重新采集
                    accountList = pbcCollectAccountService.findByCollectOrganIdAndCollectStateNot(pbcCollectOrganDto.getId(), CollectState.success,CollectState.noNeed);
                }

                int size = accountList.size();
                log.info("开始采集：" + pbcCollectOrganDto.getBankName()+","+pbcCollectOrganDto.getOrganizationId() + ";总账户数量:" + size + "===============================");
                if (CollectionUtils.isEmpty(accountList)) {
                    continue;
                }

                // 如果是年检采集，不采集在FetchPbcInfo表中已经存在的数据,只采集状态为正常且日期为年检时间内的数据
                Set<String> acctNoSet = new HashSet<String>();
                OrganizationDto organizationDto = organizationService.findById(pbcCollectOrganDto.getOrganizationId());
                if(organizationDto !=null){
                    List<FetchPbcInfoDto> fetchPbcInfoList = fetchPbcInfoService.findByOrganFullId(organizationDto.getFullId());
//                    List<FetchPbcInfo> fetchPbcInfoList = fetchPbcDao.findByOrganFullId(organizationDto.getFullId());
                    for (FetchPbcInfoDto fetchPbcInfo : fetchPbcInfoList) {
                        acctNoSet.add(fetchPbcInfo.getAcctNo());
                    }
                    for (Iterator<PbcCollectAccountDto> iterator = accountList.iterator(); iterator.hasNext();) {
                        PbcCollectAccountDto collectAccountDto = iterator.next();
                        // 状态不是正常
                        if (collectAccountDto.getAccountStatus() != AccountStatus.normal) {
                            updatePbcAccountState(collectAccountDto,"账号状态不为正常，无需采集");
                            log.info("帐号:{},账号状态不为正常，无需采集。",collectAccountDto.getAcctNo());
                            iterator.remove();
                            continue;
                        }
                        // 不在年检时间内
                        String openDate = collectAccountDto.getAcctCreateDate();
                        if (StringUtils.isNotBlank(openDate)) {
                            try {
                                if (!DateUtils.dateIsLarge(openDate, lastYearLastDay)) {
                                    updatePbcAccountState(collectAccountDto,"开户时间为今年，无需采集");
                                    log.info("帐号:{},开户时间为今年，无需采集。",collectAccountDto.getAcctNo());
                                    iterator.remove();
                                    continue;
                                }
                            } catch (Exception e) {
                                log.error("日期比较异常", e);
                            }
                        }

                        if (acctNoSet.contains(collectAccountDto.getAcctNo())){
                            updatePbcAccountState(collectAccountDto,"账号重复，无需采集");
                            log.info("帐号:{},账号重复，无需采集。",collectAccountDto.getAcctNo());
                            iterator.remove();
                            continue;
                        }
                    }
                }

                log.info("开始采集：" + pbcCollectOrganDto.getBankName()+","+pbcCollectOrganDto.getOrganizationId() + ";总的账户数量:" + size+ ";待采集的账户数量:" + accountList.size() + "===============================");

                allSize += size;

                noNeedSize += size-accountList.size();
                map.put(pbcCollectOrganDto.getOrganizationId(),accountList);
            }

            if(countAddFlag){//首次采集和Excel重新采集
                updateCollectTaskCount(collectTaskId,allSize);
            }

            updateCollectTaskNoneedCount(collectTaskId,noNeedSize);

            finishFirstCollecting = true;

            for (Map.Entry<Long,List<PbcCollectAccountDto>> entry : map.entrySet()) {
                isNextExecutor();// 最大线程数量
                Long organizationId = entry.getKey();
                List<PbcCollectAccountDto> pbcCollectAccountList  = entry.getValue();
                OrganizationDto organizationDto = organizationService.findById(organizationId);

                executor = new PbcCollectTaskExecutor(pbcCollectAccountList);
                executor.setOrganizationDto(organizationDto);
                executor.setPbcCollectAccountService(pbcCollectAccountService);
                executor.setPbcAccountService(pbcAccountService);
                executor.setTransactionUtils(transactionUtils);
                executor.setFetchPbcDao(fetchPbcDao);
                executor.setPbcAmsService(pbcAmsService);
                executor.setPbcCollectPause(pbcCollectPause);
                executor.setAnnualTaskId(annualTaskId);
                executor.setCollectTaskId(collectTaskId);
                executor.setCollectConfig(collectConfig);
                executor.setAnnualResultService(annualResultService);
                executor.setCollectTaskDao(collectTaskDao);
                executor.setTransactionManager(transactionManager);
                executor.setCountTypeFlag(countAddFlag);
                executor.setPbcCollectionLimitUse(pbcCollectionLimitUse);
                executor.setPbcCollectionLimitNum(pbcCollectionLimitNum);
                futureList.add(annualExecutor.submit(executor));
            }

            valiCollectCompleted(collectTaskId);
            log.info("全部线程执行结束");
            map.clear();
            System.gc();
        }
    }




    /**
     * 无人行组织账号时，完成采集任务
     *
     * @param collectTaskId
     * @param annualTaskId
     */
    private void finishCollectTaskByNull(Long collectTaskId,Long annualTaskId){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CollectTask collectTask = collectTaskDao.findById(collectTaskId);
        collectTask.setCollectStatus(CollectTaskState.done);
        collectTask.setCount(0);
        collectTask.setProcessed(0);
        collectTask.setSuccessed(0);
        collectTask.setFailed(0);
        collectTask.setIsCompleted(CompanyIfType.Yes);
        collectTask.setEndTime(DateUtils.getDateTime());
        collectTask.setAnnualTaskId(annualTaskId);
        collectTaskDao.save(collectTask);
        transactionManager.commit(transaction);
    }



    /**
     * 更新人行采集任务的总数
     *
     * @param collectTaskId
     * @param count
     */
    private void updateCollectTaskCount(Long collectTaskId,int count){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CollectTask collectTask = collectTaskDao.findById(collectTaskId);
        collectTask.setCount(collectTask.getCount()+count);
        collectTaskDao.save(collectTask);
        transactionManager.commit(transaction);
    }



    /**
     * 更新人行采集任务的成功数量--用于处理无需处理的数量
     *
     * @param collectTaskId
     * @param count
     */
    private void updateCollectTaskNoneedCount(Long collectTaskId, int count){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CollectTask collectTask = collectTaskDao.findById(collectTaskId);
        collectTask.setProcessed(collectTask.getProcessed()+count);
        collectTaskDao.save(collectTask);
        transactionManager.commit(transaction);
    }


    /**
     * 更新任务状态
     * @param collectTaskId
     * @param collectStatus
     */
    private void updateCollectTaskProcessed(Long collectTaskId,CollectTaskState collectStatus){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CollectTask collectTask = collectTaskDao.findById(collectTaskId);
        collectTask.setCollectStatus(collectStatus);
        collectTaskDao.save(collectTask);
        transactionManager.commit(transaction);

    }


    /**
     * 更新异常的提醒
     * @param collectTaskId
     * @param exceptionReason
     */
    private void updateCollectTaskFailed(Long collectTaskId, String exceptionReason,CollectTaskState collectTaskState){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        CollectTask collectTask = collectTaskDao.findById(collectTaskId);
        collectTask.setCollectStatus(collectTaskState);
        collectTask.setExceptionReason(exceptionReason);
        collectTaskDao.save(collectTask);
        transactionManager.commit(transaction);

    }

    /**
     * 更新pbcAccount对象
     */
    private void updatePbcAccountState(PbcCollectAccountDto collectAccountDto,String parErrorMsg){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        collectAccountDto.setCollectState(CollectState.noNeed);
        collectAccountDto.setParErrorMsg(parErrorMsg);
        pbcCollectAccountService.save(collectAccountDto);
        transactionManager.commit(transaction);
    }

    /**
     * 清除之前的异常信息
     * @param taskId
     * @param annualTaskId
     */
    private void clearErrorMessage(Long taskId,Long annualTaskId){
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        collectTaskErrorMessageDao.deleteAllByTaskIdAndAnnualTaskId(taskId,annualTaskId);
        transactionManager.commit(transaction);
    }

    private void checkFuture(){
        for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext();) {
            Future<Long> future = iterator.next();
            if(future.isDone()){
                iterator.remove();
            }
        }
    }

    private void isNextExecutor() {
        checkFuture();
        if (futureList.size() >= amsAccountExecutorNum) {
            sleep(1);// 暂停1分钟
            log.info("激活的采集线程：" + (futureList.size()) + ">>>>>>>>>>>>>>>>>>>>>>>>");
            isNextExecutor();
        }
    }

    /**
     * 判断采集是否完成
     *
     * @param taskId
     * @throws Exception
     */
    private void valiCollectCompleted(final Long taskId) throws Exception {
        while (futureList.size() > 0) {
            checkFuture();
            // 暂停1分钟
            TimeUnit.MINUTES.sleep(1);
        }
    }

    private String getMaxCollectBatch() {
        return pbcCollectOrganService.getMaxCollectBatch();
    }

    private void saveTask(final Long collectTaskId) {
        try {
            transactionUtils.executeInNewTransaction(new TransactionCallback() {
                @Override
                public void execute() throws Exception {
                    CollectTask collectTask = collectTaskDao.findById(collectTaskId);
                    collectTask.setCollectStatus(CollectTaskState.done);
                    collectTask.setEndTime(DateUtils.getDateTime());
//                    collectTask.setSuccessed(collectTask.getCount());
                    collectTask.setIsCompleted(CompanyIfType.Yes);
//                    if (collectTask.getCount() > 0 && collectTask.getProcessed() == collectTask.getCount()) {
//                        collectTask.setIsCompleted(CompanyIfType.Yes);
//                    }
                    collectTaskDao.save(collectTask);
                }
            });
        } catch (Exception e) {
            log.error("检查线程失败", e);
        }
    }
}

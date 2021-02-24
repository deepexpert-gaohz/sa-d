package com.ideatech.ams.compare.processor;

import com.ideatech.ams.account.service.core.TransactionCallback;
import com.ideatech.ams.account.service.core.TransactionUtils;
import com.ideatech.ams.compare.dto.*;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.enums.CollectState;
import com.ideatech.ams.compare.enums.CollectTaskState;
import com.ideatech.ams.compare.enums.CompareState;
import com.ideatech.ams.compare.enums.DataSourceEnum;
import com.ideatech.ams.compare.service.*;
import com.ideatech.ams.compare.vo.CompareCollectRecordVo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Description 比对管理--在线采集虚拟类
 * @Author wanghongjie
 * @Date 2019/2/11
 **/
@Slf4j
public abstract class AbstractOnlineCollectionProcessor implements OnlineCollectionProcessor{

    protected List<Future<Long>> futureList;

    //采集线程数
    protected int amsAccountExecutorNum=5;

    @Autowired
    protected CompareCollectTaskService compareCollectTaskService;

    @Autowired
    protected TransactionUtils transactionUtils;

    @Autowired
    protected CompareTaskService compareTaskService;

    @Autowired
    protected CompareCollectRecordService compareCollectRecordService;

    @Autowired
    protected CompareDataService compareDataService;

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public void clearFuture() {
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

    /**
     * 判断线程是否是否已经用完
     */
    protected void isNextExecutor() {
        checkFuture();
        if (futureList.size() >= amsAccountExecutorNum) {
            sleep(1);// 暂停1分钟
            log.info("激活的采集线程：" + (futureList.size()) + ">>>>>>>>>>>>>>>>>>>>>>>>");
            isNextExecutor();
        }
    }

    /**
     * 关闭不用的线程
     */
    private void checkFuture(){
        for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext();) {
            Future<Long> future = iterator.next();
            if(future.isDone()){
                iterator.remove();
            }
        }
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
     * 判断采集是否完成
     *
     * @throws Exception
     */
    @Override
    public void valiCollectCompleted() throws Exception {
        while(futureList.size()>0){
            log.info("采集线程数：" + futureList.size());
            for (Iterator<Future<Long>> iterator = futureList.iterator(); iterator.hasNext();) {
                Future<Long> future = iterator.next();
                if(future.isDone()){
                    iterator.remove();
                }
            }
            log.info("判断采集是否完成，暂停1分钟.............");
            // 暂停1分钟
            TimeUnit.MINUTES.sleep(1);
        }
    }

    @Override
    public void createNewCollectTask(final Long compareTaskId, final DataSourceDto dataSourceDto,final CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {
            @Override
            public void execute() throws Exception {
                CompareTaskDto compareTaskDto = compareTaskService.findById(compareTaskId);
                if(compareTaskDto == null){
                    throw new EacException("比对任务["+compareTaskId+"]不能为空");
                }else{
//                    CompareCollectTaskDto compareCollectTaskDto = new CompareCollectTaskDto();
                    compareCollectTaskDto.setCompareTaskId(compareTaskId);
                    compareCollectTaskDto.setCollectTaskType(dataSourceDto.getDataType());
                    compareCollectTaskDto.setDataSourceId(dataSourceDto.getId());
                    compareCollectTaskDto.setCollectStatus(CollectTaskState.init);
                    compareCollectTaskDto.setCount(0);
                    compareCollectTaskDto.setProcessed(0);
                    compareCollectTaskDto.setFailed(0);
                    compareCollectTaskDto.setSuccessed(0);
                    compareCollectTaskDto.setStartTime(DateUtils.getDateTime());
                    compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);
                    if(compareTaskDto.getState() == CompareState.INIT){//未初始化
                        compareTaskDto.setState(CompareState.COLLECTING);
                        compareTaskService.saveTask(compareTaskDto);
                    }
                    log.info("创建比对采集任务[{}]成功",compareCollectTaskDto.getId());
                }
            }
        });
    }

    @Override
    public void resetNewCollectTask(final Long compareTaskId, final DataSourceDto dataSourceDto, final CompareCollectTaskDto compareCollectTaskDto) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {
            @Override
            public void execute() throws Exception {
                CompareTaskDto compareTaskDto = compareTaskService.findById(compareTaskId);
                if(compareTaskDto == null){
                    throw new EacException("比对任务["+compareTaskId+"]不能为空");
                }else{
                    CompareCollectTaskDto compareCollectTaskDto = compareCollectTaskService.findByCompareTaskIdAndDataSourceId(compareTaskId, dataSourceDto.getId());
                    if(compareCollectTaskDto == null) {
                        throw new EacException("比对任务["+compareTaskId+"]中的数据源[{"+dataSourceDto.getName()+"}]的采集任务不能为空");
                    }else if(compareCollectTaskDto.getCollectStatus() != CollectTaskState.done && compareCollectTaskDto.getCollectStatus() != CollectTaskState.fail){
                        throw new EacException("比对任务["+compareTaskId+"]中的采集任务[{"+compareCollectTaskDto.getId()+"}]的状态为[{"+compareCollectTaskDto.getCollectStatus().name()+"}],不能再次采集");
                    }else{
                        compareCollectTaskDto.setCollectStatus(CollectTaskState.init);
                        compareCollectTaskDto.setStartTime(DateUtils.getDateTime());
                        compareCollectTaskDto.setFailed(0);
                        compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);
                        if(compareTaskDto.getState() == CompareState.INIT){//未初始化
                            compareTaskDto.setState(CompareState.COLLECTING);
                            compareTaskService.saveTask(compareTaskDto);
                        }
                    }
                    log.info("再次启动比对采集任务[{}]成功",compareCollectTaskDto.getId());
                }
            }
        });
    }

    @Override
    public boolean checkCollectTask(Long compareTaskId, DataSourceDto dataSourceDto) throws Exception {
        CompareTaskDto compareTaskDto = compareTaskService.findById(compareTaskId);
        if(compareTaskDto == null){
            throw new EacException("比对任务["+compareTaskId+"]不能为空");
        }else{
            checkPriority(compareTaskId,dataSourceDto);
            CompareCollectTaskDto compareCollectTaskDtoNew = compareCollectTaskService.findByCompareTaskIdAndDataSourceId(compareTaskId, dataSourceDto.getId());
            if(compareCollectTaskDtoNew == null){
                log.info("无对应的比对采集任务，可创建新的比对采集任务");
                return true;
            }else{
                //TODO: 需要增加任务状态的判断
                log.info("有对应的比对采集任务[{}]",compareCollectTaskDtoNew.getId());
                throw new EacException("已经有对应的采集任务，无法启动新的采集任务");
            }
        }
    }


    @Override
    public boolean checkResetCollectTask(Long compareTaskId, DataSourceDto dataSourceDto) throws Exception {
        CompareTaskDto compareTaskDto = compareTaskService.findById(compareTaskId);
        if(compareTaskDto == null){
            throw new EacException("比对任务["+compareTaskId+"]不能为空");
        }else{
            checkPriority(compareTaskId,dataSourceDto);
            CompareCollectTaskDto compareCollectTaskDtoNew = compareCollectTaskService.findByCompareTaskIdAndDataSourceId(compareTaskId, dataSourceDto.getId());
            if(compareCollectTaskDtoNew == null){
                throw new EacException("无对应的比对采集任务，无法启动再次采集");
            }else {
                if (compareCollectTaskDtoNew.getCollectStatus() == CollectTaskState.done || compareCollectTaskDtoNew.getCollectStatus() == CollectTaskState.fail) {
                    log.info("有对应的比对采集任务[{}],状态为[{}],可以再次启动", compareCollectTaskDtoNew.getCollectTaskType().name(), compareCollectTaskDtoNew.getCollectStatus().getChName());
                    return true;
                } else {
                    throw new EacException("已经有对应的采集任务[" + compareCollectTaskDtoNew.getCollectTaskType().name()+ "],状态为[" + compareCollectTaskDtoNew.getCollectStatus().getChName() + "],无法再次启动");
                }
            }
        }
    }


    /**
     * 确定先后关系
     * @param compareTaskId
     * @param dataSourceDto
     */
    protected void checkPriority(Long compareTaskId, DataSourceDto dataSourceDto){
        Map<Long, CompareRuleDataSourceDto> map = compareTaskService.findCompareRuleDataSourceDtoMapByTaskId(compareTaskId);
        CompareRuleDataSourceDto compareRuleDataSourceDto = map.get(dataSourceDto.getId());
        if(compareRuleDataSourceDto != null){//对象不为null
            String parentDataSourceIdAll = compareRuleDataSourceDto.getParentDataSourceIds();
            if(StringUtils.isNotBlank(parentDataSourceIdAll)){//先决条件不为空
                String[] parentDataSourceIds = StringUtils.split(parentDataSourceIdAll, ",");
                for(String parentDataSourceId:parentDataSourceIds){
                    DataSourceDto dataSourceDtoParent = dataSourceService.findById(Long.valueOf(parentDataSourceId));
                    if(dataSourceDtoParent == null){
                        throw new EacException("采集任务["+dataSourceDto.getName()+"]依赖的先决任务["+parentDataSourceId+"]为空，请重新选定");
                    }else{
                        CompareCollectTaskDto compareCOllectTaskDto = compareCollectTaskService.findByCompareTaskIdAndDataSourceId(compareTaskId, dataSourceDtoParent.getId());
                        if(compareCOllectTaskDto == null || !compareCOllectTaskDto.getCollectStatus().equals(CollectTaskState.done)){
                            throw new EacException("采集任务["+dataSourceDto.getName()+"]必须依赖于["+dataSourceDtoParent.getName()+"]完成");
                        }
                    }
                }
            }
        }
    }


    /**
     * 初始化采集任务的采集数量
     * @throws Exception
     */
    protected void initCountCollectTask(final Long compareCollectTaskId,final Integer count) throws Exception {
        transactionUtils.executeInNewTransaction(new TransactionCallback() {

            @Override
            public void execute() throws Exception {
                CompareCollectTaskDto compareCollectTaskDto = compareCollectTaskService.findById(compareCollectTaskId);
                compareCollectTaskDto.setCount(count);
                compareCollectTaskDto.setCollectStatus(CollectTaskState.collecting);
                compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);
            }
        });
    }


    /**
     * 保存成功记录
     * @param compareData
     * @param collectTaskId
     * @throws Exception
     */
    @Override
    public void saveSuccessedRecord(final CompareDataDto compareData, final Long collectTaskId,final DataSourceDto dataSourceDto, Propagation propagation) throws Exception {
        transactionUtils.exexuteInByType(new TransactionCallback(){
            @Override
            public void execute() throws Exception {
                CompareCollectRecordDto compareCollectRecordDto = new CompareCollectRecordDto();
                CompareCollectRecordDto collectTaskIdAndCompareTaskIdAndAcctNo = compareCollectRecordService.findByCollectTaskIdAndCompareTaskIdAndAcctNo(collectTaskId, compareData.getTaskId(), compareData.getAcctNo());
                if(collectTaskIdAndCompareTaskIdAndAcctNo == null){
                    BeanUtils.copyProperties(compareData, compareCollectRecordDto);
                    compareCollectRecordDto.setCompareTaskId(compareData.getTaskId());
                    compareCollectRecordDto.setCollectTaskId(collectTaskId);
                    compareCollectRecordDto.setCollectState(CollectState.success);
                    compareCollectRecordDto.setDataSourceType(dataSourceDto.getCode());
                    compareCollectRecordService.saveCompareCollectRecord(compareCollectRecordDto);
                }else{
                    if(collectTaskIdAndCompareTaskIdAndAcctNo.getCollectState() == CollectState.fail){
                        collectTaskIdAndCompareTaskIdAndAcctNo.setFailReason(null);
                        collectTaskIdAndCompareTaskIdAndAcctNo.setCollectState(CollectState.success);
                        collectTaskIdAndCompareTaskIdAndAcctNo.setRegNo(compareData.getRegNo());
                        collectTaskIdAndCompareTaskIdAndAcctNo.setDepositorName(compareData.getDepositorName());
                        compareCollectRecordService.saveCompareCollectRecord(collectTaskIdAndCompareTaskIdAndAcctNo);
                    }else{
                        log.error("比对任务[{}]--采集任务[{}]--账号[{}]的之前采集状态为[{}],不能更改为成功",collectTaskIdAndCompareTaskIdAndAcctNo.getCompareTaskId()
                                ,collectTaskIdAndCompareTaskIdAndAcctNo.getCollectTaskId(),collectTaskIdAndCompareTaskIdAndAcctNo.getAcctNo()
                                ,collectTaskIdAndCompareTaskIdAndAcctNo.getCollectState().name());
                    }
                }
            }
        },propagation);
    }

    /**
     * 保存失败记录
     * @param compareData
     * @param collectTaskId
     * @param errorMsg
     * @throws Exception
     */
    @Override
    public void saveFailedRecord(final CompareDataDto compareData,final Long collectTaskId,final String errorMsg,final DataSourceDto dataSourceDto, Propagation propagation) throws Exception {
        transactionUtils.exexuteInByType(new TransactionCallback() {

            @Override
            public void execute() throws Exception {
                CompareCollectRecordDto compareCollectRecordDto = new CompareCollectRecordDto();
                BeanUtils.copyProperties(compareData, compareCollectRecordDto);
                CompareCollectRecordDto collectTaskIdAndCompareTaskIdAndAcctNo = compareCollectRecordService.findByCollectTaskIdAndCompareTaskIdAndAcctNo(collectTaskId, compareData.getTaskId(), compareData.getAcctNo());
                if(collectTaskIdAndCompareTaskIdAndAcctNo == null){
                    compareCollectRecordDto.setCompareTaskId(compareData.getTaskId());
                    compareCollectRecordDto.setCollectTaskId(collectTaskId);
                    compareCollectRecordDto.setCollectState(CollectState.fail);
                    compareCollectRecordDto.setFailReason(errorMsg);
                    compareCollectRecordDto.setDataSourceType(dataSourceDto.getCode());
                    compareCollectRecordService.saveCompareCollectRecord(compareCollectRecordDto);
                }else{
                    if(collectTaskIdAndCompareTaskIdAndAcctNo.getCollectState() == CollectState.fail){
                        collectTaskIdAndCompareTaskIdAndAcctNo.setFailReason(errorMsg);
                        compareCollectRecordService.saveCompareCollectRecord(collectTaskIdAndCompareTaskIdAndAcctNo);
                    }else{
                        log.error("比对任务[{}]--采集任务[{}]--账号[{}]的之前采集状态为[{}],不能更改为失败",collectTaskIdAndCompareTaskIdAndAcctNo.getCompareTaskId()
                                ,collectTaskIdAndCompareTaskIdAndAcctNo.getCollectTaskId(),collectTaskIdAndCompareTaskIdAndAcctNo.getAcctNo()
                                ,collectTaskIdAndCompareTaskIdAndAcctNo.getCollectState().name());
                    }
                }
            }
        },propagation);
    }


    /**
     * 根据DataSource保存CompareData
     * @param compareData
     * @param dataSourceDto
     * @throws Exception
     */
    @Override
    public void saveCompareData(final CompareDataDto compareData,final DataSourceDto dataSourceDto, Propagation propagation) throws Exception {
        transactionUtils.exexuteInByType(new TransactionCallback() {

            @Override
            public void execute() throws Exception {
                compareDataService.saveCompareData(compareData,dataSourceDto);
            }
        },propagation);
    }

    /**
     * 成功记录操作
     * @param compareData
     * @param collectTaskId
     * @param dataSourceDto
     */
    @Override
    public void saveSuccessCombine(final CompareDataDto compareData, final Long collectTaskId, final DataSourceDto dataSourceDto, Propagation propagation) throws Exception {
        transactionUtils.exexuteInByType(new TransactionCallback() {
            @Override
            public void execute() throws Exception {
                saveSuccessedRecord(compareData,collectTaskId,dataSourceDto,null);
                saveCompareData(compareData,dataSourceDto,null);
            }
        },propagation);
    }

    /**
     *
     * @param compareCollectTaskId
     * @param successed
     * @param failed
     * @throws Exception
     */
    protected void updatedCount(final Long compareCollectTaskId,final Integer successed,final Integer failed, Propagation propagation) throws Exception {
        transactionUtils.exexuteInByType(new TransactionCallback() {

            @Override
            public void execute() throws Exception {
                CompareCollectTaskDto compareCollectTaskDto = compareCollectTaskService.findById(compareCollectTaskId);
                compareCollectTaskDto.setSuccessed(compareCollectTaskDto.getSuccessed() + successed);
                compareCollectTaskDto.setFailed(compareCollectTaskDto.getFailed() + failed);
                compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);
            }
        },propagation);
    }



    /**
     * 完成采集任务
     * @throws Exception
     */
    protected void finishCollectTask(final Long compareCollectTaskId, Propagation propagation) throws Exception {
        transactionUtils.exexuteInByType(new TransactionCallback() {

            @Override
            public void execute() throws Exception {
                CompareCollectTaskDto compareCollectTaskDto = compareCollectTaskService.findById(compareCollectTaskId);
                compareCollectTaskDto.setEndTime(DateUtils.getDateTime());
                //20190605 海峡银行要求采集成功的数量为0条，状态也变为{【采集成功】
                /*if(compareCollectTaskDto.getSuccessed() == 0){
                    compareCollectTaskDto.setIsCompleted(CompanyIfType.No);
                    compareCollectTaskDto.setCollectStatus(CollectTaskState.fail);
                    compareCollectTaskDto.setExceptionReason("采集的数量为0");
                }else{
                    compareCollectTaskDto.setIsCompleted(CompanyIfType.Yes);
                    compareCollectTaskDto.setCollectStatus(CollectTaskState.done);
                }*/
                if(compareCollectTaskDto.getSuccessed() == 0){
                    compareCollectTaskDto.setExceptionReason("采集的数量为0");
                }
                compareCollectTaskDto.setIsCompleted(CompanyIfType.Yes);
                compareCollectTaskDto.setCollectStatus(CollectTaskState.done);
                compareCollectTaskService.saveCompareCollectTask(compareCollectTaskDto);
            }
        },propagation);
    }


    /**
     * 组合采集数据来源
     * @param compareCollectTaskDtos
     * @return
     */
    protected Set<CompareCollectRecordVo> mixCompareCollectRecordDto(List<CompareCollectTaskDto> compareCollectTaskDtos, Long compareTaskId){
        Set<CompareCollectRecordVo> compareCollectRecordVosSet = new HashSet<>();
        for(CompareCollectTaskDto compareCollectTaskDto:compareCollectTaskDtos){
            List<CompareCollectRecordDto> compareCollectRecordDtoList = compareCollectRecordService.findByCollectTaskIdAndCompareTaskId(compareCollectTaskDto.getId(),compareTaskId);
            List<CompareCollectRecordVo> compareCollectRecordVosList = ConverterService.convertToList(compareCollectRecordDtoList, CompareCollectRecordVo.class);
            CollectionUtils.addAll(compareCollectRecordVosSet,compareCollectRecordVosList);
        }
        return compareCollectRecordVosSet;

    }

    /**
     * 分批次采集数据
     * @return
     */
    protected <E> Map<String, Set<E>> getBatchTokens(Collection<E> tokens) {
        Map<String, Set<E>> returnMap = new HashMap<String, Set<E>>(16);
        if (tokens != null && tokens.size() > 0) {
            int allLeafSum = tokens.size();
            int tokensNum = (allLeafSum / amsAccountExecutorNum) + 1;
            int num = 0;
            int batchNum = 0;
            Set<E> batchTokens = new HashSet<>();
            for (E token : tokens) {
                if (num > 0 && num % tokensNum == 0) {
                    batchNum++;
                    returnMap.put("第" + batchNum + "线程", batchTokens);
                    batchTokens = new HashSet<E>();
                }
                batchTokens.add(token);
                num++;
            }
            returnMap.put("第" + (batchNum + 1) + "线程", batchTokens);
        }
        return returnMap;
    }

    public void checkCompareTaskState(Long taskId){
        try {
            compareTaskService.updateCompareTaskState(taskId);
            //更新采集完成后的数量统计
            compareTaskService.getCompareTaskCount(taskId);
        } catch (Exception e) {
            log.error("判断比对任务是否都采集完成错误：，",e);
        }

    }

    /**
     * 采集步骤
     * @param compareTaskId
     * @param dataSourceDto
     */
    @Override
    public void onlineCollect(Long compareTaskId, DataSourceDto dataSourceDto) throws Exception {
        CompareCollectTaskDto compareCollectTaskDto = new CompareCollectTaskDto();
        //1.采集初始化
        initCollect(compareTaskId,dataSourceDto,compareCollectTaskDto);
        //2.采集进行时
        processCollect(compareTaskId,dataSourceDto,compareCollectTaskDto);
        //3.采集结束
        finishCollect(compareTaskId,dataSourceDto,compareCollectTaskDto);
        //4.判断比对任务是否都采集完成了
        checkCompareTaskState(compareTaskId);
    }

    @Override
    public void resetOnlineCollect(Long compareTaskId, DataSourceDto dataSourceDto) throws Exception {
        CompareCollectTaskDto compareCollectTaskDto = compareCollectTaskService.findByCompareTaskIdAndDataSourceId(compareTaskId, dataSourceDto.getId());
        //1.再次采集初始化
        initResetCollect(compareTaskId,dataSourceDto,compareCollectTaskDto);
        //2.再次采集进行时
        processResetCollect(compareTaskId,dataSourceDto,compareCollectTaskDto);
        //3.采集结束
        finishCollect(compareTaskId,dataSourceDto,compareCollectTaskDto);
        //4.判断比对任务是否都采集完成了
        checkCompareTaskState(compareTaskId);
    }
}

package com.ideatech.ams.compare.service;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.compare.dto.*;
import com.ideatech.ams.compare.entity.CompareCollectTask;
import com.ideatech.ams.compare.enums.*;
import com.ideatech.ams.compare.executor.CompareCollectControllerExecutor;
import com.ideatech.ams.compare.executor.CompareTaskCompareExecutor;
import com.ideatech.ams.compare.processor.OnlineCollectionProcessor;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.ApplicationContextUtil;
import com.ideatech.common.util.DateUtil;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description 比对管理--定时任务
 * @Author wanghongjie
 * @Date 2019/2/16
 **/
@Slf4j
@Service
@Transactional
public class CompareScheduledTaskServiceImpl implements CompareScheduledTaskService {

    @Autowired
    private CompareTaskService compareTaskService;

    @Autowired
    private CompareCollectTaskService compareCollectTaskService;

    @Autowired
    private CompareScheduledTaskService compareScheduledTaskService;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private ThreadPoolTaskExecutor compareExecutor;

    @Autowired
    private CompareRuleService compareRuleService;

    @Autowired
    private CompareRuleDataSourceService compareRuleDataSourceService;

    @Override
    public void checkAndStartScheduledTask(TaskType taskType, CompareState... state){
        List<CompareTaskDto> taskTypeAndStateDtoLists = compareTaskService.findByTaskTypeAndStateIn(taskType, state);
        if(taskTypeAndStateDtoLists.size()>0){
            log.info("待启动的定时任务数量为{}",taskTypeAndStateDtoLists.size());
            for(CompareTaskDto compareTaskDto : taskTypeAndStateDtoLists){
                startScheduledTask(compareTaskDto);
            }
        }else{
            log.info("无待启动的定时任务");
        }
    }


    @Override
    public void checkAndStartScheduledCollectionTask(TaskType taskType,CompareState... state){
        List<CompareTaskDto> taskTypeAndStateDtoLists = compareTaskService.findByTaskTypeAndStateIn(taskType, state);
        if(taskTypeAndStateDtoLists.size()>0){
            log.info("[{}]的比对任务数量为[{}]",taskType.getFullName(),taskTypeAndStateDtoLists.size());
            for(CompareTaskDto compareTaskDto : taskTypeAndStateDtoLists){
                startScheduledCollectionTask(compareTaskDto);
            }
        }else{
            log.info("无待启动的定时采集任务");
        }
    }


    /**
     * 启动比对管理的在线采集
     * @param compareTaskDto
     */
    private void startScheduledCollectionTask(CompareTaskDto compareTaskDto){
        CompareRuleDto compareRuleDto = compareRuleService.findById(compareTaskDto.getCompareRuleId());
        if (compareRuleDto != null) {
            //根据规则找到对应的数据源
            List<CompareRuleDataSourceDto> compareRuleDataSourceDtoList = compareRuleDataSourceService.findByCompareRuleId(compareRuleDto.getId());
            if (CollectionUtils.isNotEmpty(compareRuleDataSourceDtoList)) {
                for (CompareRuleDataSourceDto compareRuleDataSourceDto : compareRuleDataSourceDtoList) {
                    if (compareRuleDataSourceDto.getActive()) {
                        DataSourceDto dataSourceDto = dataSourceService.findById(compareRuleDataSourceDto.getDataSourceId());
                        if(dataSourceDto.getCollectType() == CollectType.ONLINE) {//在线采集
                            try {
                                OnlineCollectionProcessor onlineCollectionProcessor = (OnlineCollectionProcessor) ApplicationContextUtil.getBean(org.apache.commons.lang.StringUtils.lowerCase(dataSourceDto.getCode()) + "OnlineCollectionProcessor");
                                if (onlineCollectionProcessor.checkCollectTask(compareTaskDto.getId(), dataSourceDto)) {
                                    CompareCollectControllerExecutor compareCollectControllerExecutor = new CompareCollectControllerExecutor();
                                    compareCollectControllerExecutor.setTaskId(compareTaskDto.getId());
                                    compareCollectControllerExecutor.setDataSourceDto(dataSourceDto);
                                    compareCollectControllerExecutor.setOnlineCollectionProcessor(onlineCollectionProcessor);
                                    compareExecutor.submit(compareCollectControllerExecutor);
                                }
                            }catch (EacException ex){
                                log.error("在线采集启动异常:"+ex.getMessage());
                            }catch (Exception e){
                                log.error("在线采集启动异常:"+e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     *
     * 启动比对管理任务
     * @param compareTaskDto
     * @return
     */
    private void startScheduledTask(CompareTaskDto compareTaskDto){
        TaskRate rate = compareTaskDto.getRate();
        String startTime = compareTaskDto.getStartTime();
        if(rate == null){//无定时任务
            log.info("定时任务[{}]未选择对应的启动频率",compareTaskDto.getName());
        }else if(rate == TaskRate.day && compareTaskDto.getState() == CompareState.INIT){//只启动一次 && 未开始
            if(StringUtils.isNotBlank(startTime) && DateUtils.isDateFormat(startTime,"yyyy-MM-dd hh:mm")){
                if(DateUtils.isMinuteAfter(startTime,"yyyy-MM-dd hh:mm")){//启动时间已经到了
                    compareScheduledTaskService.saveStartTask(compareTaskDto);
                    log.info("定时任务[{}]启动频率[{}],启动时间[{}]已到，定时任务状态改成[{}]",compareTaskDto.getName(),compareTaskDto.getRate().getValue(),compareTaskDto.getStartTime(),CompareState.COLLECTING.getName());
                }else{//启动时间未到
                    log.info("定时任务[{}]启动频率[{}],启动时间[{}]未到",compareTaskDto.getName(),compareTaskDto.getRate().getValue(),compareTaskDto.getStartTime());
                }
            }else{
                log.info("定时任务[{}]启动频率[{}],启动时间[{}]格式不对",compareTaskDto.getName(),compareTaskDto.getRate().getValue(),compareTaskDto.getStartTime());
            }
        }else if(rate == TaskRate.week || rate == TaskRate.month || rate == TaskRate.quarter){
            if(StringUtils.isNotBlank(startTime) && DateUtils.isDateFormat(startTime,"yyyy-MM-dd hh:mm")){
                if(checkRateAndStartTime(compareTaskDto)){//启动时间已经到了
                    compareScheduledTaskService.saveStartTask(compareTaskDto);
                    log.info("定时任务[{}]启动频率[{}],启动时间[{}]已到，定时任务状态改成[{}]",compareTaskDto.getName(),compareTaskDto.getRate().getValue(),compareTaskDto.getStartTime(),CompareState.COLLECTING.getName());
                }else{//启动时间未到
                    log.info("定时任务[{}]启动频率[{}],启动时间[{}]未到",compareTaskDto.getName(),compareTaskDto.getRate().getValue(),compareTaskDto.getStartTime());
                }
            }else{
                log.info("定时任务[{}]启动频率[{}],启动时间[{}]格式不对",compareTaskDto.getName(),compareTaskDto.getRate().getValue(),compareTaskDto.getStartTime());
            }
        }else{
            log.info("定时任务[{}]无需启动",compareTaskDto.getName());
        }
    }


    /**
     * 判断是否能启动
     * @param compareTaskDto
     * @return
     */
    private boolean checkRateAndStartTime(CompareTaskDto compareTaskDto){
        TaskRate rate = compareTaskDto.getRate();
        String startTime = compareTaskDto.getStartTime();
        Date lastStartedTime = compareTaskDto.getLastStartedTime();
        if(rate == TaskRate.week){//周启动
            if(lastStartedTime ==null){//未启动
                return DateUtils.isMinuteAfter(startTime,"yyyy-MM-dd hh:mm");
            }else{//再次启动
                Date date = DateUtil.addWeeks(lastStartedTime, 1);
                return DateUtils.isMinuteAfter(date);
            }
        }else if(rate == TaskRate.month){//月启动
            if(lastStartedTime ==null){//未启动
                return DateUtils.isMinuteAfter(startTime,"yyyy-MM-dd hh:mm");
            }else{//再次启动
                Date date = DateUtil.addMonths(lastStartedTime,1);
                return DateUtils.isMinuteAfter(date);
            }
        }else if(rate == TaskRate.quarter){//季度启动
            if(lastStartedTime ==null){//未启动
                return DateUtils.isMinuteAfter(startTime,"yyyy-MM-dd hh:mm");
            }else{//再次启动
                Date date = DateUtil.addMonths(lastStartedTime,3);
                return DateUtils.isMinuteAfter(date);
            }
        }
        return false;
    }

    public void doCompareTask(CompareTaskDto compareTaskDto){
        log.info("定时任务[{}]开始比对......",compareTaskDto.getName());
        TaskRate rate = compareTaskDto.getRate();
        String startTime = compareTaskDto.getStartTime();
        if(rate == null){//无定时任务
            log.info("定时任务[{}]未选择对应的启动频率",compareTaskDto.getName());
        }else if(rate == TaskRate.day && (compareTaskDto.getState() == CompareState.COLLECTSUCCESS)){//只启动一次 && 未开始
            if(StringUtils.isNotBlank(startTime) && DateUtils.isDateFormat(startTime,"yyyy-MM-dd hh:mm")){
                if(DateUtils.isMinuteAfter(startTime,"yyyy-MM-dd hh:mm")){//启动时间已经到了
                    CompareTaskCompareExecutor compareTaskCompareExecutor = new CompareTaskCompareExecutor();
                    compareTaskCompareExecutor.setCompareTaskService(compareTaskService);
                    compareTaskCompareExecutor.setTaskId(compareTaskDto.getId());
                    compareExecutor.submit(compareTaskCompareExecutor);
                    log.info("定时任务[{}]启动频率[{}],启动时间[{}]已到，定时任务状态改成[{}]",compareTaskDto.getName(),compareTaskDto.getRate().getValue(),compareTaskDto.getStartTime(),CompareState.COMPARING.getName());
                }else{//启动时间未到
                    log.info("定时任务[{}]启动频率[{}],启动时间[{}]未到",compareTaskDto.getName(),compareTaskDto.getRate().getValue(),compareTaskDto.getStartTime());
                }
            }else{
                log.info("定时任务[{}]启动频率[{}],启动时间[{}]格式不对",compareTaskDto.getName(),compareTaskDto.getRate().getValue(),compareTaskDto.getStartTime());
            }
        }else if(rate == TaskRate.week || rate == TaskRate.month || rate == TaskRate.quarter){
            if(StringUtils.isNotBlank(startTime) && DateUtils.isDateFormat(startTime,"yyyy-MM-dd hh:mm")){
                if(checkRateAndStartTime(compareTaskDto)){//启动时间已经到了
                    CompareTaskCompareExecutor compareTaskCompareExecutor = new CompareTaskCompareExecutor();
                    compareTaskCompareExecutor.setCompareTaskService(compareTaskService);
                    compareTaskCompareExecutor.setTaskId(compareTaskDto.getId());
                    compareExecutor.submit(compareTaskCompareExecutor);
                    log.info("定时任务[{}]启动频率[{}],启动时间[{}]已到，定时任务状态改成[{}]",compareTaskDto.getName(),compareTaskDto.getRate().getValue(),compareTaskDto.getStartTime(),CompareState.COMPARING.getName());
                }else{//启动时间未到
                    log.info("定时任务[{}]启动频率[{}],启动时间[{}]未到",compareTaskDto.getName(),compareTaskDto.getRate().getValue(),compareTaskDto.getStartTime());
                }
            }else{
                log.info("定时任务[{}]启动频率[{}],启动时间[{}]格式不对",compareTaskDto.getName(),compareTaskDto.getRate().getValue(),compareTaskDto.getStartTime());
            }
        }else{
            log.info("定时任务[{}]无需启动",compareTaskDto.getName());
        }
    }

    /**
     * 保存比对管理任务
     * @param compareTaskDto
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void saveStartTask(CompareTaskDto compareTaskDto){
        CompareTaskDto compareTaskDtoNew = compareTaskService.findById(compareTaskDto.getId());
        compareTaskDtoNew.setState(CompareState.COLLECTING);
        compareTaskDtoNew.setLastStartedTime(new Date());
        compareTaskService.saveTask(compareTaskDtoNew);
    }

    @Override
    public void doCompare(TaskType taskType, CompareState... state) {
        List<CompareTaskDto> taskTypeAndStateDtoLists = compareTaskService.findByTaskTypeAndStateIn(taskType, state);
        if(taskTypeAndStateDtoLists.size()>0){
            log.info("待比对的定时任务数量为{}",taskTypeAndStateDtoLists.size());
            for(CompareTaskDto compareTaskDto : taskTypeAndStateDtoLists){
                doCompareTask(compareTaskDto);
            }
        }else{
            log.info("无待启动的定时任务");
        }
    }
}

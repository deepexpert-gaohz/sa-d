package com.ideatech.ams.annual.schedule;

import com.ideatech.ams.annual.service.AnnualStatisticsService;
import com.ideatech.ams.annual.service.AnnualTaskService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.schedule.service.SchedulerLockService;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.util.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AnnualResultStatisticsSchedule implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private AnnualStatisticsService annualStatisticsService;

    @Autowired
    private AnnualTaskService annualTaskService;

    /**
     * 是否执行定时任务，集群环境中应该只有一台机器此项为true
     */
    @Value("${ams.schedule.host:true}")
    private boolean scheduleHost;

    @Value("${ams.schedule.autoStatistics.use:true}")
    private boolean statisticsUse;

    @Value("${ams.schedule.autoStatistics.timing:0 0 23 * * ?}")
    private String annualStatisticsScheduleTiming;

    @Autowired
    private ConfigService configService;

    private String ip = "";

    @Autowired
    private SchedulerLockService schedulerLockService;

    public static final String TRUE = "TRUE";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //年检结果统计定时任务
        if(scheduleHost){
            if(statisticsUse) {
                taskScheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ConfigDto isRunning = new ConfigDto();
                            isRunning.setConfigKey("FALSE");//不执行任务。

                            schedulerLockService.isRunningFun(1020L,isRunning);

                            if (TRUE.equals(isRunning.getConfigKey())){
                                log.info("年检结果统计定时任务开始。。。");
                                annualStatisticsService.saveStatistics(annualTaskService.getAnnualCompareTaskId());
                                log.info("年检结果统计定时任务结束。。。");
                                schedulerLockService.releaseLock(1020L);//释放锁
                            }
                        } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                            log.error("年检结果统计定时任务失败：已有主机启动该定时任务。");
                        } catch (Exception e) {
                            schedulerLockService.releaseLock(1020L);//释放锁
                            log.error("年检结果统计定时任务失败", e);
                        }
                    }
                }, new CronTrigger(annualStatisticsScheduleTiming));
            }
        }

    }

}

package com.ideatech.ams.account.schedule;

import com.ideatech.ams.account.service.BatchSuspendService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.schedule.service.SchedulerLockService;
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
public class BatchSuspendSchedule implements ApplicationListener<ContextRefreshedEvent> {

    private String scheduleName;

    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * 是否执行定时任务，集群环境中应该只有一台机器此项为true
     */
    @Value("${ams.schedule.host:true}")
    private boolean scheduleHost;

    @Value("${ams.schedule.batch-suspend.use:true}")
    private boolean scheduleUse;

    @Value("${ams.schedule.batch-suspend.timing:0 0 23 * * ?}")
    private String scheduleTiming;

    @Autowired
    private BatchSuspendService batchSuspendService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SchedulerLockService schedulerLockService;

    private static final String CONFIG_KEY = "systemIP";


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.scheduleName = "批量久悬定时任务";
        if (scheduleHost) {
            if (scheduleUse) {
                taskScheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ConfigDto isRunning = new ConfigDto();
                            isRunning.setConfigKey("FALSE");//不执行任务。

                            //id见SchedulerLockInitializer初始化类
                            schedulerLockService.isRunningFun(1013L,isRunning);

                            if (isRunning.getConfigKey().equals("TRUE")) {
                                log.info("{}开始", scheduleName);
                                long startTime = System.currentTimeMillis();
                                batchSuspendService.process();
                                log.info("结束,耗时{}毫秒", scheduleName, System.currentTimeMillis() - startTime);
                                schedulerLockService.releaseLock(1013L);//释放锁
                            }
                        } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                            log.error("批量久悬定时任务失败：已有主机启动该定时任务。");
                        } catch (Exception e) {
                            log.error("{}失败",scheduleName, e);
                            schedulerLockService.releaseLock(1013L);//释放锁
                        }
                    }
                }, new CronTrigger(scheduleTiming));
            }
        }

    }



}

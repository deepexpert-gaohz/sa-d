package com.ideatech.ams.compare.schedule;

import com.ideatech.ams.compare.enums.TaskRate;
import com.ideatech.ams.compare.enums.TaskType;
import com.ideatech.ams.compare.executor.CompareTaskCompareExecutor;
import com.ideatech.ams.compare.service.CompareTaskService;
import com.ideatech.ams.system.config.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * @author jzh
 * @date 2019/6/12.
 */

@Component
@Slf4j
public class SaicCheckSchedule implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private ConfigService configService;

    @Autowired
    private CompareTaskService compareTaskService;

    /**
     * 执行周期
     */
    @Value("${compare.saicCheck.timing:0 0 0 15 1/3 ? *}")
    private String timing;

    /**
     * 开始时间（预留可配置）
     */
    //@Value("${compare.saicCheck.startTime}")
    private String startTime;

    /**
     * 是否启用定时任务
     */
    @Value("${compare.saicCheck.use:false}")
    private boolean isUse;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (isUse){
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (configService.isRunningScheduledIp()) {
                        Long id = compareTaskService.createSaicCheakTask(TaskType.TIMING, TaskRate.day,startTime);
                        log.info("初始化定时任务成功,ID：{}，执行周期：{}",id,TaskRate.day);
                    }
                }
            }, new CronTrigger(timing));
        }
    }
}

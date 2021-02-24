package com.ideatech.ams.account.schedule;

import com.ideatech.ams.account.service.BatchNoticeDueChangeService;
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
public class NoticeSchedule implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TaskScheduler taskScheduler;

    private String scheduleName;

    /**
     * 是否执行定时任务，集群环境中应该只有一台机器此项为true
     */
    @Value("${ams.schedule.host:true}")
    private boolean scheduleHost;

    @Value("${ams.notice.overDue.use:false}")
    private boolean scheduleUse;

    @Value("${ams.notice.overDue.timing:0 0 12 * * ?}")
    private String scheduleTiming;

    @Autowired
    private ConfigService configService;

    @Autowired
    private BatchNoticeDueChangeService batchNoticeDueChangeService;

    @Autowired
    private SchedulerLockService schedulerLockService;

    private static final String CONFIG_KEY = "systemIP";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.scheduleName = "通知提醒是否超期批量更新定时任务";
        if (scheduleHost) {
            if (scheduleUse) {
                taskScheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ConfigDto isRunning = new ConfigDto();
                            isRunning.setConfigKey("FALSE");//不执行任务。

                            schedulerLockService.isRunningFun(1018L,isRunning);

                            if (isRunning.getConfigKey().equals("TRUE")){
                                log.info("{}开始", scheduleName);
                                long startTime = System.currentTimeMillis();

                                batchNoticeDueChangeService.noticeDueChange();

                                log.info("{}结束,耗时{}毫秒", scheduleName, System.currentTimeMillis() - startTime);
                                schedulerLockService.releaseLock(1018L);//释放锁
                            }
                        } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                            log.error("通知提醒是否超期批量更新定时任务失败：已有主机启动该定时任务。");
                        } catch (Exception e) {
                            schedulerLockService.releaseLock(1018L);//释放锁
                            log.error("{}失败",scheduleName, e);
                        }
                    }
                }, new CronTrigger(scheduleTiming));
            }
        }

    }

}

package com.ideatech.ams.account.schedule;

import com.ideatech.ams.account.service.core.TpoService;
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
public class TpoSchedule implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TaskScheduler taskScheduler;


    /**
     * 是否执行定时任务，集群环境中应该只有一台机器此项为true
     */
    @Value("${ams.schedule.host:true}")
    private boolean scheduleHost;

    @Value("${ams.schedule.tpo.use:true}")
    private boolean scheduleUse;

    @Value("${ams.schedule.tpo.timing:0 0 23 * * ?}")
    private String scheduleTiming;

    @Autowired
    private TpoService tpoService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SchedulerLockService schedulerLockService;

    private String ip = "";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //年检结果统计定时任务
        if (scheduleHost) {
            if (scheduleUse) {
                taskScheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ConfigDto isRunning = new ConfigDto();
                            isRunning.setConfigKey("FALSE");//不执行任务。

                            schedulerLockService.isRunningFun(1014L,isRunning);

                            if(isRunning.getConfigKey().equals("TRUE")){
                                log.info("T+1定时任务开始");
                                long startTime = System.currentTimeMillis();
                                tpoService.processTxtFile();
                                log.info("T+1定时任务结束,耗时{}毫秒", System.currentTimeMillis() - startTime);
                                schedulerLockService.releaseLock(1014L);//释放锁
                            }
                        } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                            log.error("T+1定时任务失败：已有主机启动该定时任务。");
                        } catch (Exception e) {
                            log.error("T+1定时任务失败", e);
                            schedulerLockService.releaseLock(1014L);//释放锁
                        }
                    }
                }, new CronTrigger(scheduleTiming));
            }
        }

    }

}

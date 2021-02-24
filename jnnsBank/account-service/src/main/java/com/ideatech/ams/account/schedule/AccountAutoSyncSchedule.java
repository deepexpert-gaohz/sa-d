package com.ideatech.ams.account.schedule;

import com.ideatech.ams.account.service.bill.AccountBillsAllService;
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

/**
 * 自动报备定时任务
 *
 * @author van
 * @date 2018/7/18 14:44
 */
@Component
@Slf4j
public class AccountAutoSyncSchedule implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * 是否执行定时任务，集群环境中应该只有一台机器此项为true
     */
    @Value("${ams.schedule.host:true}")
    private boolean scheduleHost;

    @Value("${ams.schedule.autoSync.use:true}")
    private boolean autoSyncUse;
    @Value("${ams.schedule.autoSync.timing:0 30 18 * * ?}")
    private String autoSyncScheduleTiming;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SchedulerLockService schedulerLockService;

    private String ip = "";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        //推送核心定时任务
        if(scheduleHost ){
            if (autoSyncUse) {
                taskScheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ConfigDto isRunning = new ConfigDto();
                            isRunning.setConfigKey("FALSE");//不执行任务。

                            schedulerLockService.isRunningFun(1017L,isRunning);

                            if (isRunning.getConfigKey().equals("TRUE")){
                                log.info("自动报送定时任务开始。。。");
                                accountBillsAllService.autoSync();
                                log.info("自动报送定时任务结束。。。");
                                schedulerLockService.releaseLock(1017L);//释放锁
                            }
                        } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                            log.error("自动报送定时任务失败：已有主机启动该定时任务。");
                        } catch (Exception e) {
                            log.error("自动报送定时任务失败", e);
                            schedulerLockService.releaseLock(1017L);//释放锁
                        }
                    }
                }, new CronTrigger(autoSyncScheduleTiming));
            }
        }
    }
}




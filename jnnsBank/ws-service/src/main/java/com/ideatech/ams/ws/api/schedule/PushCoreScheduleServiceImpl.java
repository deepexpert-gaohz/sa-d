package com.ideatech.ams.ws.api.schedule;

import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.schedule.service.SchedulerLockService;
import com.ideatech.ams.ws.api.service.AmsPushCoreService;
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
 * 推送核心接口
 */
@Component
@Slf4j
public class PushCoreScheduleServiceImpl implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private AmsPushCoreService amsPushCoreService;

    @Autowired
    private ConfigService configService;
    /**
     * 是否执行定时任务，集群环境中应该只有一台机器此项为true
     */
    @Value("${ams.schedule.host:true}")
    private boolean scheduleHost;

    @Value("${ams.schedule.pushCore.use:true}")
    private boolean pushCoreUse;
    @Value("${ams.schedule.pushCore.timing}")
    private String pushCoreScheduleTiming;

    @Value("${ams.schedule.pushCancelHeZhunCore.use:true}")
    private boolean pushCancelHeZhunCoreUse;
    @Value("${ams.schedule.pushCancelHeZhunCore.timing}")
    private String pushCancelHeZhunCoreScheduleTiming;

    private String ip = "";

    @Autowired
    private SchedulerLockService schedulerLockService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        //推送核心定时任务
        if (scheduleHost) {
            if (pushCoreUse) {
                taskScheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            ConfigDto isRunning = new ConfigDto();
                            isRunning.setConfigKey("FALSE");//不执行任务。

                            schedulerLockService.isRunningFun(1015L,isRunning);

                            if (isRunning.getConfigKey().equals("TRUE")){
                                log.info("推送核心开户许可证开始。。。");
                                amsPushCoreService.checkAndPushCore();
                                log.info("推送核心开户许可证结束。。。");
                                schedulerLockService.releaseLock(1015L);//释放锁
                            }
                        } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                            log.error("推送核心开户许可证失败：已有主机启动该定时任务。");
                        } catch (Exception e) {
                            log.error("推送核心开户许可证失败", e);
                            schedulerLockService.releaseLock(1015L);//释放锁
                        }
                    }
                }, new CronTrigger(pushCoreScheduleTiming));
            }
        }

        //取消推送核心定时任务
        if (scheduleHost) {
            if (pushCancelHeZhunCoreUse) {
                taskScheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ConfigDto isRunning = new ConfigDto();
                            isRunning.setConfigKey("FALSE");//不执行任务。

                            schedulerLockService.isRunningFun(1016L,isRunning);

                            if (isRunning.getConfigKey().equals("TRUE")){
                                log.info("取消核准推送核心开户许可证开始。。。");
                                amsPushCoreService.checkAndCancelHeZhunPushCore();
                                log.info("取消核准推送核心开户许可证结束。。。");
                                schedulerLockService.releaseLock(1016L);//释放锁
                            }
                        } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                            log.error("取消核准推送核心开户许可证失败：已有主机启动该定时任务。");
                        } catch (Exception e) {
                            log.error("取消核准推送核心开户许可证失败", e);
                            schedulerLockService.releaseLock(1016L);//释放锁
                        }
                    }
                }, new CronTrigger(pushCancelHeZhunCoreScheduleTiming));
            }
        }
    }
}

package com.ideatech.ams.schedule;



import com.ideatech.ams.service.SyncCoreComparOpenAcctService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class SyncCompareAutoSchedule implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(SyncCompareAutoSchedule.class);

    @Autowired
    private TaskScheduler taskScheduler;
    Pageable pageable;


    @Autowired
    private SyncCoreComparOpenAcctService syncCoreComparOpenAcctService;

    @Value("${ams.hexin.open:true}")
    private boolean autoSyncUse;

    @Value("${ams.hexin.open.time:0 0 11,14,16,17 * * ?}")
    private String openTime;

    @Value("${ams.hexin.del.core:true}")
    private boolean autoDelCoreUse;
    @Value("${ams.hexin.del.core.time:0 0 11,14,16,17 * * ?}")
    private String autoDelCoreTime;
    @Value("${ams.yujing.insert.use:true}")
    private boolean yujinginsertUse;
    @Value("${ams.yujing.insert.time:0 23 3 * * ?}")
    private String yujinginsertUseTime;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (autoSyncUse) {
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        log.info("定时任务开启...");
                        syncCoreComparOpenAcctService.syncCoreCompare();
                        log.info("定时任务结束...");
                    }catch (Exception e){
                        log.info("定时任务结束...");
                    }
                }
            }, new CronTrigger(openTime));
        }
        if (autoDelCoreUse) {
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        log.info("定时任务删除存量数据中重复数据开启...");
                        syncCoreComparOpenAcctService.delCoreData();
                        log.info("定时任务删除存量数据中重复数据结束...");
                    }catch (Exception e){
                        log.info("定时任务删除存量数据中重复数据----異常结束----...異常信息="+e);
                    }
                }
            }, new CronTrigger(autoDelCoreTime));
        }
        if (yujinginsertUse) {
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                   log.info("定时任务预警数据导入全量表开始...");
                   syncCoreComparOpenAcctService.insertYuJingData( pageable);
                   log.info("定时任务预警数据导入全量表结束...");
                }
            }, new CronTrigger(yujinginsertUseTime));
        }
    }
}

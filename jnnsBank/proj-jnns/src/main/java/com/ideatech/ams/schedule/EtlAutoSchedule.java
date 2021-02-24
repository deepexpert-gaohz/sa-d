package com.ideatech.ams.schedule;

import com.ideatech.ams.readData.service.ReadDataService;
import com.ideatech.ams.service.EtlService;
import com.ideatech.ams.service.JnnsSaicTestService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Log4j
public class EtlAutoSchedule implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TaskScheduler taskScheduler;



    @Autowired
    EtlService etlService;

    @Autowired
    private JnnsSaicTestService jnnsSaicTestService;

    @Autowired
    private ReadDataService readDataService;






    @Value("${ams.etl.open:true}")
    private boolean autoSyncUse;

    @Value("${ams.etl.open.time}")
    private String openTime;

    @Value("${ams.change.open.time}")
    private String changeTime;

    @Value("${ams.huifa.open.time}")
    private String huifaTime;


    @Value("${ams.etl.hexin.time}")
    private String hexinTime;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //每天核心更新，并更新产品全量账户状态
        if (autoSyncUse) {
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    etlService.douUpdateCoreData();
                }
            }, new CronTrigger(openTime));
        }
        //每天核心同步数据
     /*   if (autoSyncUse) {
            taskScheduler.schedule(new Runnable() {
               @Override
               public void run() {
                    etlService.doCoreData();
                }
            }, new CronTrigger(hexinTime));
        }*/



        //汇法数据
        if (autoSyncUse) {
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    jnnsSaicTestService.doQuery();
                }
            }, new CronTrigger(huifaTime));
        }

        //账管工商信息预警
        if (autoSyncUse) {
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    readDataService.readData();
                }
            }, new CronTrigger(changeTime));
        }
    }

}



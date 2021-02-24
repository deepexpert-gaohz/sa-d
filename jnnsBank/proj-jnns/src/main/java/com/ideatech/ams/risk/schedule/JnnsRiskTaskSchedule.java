package com.ideatech.ams.risk.schedule;

import com.ideatech.ams.risk.riskdata.service.RiskApiService;
import com.ideatech.ams.risk.service.JnnsOpenAcctRiskFileService;
import com.ideatech.ams.risk.service.JnnsRiskCompareAcctFileService;
import com.ideatech.ams.utils.DateUtils;
import com.ideatech.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 风险任务自动调度
 *
 * @author yangcq
 * @date 2019/06/16 23:54
 * address wulmq
 */
@Configuration
@EnableScheduling
@Slf4j
public class JnnsRiskTaskSchedule implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * 开户风险监测数据跑批 是否启用
     */
    @Value("${ams.risk.schedule.syncRiskData.use:true}")
    private boolean autoSyncRiskUse;
    /**
     * 开户风险监测数据跑批 定时任务配置时间
     */
    @Value("${ams.risk.schedule.syncRiskData.timing}")
    private String autoSyncRiskScheduleTiming;


    @Autowired
    private RiskApiService riskApiService;

    @Autowired
    private JnnsOpenAcctRiskFileService szsmFtpOdsFileService;

    @Autowired
    private JnnsRiskCompareAcctFileService yinQiDuiZhangFileService;

    /**
     * 1.批量生成非风险数据，即存量数据中，所有账户统计信息
     * 2.批量生成风险数据
     *
     * @param
     * @author yangcq
     * @date 2019-06-17 00:17
     * @address wulmq
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //批量生成风险数据
        if (autoSyncRiskUse) {
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        log.info("获取江南农商数仓的T+1文件的定时任务开启...");
                        Date startDate = new Date();
                        long startDateLong = startDate.getTime();
                        szsmFtpOdsFileService.pullOdsFile(DateUtils.DateToStr(DateUtil.subDays(new Date(), 1), "yyyyMMdd"));
                        Date endDate = new Date();
                        long endDateLong = endDate.getTime();
                        log.info("获取江南农商数仓的T+1文件的定时任务结束...共耗时" + (endDateLong - startDateLong) / 1000 + "秒");
                    } catch (Exception e) {
                        log.error("获取江南农商数仓的T+1文件的定时任务发生异", e);
                    }

                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                        String currentDate = formatter.format(new Date());
                        log.info("风险数据生成定时任务开始。。。");
                        riskApiService.syncRiskData(currentDate);
                        log.info("风险数据生成定时任务结束。。。");
                    } catch (Exception e) {
                        log.error("风险数据生成定时任务异常", e);
                    }
                    try {
                        log.info("江南农商对账数据获取定时任务开始!");
                        Date startGenDate = new Date();
                        long startGenDateLong = startGenDate.getTime();
                        yinQiDuiZhangFileService.getFile();
                        Date endGenDate = new Date();
                        long endGenDateLong = endGenDate.getTime();
                        log.info("江南农商对账数据获取定时任务结束...共耗时" + (endGenDateLong - startGenDateLong) / 1000 + "秒");
                    } catch (Exception e) {
                        log.error("江南农商对账数据获取的定时任务生异常", e);
                    }
                }
            }, new CronTrigger(autoSyncRiskScheduleTiming));
        }

    }
}




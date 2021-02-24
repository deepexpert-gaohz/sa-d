package com.ideatech.ams.risk.schedule;

import com.ideatech.ams.risk.CustomerTransaction.service.CustomerTransactionService;
import com.ideatech.ams.risk.highRisk.dao.HighRiskApiDao;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;

/**
 * 客户异动信息定时调度（工商基础信息\严重违法信息\经营异常外部接口，定时对比生成客户异动）
 * liuzheng 20191022
 */

@Configuration
@EnableScheduling
public class CustomerTransactionSchedule implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger log = LoggerFactory.getLogger(CustomerTransactionSchedule.class);

    @Autowired
    TaskScheduler taskScheduler;
    @Value("${ams.customer.transaction.queryUrlDate.use:false}")
    private boolean autoQueryUse;
    @Value("${ams.customer.transaction.queryUrlDate.timing: 0 0 23 L 1/3 ?}")
    private String autoQueryTiming;
    @Value("${ams.customer.transaction.checkTransaction.use:false}")
    private boolean autoCheckUse;
    @Value("${ams.customer.transaction.checkTransaction.timing:0 0 23 * * ?}")
    private String autoCheckTiming;
    @Autowired
    CustomerTransactionService customerTransactionService;
    @Autowired
    OrganizationDao organizationDao;
    @Autowired
    HighRiskApiDao highRiskApiDao;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //定时查询外部接口数据
        if (autoQueryUse) {
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        log.info("开始获取工商基础信息等外部数据任务。。。");
//                        log.info("----------开始获取基本工商数据----------");
//                        customerTransactionService.queryBusinessData("");
//                        log.info("----------获取基本工商数据结束----------");
//                        log.info("----------开始获取严重违法数据----------");
//                        customerTransactionService.queryCompanyBlack("");
//                        log.info("----------获取严重违法数据结束----------");
//                        log.info("----------开始获取经营异常数据----------");
//                        customerTransactionService.queryCompanyException("");
//                        log.info("----------获取经营异常数据结束----------");
                        customerTransactionService.queryBusinessDataBase("");
                        log.info("获取工商基础信息等外部数据任务结束。。。");
                    } catch (Exception e) {
                        log.error("获取工商基础信息等外部数据任务失败", e);
                    }
                }
            }, new CronTrigger(autoQueryTiming));

        }
        //定时对比生出客户异动信息
        if (autoCheckUse) {
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        log.info("定时对比生出客户异动信息任务开始。。。");
                        customerTransactionService.checkBusinessData();
                        log.info("定时对比生出客户异动信息任务结束。。。");
                    } catch (Exception e) {
                        log.error("定时对比生出客户异动信息任务失败", e);
                    }
                }
            }, new CronTrigger(autoCheckTiming));
        }
    }
}

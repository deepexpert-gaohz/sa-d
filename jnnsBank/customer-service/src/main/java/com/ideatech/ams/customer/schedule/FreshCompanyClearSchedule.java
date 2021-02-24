package com.ideatech.ams.customer.schedule;

import com.ideatech.ams.customer.service.newcompany.FreshCompanyService;
import com.ideatech.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jzh
 * @date 2019/3/12.
 */
@Component
@Slf4j
public class FreshCompanyClearSchedule implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 是否执行定时任务，集群环境中应该只有一台机器此项为true
     */
    @Value("${ams.schedule.host:true}")
    private boolean scheduleHost;

    @Value("${ams.schedule.clear-company.use:true}")
    private boolean isUse;

    @Value("${ams.schedule.clear-company.timing:0 0 12 * * ?}")
    private String times;

    @Value("${ams.schedule.clear-company.days:90}")
    private Integer dayRange;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private FreshCompanyService freshCompanyService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if(scheduleHost){
            log.info("scheduleHost:"+scheduleHost);
            //启动开关（存在配置文件）
            if(isUse) {
                log.info("isUse:"+isUse);
                log.info("times:"+times);

                taskScheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        log.info("FreshCompanyClearSchedule进入run");
                        log.info("进入删除");
                        // 日期格式
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = DateUtil.subDays(new Date(),dayRange);
                        log.info(date.toString());
                        //删除某天之前的数据
                        freshCompanyService.delete(dateFormat.format(date));
                    }
                }, new CronTrigger(times));
            }
        }
    }
}

package com.ideatech.ams.customer.schedule;

import com.ideatech.ams.customer.dto.neecompany.FreshCompanyConfigDto;
import com.ideatech.ams.customer.service.newcompany.FreshCompanyConfigService;
import com.ideatech.ams.customer.service.newcompany.FreshCompanyService;
import com.ideatech.common.util.DateUtils;
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

@Component
@Slf4j
public class FreshCompanyAddSchedule implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * 是否执行定时任务，集群环境中应该只有一台机器此项为true
     */
    @Value("${ams.schedule.host:true}")
    private boolean scheduleHost;

    //新注册企业定时任务
    @Value("${ams.schedule.fresh-company.use:false}")
    private boolean isUse;

    @Value("${ams.schedule.fresh-company.timing:0 0 6 * * ?}")
    private String times;

    @Autowired
    private FreshCompanyConfigService freshCompanyConfigService;

    @Autowired
    private FreshCompanyService freshCompanyService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(scheduleHost){
            if(isUse) {
                taskScheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        FreshCompanyConfigDto config = freshCompanyConfigService.getConfig();
                        if(config == null) {
                            return;
                        }
                        if (config.getUnlimited()) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); // 日期格式
                            if ("months".equals(config.getSelectRange())) {
                                freshCompanyService.add(config.getProvinceCode(), config.getBeginDate().replaceAll("-", ""), config.getEndDate().replaceAll("-", ""));
                            } else if ("days".equals(config.getSelectRange())) {
                                Date dateBefore = DateUtils.dayBefore(new Date(), Long.parseLong(config.getDayRange()));
                                freshCompanyService.add(config.getProvinceCode(), dateFormat.format(dateBefore), dateFormat.format(new Date()));
                            }
                        }
                    }
                }, new CronTrigger(times));
            }
        }
    }

}

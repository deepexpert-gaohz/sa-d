package com.ideatech.ams.customer.schedule;

import com.ideatech.ams.customer.dto.SaicStateDto;
import com.ideatech.ams.customer.service.SaicMonitorService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jzh
 * @date 2019/4/19.
 */

@Component
@Slf4j
public class GetSaicStateSchedule implements ApplicationListener<ContextRefreshedEvent> {

    @Value("0 0/10 * * * ?")
    private String times;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private SaicMonitorService saicMonitorService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SaicStateDto saicStateDto;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                if (configService.isRunningScheduledIp()) {
                    saicStateDto.setState(false);
                    saicStateDto.setSpeed(9999L);
                    List<ConfigDto> configDtoList = configService.findByKey("saicState");
                    if (CollectionUtils.isNotEmpty(configDtoList)) {
                        if (configDtoList.get(0).getConfigValue().equals("true")) {
                            SaicStateDto stateDto = saicMonitorService.getState();
                            saicStateDto.setState(stateDto.getState());
                            saicStateDto.setSpeed(stateDto.getSpeed());
                        }
                    }
                }
            }
        }, new CronTrigger(times));
    }

}

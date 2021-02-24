package com.ideatech.ams.account.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.ReportStatisticsForDateDTO;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.pbc.PbcFaultSmsService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.schedule.service.SchedulerLockService;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 到期超期提醒定时发送
 */
@Component
@Slf4j
public class PbcFaultSmsSchedule implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * 发送时间（频率）
     */
    @Value("${ams.pbcFault.timing}")
    private String times;

    /**
     * 是否启用定时任务
     */
    @Value("${ams.pbcFault.use}")
    private boolean isUse;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private PbcFaultSmsService pbcFaultSmsService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SchedulerLockService schedulerLockService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //1、是否启用定时任务 （配置文件配置）
        if (isUse) {
            log.info("创建上报失败短信提醒定时任务");
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        ConfigDto isRunning = new ConfigDto();
                        isRunning.setConfigKey("FALSE");//不执行任务。

                        schedulerLockService.isRunningFun(1021L,isRunning);

                        log.info("开始执行发送上报失败短信提醒任务");
                        String now = DateUtils.DateToStr(new Date(), "yyyy-MM-dd");

                        List<OrganizationDto> organList = organizationService.listAll();
                        JSONArray arr = new JSONArray();
                        log.info("开始获取所有机构当天上报统计数据");
                        for (OrganizationDto organ : organList) {
                            ReportStatisticsForDateDTO dto = allBillsPublicService.statisticsForDate(now, organ.getFullId());
                            if (dto != null
                                    && (!dto.getEccsNumAll().equals(dto.getEccsNum()) || !dto.getPbcNumAll().equals(dto.getPbcNum()))) {
                                JSONObject json = (JSONObject) JSON.toJSON(dto);
                                json.put("code", organ.getCode());
                                json.put("name", organ.getName());
                                json.put("telephone", organ.getTelephone());
                                arr.add(json);
                            }
                        }
                        log.info("获取所有机构当天上报统计数据结束");
                        pbcFaultSmsService.sendMessage(arr);
                        log.info("发送上报失败短信提醒任务结束");
                        schedulerLockService.releaseLock(1021L);//释放锁
                    } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                        log.error("自动报送定时任务失败：已有主机启动该定时任务。");
                    } catch (Exception e) {
                        log.error("自动报送定时任务失败", e);
                        schedulerLockService.releaseLock(1021L);//释放锁
                    }
                }
            }, new CronTrigger(times));
        }
    }


}

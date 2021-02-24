package com.ideatech.ams.system.initializer;

import com.ideatech.ams.system.config.dao.ConfigDao;
import com.ideatech.ams.system.config.entity.ConfigPo;
import com.ideatech.ams.system.schedule.dao.SchedulerLockDao;
import com.ideatech.ams.system.schedule.entity.SchedulerLock;
import com.ideatech.ams.system.schedule.enums.StatusEnum;
import com.ideatech.common.constant.DataInitializerConstant;
import com.ideatech.common.initializer.AbstractDataInitializer;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有需要锁的定时任务，都需要在这边初始化到数据库。
 * @author jzh
 * @date 2019-08-21.
 */

@Component
@Slf4j
public class SchedulerLockInitializer extends AbstractDataInitializer {

    @Autowired
    private SchedulerLockDao schedulerLockDao;

    @Autowired
    private ConfigDao configDao;

    @Value("${apply.task.scheduletiming.delaySec}")
    private long delaySec;

    @Value("${apply.task.syncScheduletiming.delaySec}")
    private long syncDelaySec;

    @Value("${import.file.scheduletiming}")
    private String scheduleTiming;

    @Value("${apply.images.scheduletiming}")
    private String imageScheduletiming;

    @Value("${ams.scheduler.updateEccsPassword}")
    private String updateEccsPassword;

    @Value("${ams.annual.export-timing}")
    private String annualExportTiming;

    @Value("${ams.schedule.batch-suspend.timing:0 0 23 * * ?}")
    private String scheduleTiming1013;

    @Value("${ams.schedule.tpo.timing:0 0 23 * * ?}")
    private String scheduleTiming1014;

    @Value("${ams.schedule.pushCore.timing}")
    private String pushCoreScheduleTiming;

    @Value("${ams.schedule.pushCancelHeZhunCore.timing}")
    private String pushCancelHeZhunCoreScheduleTiming;

    @Value("${ams.schedule.autoSync.timing:0 30 18 * * ?}")
    private String autoSyncScheduleTiming;

    @Value("${ams.notice.overDue.timing:0 0 12 * * ?}")
    private String scheduleTiming1018;

    @Value("${ams.image.video.schedule.timing:0 0 23 * * ?}")
    private String scheduleTiming1019;

    @Value("${ams.schedule.autoStatistics.timing:0 0 23 * * ?}")
    private String annualStatisticsScheduleTiming;

    @Value("${ams.pbcFault.timing}")
    private String pbcFaultTiming;

    @Override
    protected void doInit() throws Exception {

        saveSchedulerLockConfig();

        saveSchedulerLockLogConfig();

        SchedulerLock schedulerLock1001 = new SchedulerLock(1001L,"定时同步预约开户信息",delaySec);
        save(schedulerLock1001);

        SchedulerLock schedulerLock1002 = new SchedulerLock(1002L,"定时同步机构信息",syncDelaySec);
        save(schedulerLock1002);

        SchedulerLock schedulerLock1003 = new SchedulerLock(1003L,"核心数据初始化",scheduleTiming);
        save(schedulerLock1003);

        SchedulerLock schedulerLock1004 = new SchedulerLock(1004L,"拉取预约影像",imageScheduletiming);
        save(schedulerLock1004);

        SchedulerLock schedulerLock1005 = new SchedulerLock(1005L,"更新信用机构的密码",updateEccsPassword);
        save(schedulerLock1005);

        SchedulerLock schedulerLock1006 = new SchedulerLock(1006L,"核查校验密码",60L);
        save(schedulerLock1006);

        SchedulerLock schedulerLock1007 = new SchedulerLock(1007L,"年检判断工商在线采集",86400L);
        save(schedulerLock1007);

        SchedulerLock schedulerLock1008 = new SchedulerLock(1008L,"年检结果定时更新",annualExportTiming);
        save(schedulerLock1008);

        SchedulerLock schedulerLock1009 = new SchedulerLock(1009L,"[比对管理]-定时任务判断",60L);
        save(schedulerLock1009);

        SchedulerLock schedulerLock1010 = new SchedulerLock(1010L,"[比对管理]-定时采集任务启动",60L);
        save(schedulerLock1010);

        SchedulerLock schedulerLock1011 = new SchedulerLock(1011L,"[比对管理]-比对",60L);
        save(schedulerLock1011);

        SchedulerLock schedulerLock1012 = new SchedulerLock(1012L,"定期校验人行用户名密码",60L);
        save(schedulerLock1012);

        SchedulerLock schedulerLock1013 = new SchedulerLock(1013L,"批量久悬定时任务",scheduleTiming1013);
        save(schedulerLock1013);

        SchedulerLock schedulerLock1014 = new SchedulerLock(1014L,"T+1定时任务失败",scheduleTiming1014);
        save(schedulerLock1014);

        SchedulerLock schedulerLock1015 = new SchedulerLock(1015L,"推送核心定时任务",pushCoreScheduleTiming);
        save(schedulerLock1015);

        SchedulerLock schedulerLock1016 = new SchedulerLock(1016L,"取消推送核心定时任务",pushCancelHeZhunCoreScheduleTiming);
        save(schedulerLock1016);

        SchedulerLock schedulerLock1017 = new SchedulerLock(1017L,"自动报送定时任务",autoSyncScheduleTiming);
        save(schedulerLock1017);

        SchedulerLock schedulerLock1018 = new SchedulerLock(1018L,"通知提醒是否超期批量更新定时任务",scheduleTiming1018);
        save(schedulerLock1018);

        SchedulerLock schedulerLock1019 = new SchedulerLock(1019L,"视频同步定时任务",scheduleTiming1019);
        save(schedulerLock1019);

        SchedulerLock schedulerLock1020 = new SchedulerLock(1020L,"年检结果统计定时任务",annualStatisticsScheduleTiming);
        save(schedulerLock1020);

        SchedulerLock schedulerLock1021 = new SchedulerLock(1021L,"上报失败短信提醒定时任务", pbcFaultTiming);
        save(schedulerLock1021);

    }

    private void save(SchedulerLock schedulerLock) {
        SchedulerLock newSchedulerLock = schedulerLockDao.findOne(schedulerLock.getId());
        if (newSchedulerLock==null){
            schedulerLock.setOnOff(true);
            schedulerLock.setStatus(StatusEnum.UNEXECUTED);
            schedulerLockDao.save(schedulerLock);
            log.info("{}定时任务锁：初始化完成。",schedulerLock.getName());
        }else if(newSchedulerLock.getStatus()==StatusEnum.EXECUTING && newSchedulerLock.getMaxRunTime()!=null && newSchedulerLock.getLastRunTime()!=null){
            if (DateUtils.isSecondAfter(newSchedulerLock.getLastUpdateDate(),newSchedulerLock.getMaxRunTime())){
                newSchedulerLock.setStatus(StatusEnum.UNEXECUTED);
                schedulerLockDao.save(newSchedulerLock);
                log.info("{}定时任务锁：重置状态完成。",schedulerLock.getName());
            }
        }else {
            log.info("已有{}定时任务锁：无需初始化。",schedulerLock.getName());
        }
    }

    /**
     * 初始化是否启用SchedulerLock锁的配置
     */
    private void saveSchedulerLockConfig(){
        ConfigPo configPo = configDao.findTopByConfigKey("scheduler");
        if (configPo==null){
            configPo = new ConfigPo();
            configPo.setConfigKey("scheduler");
            //是否启用(true:启用;false:不启用)
            configPo.setConfigValue("false");
            configDao.save(configPo);
            log.info("初始化是否启用SchedulerLock锁的配置成功。");
        }else {
            log.info("无需初始化是否启用SchedulerLock锁的配置。");
        }
    }

    /**
     * 初始化是否启用SchedulerLock锁日志的配置
     */
    private void saveSchedulerLockLogConfig(){
        ConfigPo configPo = configDao.findTopByConfigKey("schedulerLog");
        if (configPo==null){
            configPo = new ConfigPo();
            configPo.setConfigKey("schedulerLog");
            //是否启用(true:启用;false:不启用)
            configPo.setConfigValue("false");
            configDao.save(configPo);
            log.info("初始化是否启用SchedulerLock锁日志的配置成功。");
        }else {
            log.info("无需初始化是否启用SchedulerLock锁日志的配置。");
        }
    }

    @Override
    protected boolean isNeedInit() {
        return true;
    }

    @Override
    public Integer getIndex() {
        return DataInitializerConstant.OPTION_INITIALIZER_INDEX;
    }
}

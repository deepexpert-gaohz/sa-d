package com.ideatech.ams.system.schedule.service;

import com.ideatech.ams.system.config.dao.ConfigDao;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.entity.ConfigPo;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.schedule.dao.SchedulerLockDao;
import com.ideatech.ams.system.schedule.dto.SchedulerLockDTO;
import com.ideatech.ams.system.schedule.entity.SchedulerLock;
import com.ideatech.ams.system.schedule.enums.StatusEnum;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author jzh
 * @date 2019-08-21.
 */

@Slf4j
@Service
public class SchedulerLockServiceImpl extends BaseServiceImpl<SchedulerLockDao, SchedulerLock, SchedulerLockDTO> implements SchedulerLockService{

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private ConfigService configService;

    public static final String TRUE = "TRUE";

    /**
     * 是否打印定时任务锁日志
     */
    private Boolean logFlag = false;

    @Override
    public SchedulerLockDTO findOneByName(String name) {
        SchedulerLock schedulerLock = getBaseDao().findFirstByName(name);
        return ConverterService.convert(schedulerLock,SchedulerLockDTO.class);
    }

    /**
     * 使用 configService.isRunningScheduledIp() 或者 SystemUtils.systemIP
     * @param isRunning
     */
    @Override
    public void isSystemIP(ConfigDto isRunning) {
        showLog(logFlag,1,"不启用任务调度锁功能");
        String ip = "";
        ConfigPo configPo = new ConfigPo();
        configPo.setConfigKey("%systemIP%");
        Example<ConfigPo> example = Example.of(configPo,
                ExampleMatcher.matching().withMatcher("configKey", ExampleMatcher.GenericPropertyMatchers.contains()));
        List<ConfigPo> all = configDao.findAll(example);
        if (CollectionUtils.isNotEmpty(all)) {
            ConfigPo dto = all.get(0);
            ip = dto.getConfigValue();
        }
        if (SystemUtils.systemIP(ip)) {
            //执行任务。
            isRunning.setConfigKey(TRUE);
        }else {
            showLog(logFlag,2,"SystemUtils.systemIP(ip)等于false，不执行任务。");
        }
    }

    @Override
    public void lockUp(Long id, ConfigDto isRunning) {

        showLog(logFlag,1,"启用任务调度锁功能");

        //id见SchedulerLockInitializer初始化类
        SchedulerLock schedulerLock = getBaseDao().findOne(id);

        //定周期的定时任务
        if (schedulerLock!=null && null!=schedulerLock.getCycle()){
            //上次执行时间非空
            if (null!=schedulerLock.getLastRunTime()){
                if (!DateUtils.isSecondAfter(schedulerLock.getLastUpdateDate(),schedulerLock.getCycle())){
                    showLog(logFlag,2,schedulerLock.getName()+"定时任务上锁失败：当前时间<上次执行时间+定周期。");
                    return;//当前时间<上次执行时间+定周期   -->不执行
                }
            }else {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //schedulerLockDTO.getOnOff()==Boolean.TRUE 后期可以控制定时任务是否执行
        if (schedulerLock!=null && schedulerLock.getStatus()== StatusEnum.UNEXECUTED && schedulerLock.getOnOff().equals(Boolean.TRUE)){
            schedulerLock.setStatus(StatusEnum.EXECUTING);
            schedulerLock.setLastRunTime(new Date());
            schedulerLock.setLastRunner(SystemUtils.getRealIP());
            getBaseDao().save(schedulerLock);
            //执行任务。
            isRunning.setConfigKey(TRUE);
            showLog(logFlag,1,schedulerLock.getName()+"定时任务上锁成功");
        }else if (schedulerLock!=null && schedulerLock.getStatus()== StatusEnum.EXECUTING && schedulerLock.getOnOff().equals(Boolean.TRUE)){
            //定时任务因为重新部署等原因导致中断的情况，任务状态仍在执行中。
            if (schedulerLock.getMaxRunTime()!=null && schedulerLock.getLastRunTime()!=null){
                //判断当前时间>上次执行时间+最长执行时间，是否满足触发条件。
                if (DateUtils.isSecondAfter(schedulerLock.getLastUpdateDate(),schedulerLock.getMaxRunTime())){
                    schedulerLock.setStatus(StatusEnum.UNEXECUTED);
                    getBaseDao().save(schedulerLock);
                    showLog(logFlag,1,schedulerLock.getName()+"定时任务锁：重置状态完成，等待下次执行。");
                }
            }
        }else {
            showLog(logFlag,1,"定时任务上锁失败......");
        }
    }

    @Override
    public void releaseLock(Long id) {
        SchedulerLock schedulerLock = getBaseDao().findOne(id);
        if (schedulerLock!=null && schedulerLock.getStatus()== StatusEnum.EXECUTING){
            schedulerLock.setStatus(StatusEnum.UNEXECUTED);
            getBaseDao().save(schedulerLock);
            showLog(logFlag,1,schedulerLock.getName()+"定时任务释放锁成功");
        }else if (schedulerLock!=null && schedulerLock.getStatus()== StatusEnum.UNEXECUTED){
            showLog(logFlag,1,schedulerLock.getName()+"无需释放锁资源");
        }else {
            showLog(logFlag,1,"不存在当前id["+id+"]的锁。");
        }
    }

    @Override
    public void isRunningFun(Long id, ConfigDto isRunning) {
        //是否启用任务调度锁功能（集群环境下使用乐观锁确保只有一台主机执行任务）
        ConfigPo configPo = null;
        try {
            configPo = configDao.findByConfigKey("scheduler");
        } catch (Exception e) {
            log.error("查找配置项scheduler异常");
        }

        try {
            ConfigPo configPo2 = configDao.findByConfigKey("schedulerLog");
            if (configPo2!=null && "true".equals(configPo2.getConfigValue())){
                logFlag = true;
            }else {
                logFlag = false;
            }
        } catch (Exception e) {
            logFlag = false;
            log.error("查找配置项schedulerLog异常");
        }

        if (configPo!=null && "true".equals(configPo.getConfigValue())){
            //上锁
            lockUp(id,isRunning);
        } else {
            showLog(logFlag, 1, "不启用任务调度锁功能，使用配置的ip来控制定时任务调度");
            if (configService.isRunningScheduledIp()) {
                isRunning.setConfigKey(TRUE);
            } else {
                showLog(logFlag, 2, "本台服务器位置配置的ip列表中，不执行任务。");
            }
        }
    }

    /**
     * 打印日志方法
     * @param logFlag true 打印；false 不打印
     * @param level 1:info ; 2:warn  ; 3:error
     * @param logMsg 具体的日志信息
     */
    private void showLog(Boolean logFlag,Integer level,String logMsg){
        if (logFlag){
            switch (level){
                case 1: log.info(logMsg);break;
                case 2: log.warn(logMsg);break;
                case 3: log.error(logMsg);break;
                default:log.info(logMsg);
            }
        }
    }
}

package com.ideatech.ams.system.schedule.service;

import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.schedule.dto.SchedulerLockDTO;
import com.ideatech.common.service.BaseService;

/**
 * @author jzh
 * @date 2019-08-21.
 */
public interface SchedulerLockService extends BaseService<SchedulerLockDTO> {

    SchedulerLockDTO findOneByName(String name);

    /**
     * 原有逻辑判断：是否是配置的ip
     * @param isRunning
     */
    @Deprecated
    void isSystemIP(ConfigDto isRunning);

    /**
     * 上锁
     * @param id
     * @param isRunning
     */
    void lockUp(Long id ,ConfigDto isRunning);

    /**
     * 释放锁
     * @param id
     */
    void releaseLock(Long id);

    /**
     * 是否执行任务（封装上锁和是否配置IP操作）判断方法
     * @param id
     * @param isRunning
     */
    void isRunningFun(Long id ,ConfigDto isRunning);
}

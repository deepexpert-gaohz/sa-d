package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.CompareTaskDto;
import com.ideatech.ams.compare.enums.CompareState;
import com.ideatech.ams.compare.enums.TaskType;

/**
 * 比对管理-定时任务
 */
public interface CompareScheduledTaskService {
    /**
     * 确认和启动定时的比对管理任务
     * @param taskType
     * @param state
     */
    void checkAndStartScheduledTask(TaskType taskType, CompareState... state);

    /**
     * 定时采集任务启动开始
     * @param taskType
     * @param state
     */
    void checkAndStartScheduledCollectionTask(TaskType taskType,CompareState... state);
    /**
     * 保存启动后的比对管理任务
     * @param compareTaskDto
     */
    void saveStartTask(CompareTaskDto compareTaskDto);

    /**
     * 开始比对任务
     * @param taskType
     * @param state
     */
    void doCompare(TaskType taskType, CompareState... state);
}

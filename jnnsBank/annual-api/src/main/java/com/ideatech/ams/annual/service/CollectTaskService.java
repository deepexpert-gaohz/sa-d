package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.CollectTaskDto;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.common.service.BaseService;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface CollectTaskService extends BaseService<CollectTaskDto> {
    CollectTaskDto findLastTaskByTypeAndAnnualTaskId(DataSourceEnum collectTaskType, Long taskId);
    CollectTaskDto findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum collectTaskType, Long taskId);
    void deleteByTaskId(Long taskId);
    void deleteByAnnualTaskIdAndCollectTaskType(Long taskId,DataSourceEnum collectTaskType);
}

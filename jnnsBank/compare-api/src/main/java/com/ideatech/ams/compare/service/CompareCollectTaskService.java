package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.CompareCollectTaskDto;
import com.ideatech.ams.compare.enums.CollectTaskState;
import com.ideatech.ams.compare.enums.DataSourceEnum;

import java.util.List;

/**
 * @Description 比对管理--采集任务服务层
 * @Author wanghongjie
 * @Date 2019/2/11
 **/
public interface CompareCollectTaskService {
    CompareCollectTaskDto findById(Long id);
    void saveCompareCollectTask(CompareCollectTaskDto compareCollectTaskDto);
    CompareCollectTaskDto findByCompareTaskIdAndCollectTaskType(Long compareTaskId, DataSourceEnum collectTaskType);
    List<CompareCollectTaskDto> findByCompareTaskIdAndCollectStatus(Long compareTaskId, CollectTaskState collectStatus);
    CompareCollectTaskDto findByCompareTaskIdAndDataSourceId(Long compareTaskId,Long dataSourceId);
    List<CompareCollectTaskDto> findByCompareTaskId(Long compareTaskId);
}

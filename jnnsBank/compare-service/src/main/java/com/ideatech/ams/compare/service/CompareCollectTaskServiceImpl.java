package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dao.CompareCollectTaskDao;
import com.ideatech.ams.compare.dao.CompareDefineDao;
import com.ideatech.ams.compare.dto.CompareCollectTaskDto;
import com.ideatech.ams.compare.dto.CompareDefineDto;
import com.ideatech.ams.compare.entity.CompareCollectTask;
import com.ideatech.ams.compare.entity.CompareDefine;
import com.ideatech.ams.compare.enums.CollectTaskState;
import com.ideatech.ams.compare.enums.DataSourceEnum;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description 比对管理--任务采集实现层
 * @Author wanghongjie
 * @Date 2019/2/12
 **/
@Slf4j
@Service
//@Transactional
public class CompareCollectTaskServiceImpl extends BaseServiceImpl<CompareCollectTaskDao, CompareCollectTask, CompareCollectTaskDto> implements CompareCollectTaskService {


    @Transactional
    @Override
    public void saveCompareCollectTask(CompareCollectTaskDto compareCollectTaskDto) {
        CompareCollectTask compareCollectTask = null;
        if(compareCollectTaskDto.getId() != null){
            compareCollectTask = getBaseDao().findOne(compareCollectTaskDto.getId());
            ConverterService.convert(compareCollectTaskDto,compareCollectTask);
//            BeanUtils.copyProperties(compareCollectTaskDto,compareCollectTask);
        }else{
            compareCollectTask = ConverterService.convert(compareCollectTaskDto, CompareCollectTask.class);
        }
        getBaseDao().save(compareCollectTask);
        compareCollectTaskDto.setId(compareCollectTask.getId());
    }

    @Override
    public CompareCollectTaskDto findByCompareTaskIdAndCollectTaskType(Long compareTaskId, DataSourceEnum collectTaskType) {
        CompareCollectTask compareCollectTask = getBaseDao().findByCompareTaskIdAndCollectTaskType(compareTaskId, collectTaskType);
        if(compareCollectTask == null){
            return null;
        }else{
            return ConverterService.convert(compareCollectTask,CompareCollectTaskDto.class);
        }
    }

    @Override
    public List<CompareCollectTaskDto> findByCompareTaskIdAndCollectStatus(Long compareTaskId, CollectTaskState collectStatus) {
        List<CompareCollectTask> compareCollectTaskList = getBaseDao().findByCompareTaskIdAndCollectStatus(compareTaskId, collectStatus);
        if(compareCollectTaskList == null){
            return null;
        }else{
            return ConverterService.convertToList(compareCollectTaskList,CompareCollectTaskDto.class);
        }
    }

    @Override
    public CompareCollectTaskDto findByCompareTaskIdAndDataSourceId(Long compareTaskId, Long dataSourceId) {
        CompareCollectTask compareCollectTask = getBaseDao().findByCompareTaskIdAndDataSourceId(compareTaskId, dataSourceId);
        if(compareCollectTask == null){
            return null;
        }else{
            return ConverterService.convert(compareCollectTask,CompareCollectTaskDto.class);
        }
    }

    @Override
    public List<CompareCollectTaskDto> findByCompareTaskId(Long compareTaskId) {
        List<CompareCollectTask> compareCollectTaskList = getBaseDao().findByCompareTaskId(compareTaskId);
        return ConverterService.convertToList(compareCollectTaskList,CompareCollectTaskDto.class);
    }
}

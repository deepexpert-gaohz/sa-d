package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.CollectTaskDao;
import com.ideatech.ams.annual.dto.CollectTaskDto;
import com.ideatech.ams.annual.entity.CollectTask;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Description 收集任务
 * @Author wanghongjie
 * @Date 2018/8/9
 **/
@Service
@Transactional
@Slf4j
public class CollectTaskServiceImpl extends BaseServiceImpl<CollectTaskDao, CollectTask, CollectTaskDto> implements CollectTaskService {

    @Override
    public CollectTaskDto findLastTaskByTypeAndAnnualTaskId(DataSourceEnum collectTaskType, Long taskId) {
        List<CollectTaskDto> collectTaskDtoList = ConverterService.convertToList(getBaseDao().findLastTaskByTypeAndAnnualTaskIdAnd(collectTaskType,taskId), CollectTaskDto.class);
        if(collectTaskDtoList != null && collectTaskDtoList.size()>0){
            return collectTaskDtoList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public CollectTaskDto findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(DataSourceEnum collectTaskType, Long taskId) {
        List<CollectTaskDto> collectTaskDtoList = ConverterService.convertToList(getBaseDao().findLastTaskByTypeAndAnnualTaskIdAndNotCompleted(collectTaskType,taskId), CollectTaskDto.class);
        if(collectTaskDtoList != null && collectTaskDtoList.size()>0){
            return collectTaskDtoList.get(0);
        }else{
            return null;
        }
    }

	@Override
	public void deleteByTaskId(Long taskId) {
    	getBaseDao().deleteByAnnualTaskId(taskId);
	}

    @Override
    public void deleteByAnnualTaskIdAndCollectTaskType(Long taskId,DataSourceEnum collectTaskType) {
        getBaseDao().deleteByAnnualTaskIdAndCollectTaskType(taskId,collectTaskType);
    }
}

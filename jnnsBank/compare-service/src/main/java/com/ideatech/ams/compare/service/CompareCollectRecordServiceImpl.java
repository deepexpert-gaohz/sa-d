package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dao.CompareCollectRecordDao;
import com.ideatech.ams.compare.dao.spec.CompareCollectRecordSpec;
import com.ideatech.ams.compare.dto.CompareCollectRecordDto;
import com.ideatech.ams.compare.entity.CompareCollectRecord;
import com.ideatech.ams.compare.entity.CompareCollectTask;
import com.ideatech.ams.compare.enums.CollectState;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class CompareCollectRecordServiceImpl extends BaseServiceImpl<CompareCollectRecordDao, CompareCollectRecord, CompareCollectRecordDto> implements CompareCollectRecordService {

    @Transactional
    @Override
    public void saveCompareCollectRecord(CompareCollectRecordDto compareCollectRecordDto) {

        CompareCollectRecord compareCollectRecord = new CompareCollectRecord();
        if(compareCollectRecordDto.getId() != null){
            compareCollectRecord = getBaseDao().findOne(compareCollectRecordDto.getId());
            if(compareCollectRecord == null){
                compareCollectRecord = new CompareCollectRecord();
            }
        }else{
            compareCollectRecord = new CompareCollectRecord();
        }
        ConverterService.convert(compareCollectRecordDto,compareCollectRecord);
        getBaseDao().save(compareCollectRecord);
    }

    @Override
    public List<CompareCollectRecordDto> findByCollectTaskIdAndCompareTaskId(Long collectTaskId, Long compareTaskId) {
        return ConverterService.convertToList(getBaseDao().findByCollectTaskIdAndCompareTaskId(collectTaskId,compareTaskId),CompareCollectRecordDto.class);
    }

    @Override
    public List<CompareCollectRecordDto> findByCollectTaskIdAndCompareTaskIdAndCollectState(Long collectTaskId, Long compareTaskId, CollectState collectState) {
        return ConverterService.convertToList(getBaseDao().findByCollectTaskIdAndCompareTaskIdAndCollectState(collectTaskId,compareTaskId,collectState),CompareCollectRecordDto.class);
    }

    @Override
    public List<CompareCollectRecordDto> findByTaskId(CompareCollectRecordDto dto, Pageable pageable) {
        Page<CompareCollectRecord> list = getBaseDao().findAll(new CompareCollectRecordSpec(dto),pageable);
        List<CompareCollectRecordDto> compareCollectRecordDtos = ConverterService.convertToList(list.getContent(),CompareCollectRecordDto.class);
        return compareCollectRecordDtos;
    }

    @Override
    public long findByTaskId(CompareCollectRecordDto dto) {
        return getBaseDao().count(new CompareCollectRecordSpec(dto));
    }

    @Override
    public CompareCollectRecordDto findByCompareTaskIdAndAcctNoAndDataSourceType(Long taskId, String acctNo, String dataSourceType) {
        return ConverterService.convert(getBaseDao().findByCompareTaskIdAndAcctNoAndDataSourceType(taskId,acctNo,dataSourceType),CompareCollectRecordDto.class);
    }

    @Override
    public CompareCollectRecordDto findByCollectTaskIdAndCompareTaskIdAndAcctNo(Long collectTaskId, Long compareTaskId, String acctNo) {
        return ConverterService.convert(getBaseDao().findByCollectTaskIdAndCompareTaskIdAndAcctNo(collectTaskId,compareTaskId,acctNo),CompareCollectRecordDto.class);
    }

    @Transactional
    @Override
    public void deleteData(Long taskId, String dataSourceType) {
        List<CompareCollectRecord> list = getBaseDao().findByCompareTaskIdAndDataSourceType(taskId,dataSourceType);
        if(CollectionUtils.isNotEmpty(list)){
            getBaseDao().delete(list);
        }
    }
}

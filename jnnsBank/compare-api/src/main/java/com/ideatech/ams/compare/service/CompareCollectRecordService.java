package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.CompareCollectRecordDto;
import com.ideatech.ams.compare.dto.CompareCollectTaskDto;
import com.ideatech.ams.compare.enums.CollectState;
import com.ideatech.ams.compare.enums.DataSourceEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Description 比对管理--采集记录服务层
 * @Author wanghongjie
 * @Date 2019/2/11
 **/
public interface CompareCollectRecordService {
    CompareCollectRecordDto findById(Long id);
    void saveCompareCollectRecord(CompareCollectRecordDto compareCollectRecordDto);
    List<CompareCollectRecordDto>  findByCollectTaskIdAndCompareTaskId(Long collectTaskId,Long compareTaskId);
    List<CompareCollectRecordDto>  findByCollectTaskIdAndCompareTaskIdAndCollectState(Long collectTaskId, Long compareTaskId, CollectState collectState);
    List<CompareCollectRecordDto> findByTaskId(CompareCollectRecordDto compareCollectRecordDto, Pageable pageable);
    long findByTaskId(CompareCollectRecordDto dto);
    CompareCollectRecordDto findByCompareTaskIdAndAcctNoAndDataSourceType(Long taskId,String acctNo,String dataSourceType);
    CompareCollectRecordDto findByCollectTaskIdAndCompareTaskIdAndAcctNo(Long collectTaskId,Long compareTaskId,String acctNo);
    /**
     * 删除采集数据
     * @param taskId
     * @param dataSourceType
     */
    void deleteData(Long taskId,String dataSourceType);
}

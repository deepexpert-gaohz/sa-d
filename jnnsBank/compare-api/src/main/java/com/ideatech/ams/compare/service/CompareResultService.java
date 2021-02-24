package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.CompareResultDto;
import com.ideatech.ams.compare.dto.CompareTaskDto;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetailDto;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetailListDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface CompareResultService extends BaseService<CompareResultDto> {
    TableResultResponse query(CompareResultDto dto, Pageable pageable) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, Exception;

    CompareResultDetailDto detail(CompareResultDto dto) throws IllegalAccessException, Exception;

    CompareResultDto getOne(Long id);

    Map<String,Object> getCompareResultFieldStruct(Long taskId);

    Map<String, Object> getCompareResultFieldsStruct(CompareTaskDto compareTaskDto);

    long countByTaskId(Long taskId);

    List<CompareResultDetailListDto> getCompareResultData(CompareResultDto compareResult, CompareResultDetailDto compareResultDetailDto, List<CompareResultDetailListDto> compareResultDetailListDtos);

    //不同数据源字段值不同的标红(isEquals赋值)
    void setIsEquals(List<CompareResultDetailListDto> compareResultDetailListDtos);

    List<CompareResultDto> findAllByCondition(CompareResultDto dto);

    void deleteAllByCompareTaskId(Long compareTaskId);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<CompareResultDto> findAllByConditionNew(CompareResultDto dto);

    long count(CompareResultDto condition);

    CompareResultDto findByTaskIdAndDepositorName(Long compareTaskId, String depositorName);
}

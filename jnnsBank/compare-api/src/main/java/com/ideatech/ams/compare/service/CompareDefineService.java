package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.CompareDefineDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 * @author jzh
 * @date 2019/1/17.
 */
public interface CompareDefineService extends BaseService<CompareDefineDto> {

    /**
     * 根据任务id获取规则
     *
     * @param ruleId
     * @return
     */
    List<CompareDefineDto> findByCompareRuleId(Long ruleId);

    CompareDefineDto findByCompareRuleIdAndDataSourceIdAndCompareFieldId(Long compareRuleId, Long dataSourceId, Long compareFieldId);

    List<CompareDefineDto> getCompareDefineList(Long taskId);
}

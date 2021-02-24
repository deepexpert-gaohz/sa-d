package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.CompareRuleDataSourceDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 * @author jzh
 * @date 2019/1/17.
 */
public interface CompareRuleDataSourceService extends BaseService<CompareRuleDataSourceDto> {

    /**
     * 根据CompareRule的id获取CompareRuleDataSourceDto数组
     * @param ruleId
     * @return
     */
    List<CompareRuleDataSourceDto> findByCompareRuleId(Long ruleId);

    /**
     * 根据CompareRule和datasource的id获取CompareRuleDataSourceDto数组
     * @param compareRuleId
     * @param dataSourceId
     * @return
     */
    CompareRuleDataSourceDto findByCompareRuleIdAndDataSourceId(Long compareRuleId,Long dataSourceId);

    void deleteAllByCompareRuleId(Long id);

    /**
     *  删除
     */
    List<CompareRuleDataSourceDto> findByDataSoureIdAndActive(Long id,Boolean active);
}

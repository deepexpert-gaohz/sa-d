package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.CompareRuleFieldsDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 * @author jzh
 * @date 2019/1/17.
 */
public interface CompareRuleFieldsService extends BaseService<CompareRuleFieldsDto> {
    List<CompareRuleFieldsDto> findByCompareRuleId(Long ruleId);
}

package com.ideatech.ams.risk.rule.service;

import com.ideatech.ams.risk.rule.dto.RuleConfigureDto;
import com.ideatech.ams.risk.rule.dto.RuleFieldDto;
import com.ideatech.ams.risk.rule.dto.RuleSearchDto;

import java.util.List;

public interface RuleConfigService {
    RuleSearchDto findRuleFieldByModelId(RuleSearchDto ruleSearchDto);

    List<RuleFieldDto> findByModelId(String modelId);

    void saveRuleConfiger(RuleConfigureDto ruleConfigureDto);

    RuleConfigureDto  findConfigerById(Long id);

    void deleteConfiger(Long id);

    RuleFieldDto  findFieldById(Long id);

    List<RuleConfigureDto> getModelRuleByModelId(String modelId, String corporateBank);

    Boolean initRule();
}

package com.ideatech.ams.compare.service;


import com.ideatech.ams.compare.dao.CompareRuleFieldsDao;
import com.ideatech.ams.compare.dto.CompareFieldDto;
import com.ideatech.ams.compare.dto.CompareRuleDto;
import com.ideatech.ams.compare.dto.CompareRuleFieldsDto;
import com.ideatech.ams.compare.entity.CompareRuleFields;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jzh
 * @date 2019/1/17.
 */
@Service
public class CompareRuleFieldsServiceImpl extends BaseServiceImpl<CompareRuleFieldsDao, CompareRuleFields, CompareRuleFieldsDto> implements CompareRuleFieldsService {

    @Autowired
    private CompareRuleService compareRuleService;

    @Autowired
    private CompareFieldService compareFieldService;

    @Override
    public List<CompareRuleFieldsDto> findByCompareRuleId(Long ruleId) {
        List<CompareRuleFieldsDto> compareRuleFieldsDtos = ConverterService.convertToList(getBaseDao().findAllByCompareRuleId(ruleId), CompareRuleFieldsDto.class);
        CompareRuleDto compareRuleDto = compareRuleService.findById(ruleId);
        fillDtos(compareRuleFieldsDtos, compareRuleDto, null);
        return compareRuleFieldsDtos;
    }


    private void fillDtos(List<CompareRuleFieldsDto> compareRuleFieldsDtos, CompareRuleDto compareRuleDto, CompareFieldDto compareFieldDto) {

        if (CollectionUtils.isNotEmpty(compareRuleFieldsDtos)) {
            for (CompareRuleFieldsDto compareRuleFieldsDto : compareRuleFieldsDtos) {
                CompareRuleDto ruleDto = compareRuleDto;
                if (ruleDto == null) {
                    ruleDto = compareRuleService.findById(compareRuleFieldsDto.getCompareRuleId());
                }
                CompareFieldDto fieldDto = compareFieldDto;
                if (fieldDto == null) {
                    fieldDto = compareFieldService.findById(compareRuleFieldsDto.getCompareFieldId());
                }
                compareRuleFieldsDto.setCompareFieldDto(fieldDto);
            }
        }

    }

}

package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dao.CompareDefineDao;
import com.ideatech.ams.compare.dto.*;
import com.ideatech.ams.compare.entity.CompareDefine;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jzh
 * @date 2019/1/17.
 */

@Service
public class CompareDefineServiceImpl extends BaseServiceImpl<CompareDefineDao, CompareDefine, CompareDefineDto> implements CompareDefineService {

    @Autowired
    private CompareTaskService compareTaskService;

    @Autowired
    private CompareRuleService compareRuleService;

    @Autowired
    private CompareFieldService compareFieldService;

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public List<CompareDefineDto> findByCompareRuleId(Long ruleId) {
        List<CompareDefineDto> compareDefineDtos = ConverterService.convertToList(
                getBaseDao().findByCompareRuleId(ruleId), CompareDefineDto.class);
        CompareRuleDto compareRuleDto = compareRuleService.findById(ruleId);
        fillDtos(compareDefineDtos, compareRuleDto, null, null);
        return compareDefineDtos;
    }

    @Override
    public CompareDefineDto findByCompareRuleIdAndDataSourceIdAndCompareFieldId(Long compareRuleId, Long dataSourceId, Long compareFieldId) {

        CompareDefineDto compareDefineDto = ConverterService.convert(
                getBaseDao().findByCompareRuleIdAndDataSourceIdAndCompareFieldId(compareRuleId, dataSourceId, compareFieldId), CompareDefineDto.class);

        CompareRuleDto compareRuleDto = compareRuleService.findById(compareDefineDto.getCompareRuleId());
        CompareFieldDto compareFieldDto = compareFieldService.findById(compareDefineDto.getCompareFieldId());
        DataSourceDto dataSourceDto = dataSourceService.findById(compareDefineDto.getDataSourceId());

        List<CompareDefineDto> compareDefineDtos = new ArrayList<>(1);
        compareDefineDtos.add(compareDefineDto);

        fillDtos(compareDefineDtos, compareRuleDto, compareFieldDto, dataSourceDto);
        return compareDefineDtos.get(0);

    }

    @Override
    public List<CompareDefineDto> getCompareDefineList(Long taskId) {
        CompareTaskDto compareTaskDto = compareTaskService.findById(taskId);
        List<CompareDefine> compareDefineList = getBaseDao().findByCompareRuleIdAndActive(compareTaskDto.getCompareRuleId(), true);

        List<CompareDefineDto> dtoList = ConverterService.convertToList(compareDefineList, CompareDefineDto.class);

        return dtoList;
    }


    private void fillDtos(List<CompareDefineDto> compareDefineDtos, CompareRuleDto compareRuleDto, CompareFieldDto compareFieldDto, DataSourceDto dataSourceDto) {
        if (CollectionUtils.isNotEmpty(compareDefineDtos)) {
            for (CompareDefineDto compareDefineDto : compareDefineDtos) {
                CompareRuleDto ruleDto = compareRuleDto;
                if (ruleDto == null) {
                    ruleDto = compareRuleService.findById(compareDefineDto.getCompareRuleId());
                }
                compareDefineDto.setCompareRuleDto(ruleDto);

                CompareFieldDto fieldDto = compareFieldDto;
                if (fieldDto == null) {
                    fieldDto = compareFieldService.findById(compareDefineDto.getCompareFieldId());
                }
                compareDefineDto.setCompareFieldDto(fieldDto);

                DataSourceDto dataSource = dataSourceDto;
                if (dataSource == null) {
                    dataSource = dataSourceService.findById(compareDefineDto.getDataSourceId());
                }
                compareDefineDto.setDataSourceDto(dataSource);
            }
        }
    }


}

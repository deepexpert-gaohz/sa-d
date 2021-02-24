package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dao.CompareRuleDataSourceDao;
import com.ideatech.ams.compare.dto.CompareRuleDataSourceDto;
import com.ideatech.ams.compare.dto.CompareRuleDto;
import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.entity.CompareRuleDataSource;
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
public class CompareRuleDataSourceServiceImpl extends BaseServiceImpl<CompareRuleDataSourceDao, CompareRuleDataSource, CompareRuleDataSourceDto> implements CompareRuleDataSourceService {

    @Autowired
    private CompareRuleService compareRuleService;

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public List<CompareRuleDataSourceDto> findByCompareRuleId(Long ruleId) {
        List<CompareRuleDataSourceDto> compareRuleDataSourceDtos = ConverterService.convertToList(getBaseDao().findByCompareRuleId(ruleId), CompareRuleDataSourceDto.class);
        CompareRuleDto compareRuleDto = compareRuleService.findById(ruleId);
        fillDtos(compareRuleDataSourceDtos, compareRuleDto, null);
        return compareRuleDataSourceDtos;
    }

    @Override
    public CompareRuleDataSourceDto findByCompareRuleIdAndDataSourceId(Long compareRuleId, Long dataSourceId) {
        CompareRuleDataSource sourceIdAndActive = getBaseDao().findAllByCompareRuleIdAndDataSourceIdAndActive(compareRuleId, dataSourceId, true);
        if (sourceIdAndActive == null) {
            return null;
        } else {
            return ConverterService.convert(sourceIdAndActive, CompareRuleDataSourceDto.class);
        }
    }

    @Override
    public void deleteAllByCompareRuleId(Long id) {
        getBaseDao().deleteAllByCompareRuleId(id);
    }

    @Override
    public List<CompareRuleDataSourceDto> findByDataSoureIdAndActive(Long id, Boolean active) {
        List<CompareRuleDataSourceDto> list = ConverterService.convertToList(getBaseDao().findByDataSourceIdAndActive(id, active), CompareRuleDataSourceDto.class);
        return list;
    }


    private void fillDtos(List<CompareRuleDataSourceDto> compareRuleDataSourceDtos, CompareRuleDto compareRuleDto, DataSourceDto dataSourceDto) {
        if (CollectionUtils.isNotEmpty(compareRuleDataSourceDtos)) {
            for (CompareRuleDataSourceDto compareRuleDataSourceDto : compareRuleDataSourceDtos) {
                CompareRuleDto ruleDto = compareRuleDto;
                if (ruleDto == null) {
                    ruleDto = compareRuleService.findById(compareRuleDataSourceDto.getCompareRuleId());
                }
                compareRuleDataSourceDto.setCompareRuleDto(ruleDto);

                DataSourceDto dataSource = dataSourceDto;
                if (dataSource == null) {
                    dataSource = dataSourceService.findById(compareRuleDataSourceDto.getDataSourceId());
                }
                compareRuleDataSourceDto.setDataSourceDto(dataSource);
            }
        }

    }
}

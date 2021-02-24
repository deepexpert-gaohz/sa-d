package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author vantoo
 * @date 2019-01-22 19:10
 */
public interface CompareDataService {

    /**
     * 根据数据源保存比对数据
     *
     * @param dataSourceDto
     */
    void saveCompareData(CompareDataDto compareDataDto, DataSourceDto dataSourceDto);

    /**
     * 根据taskId以及数据源获取数据
     * @param taskId
     * @param dataSourceDto
     * @return
     */
    List<CompareDataDto> getCompareData(Long taskId, DataSourceDto dataSourceDto);

    /**
     * 根据taskId以及数据源获取数据查询分页
     * @param taskId
     * @param dataSourceDto
     * @return
     */
    List<CompareDataDto> getCompareData(Long taskId, DataSourceDto dataSourceDto, Pageable pageable);

    /**
     * 获取需要比对的账号
     * @param taskId
     * @param dataSourceDto
     * @return
     */
    List<String> getCompareDataAcctNo(Long taskId, DataSourceDto dataSourceDto);

    /**
     * 删除比对数据
     *
     */
    void delCompareData(Long taskId,DataSourceDto dataSourceDto);

}

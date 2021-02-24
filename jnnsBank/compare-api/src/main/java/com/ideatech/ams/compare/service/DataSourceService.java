package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.DataSourceDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

public interface DataSourceService extends BaseService<DataSourceDto> {
    /**
     * 查询数据源
     * @param dto
     * @return
     */
    TableResultResponse<DataSourceDto> query(DataSourceDto dto,Pageable pageable) throws Exception;

    /**
     * 更新数据
     */
    void updateSource(DataSourceDto dto);

    void DataSourceSave(DataSourceDto dto);

    void deleteDataSource(Long id);

    DataSourceDto findByName(String name);
}

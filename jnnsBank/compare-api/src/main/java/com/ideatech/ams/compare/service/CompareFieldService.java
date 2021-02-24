package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.CompareFieldDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompareFieldService extends BaseService<CompareFieldDto> {
    /**
     * 查询所有比对字段
     * @param dto
     * @param pageable
     * @return
     */
    TableResultResponse query(CompareFieldDto dto, Pageable pageable);

    /**
     * 取出所有比对字段
     * @return
     */
    List<CompareFieldDto> getAll();

    CompareFieldDto findByName(String name);

    CompareFieldDto findByField(String field);
}

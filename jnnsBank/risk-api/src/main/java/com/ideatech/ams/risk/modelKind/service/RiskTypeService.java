package com.ideatech.ams.risk.modelKind.service;

import com.ideatech.ams.risk.modelKind.dto.RiskTypeSearchDto;
import com.ideatech.ams.risk.modelKind.dto.RiskTypeDto;

import java.util.List;

/**
 * @auther zhuqr
 * @date ${date} ${time}
 */
public interface RiskTypeService {

    /**
     *分页模糊查询
     * @param riskTypeSearchDto
     * @return
     */
    RiskTypeSearchDto search(RiskTypeSearchDto riskTypeSearchDto);

    /**
     * 保存风险类型
     * @param riskTypeDto
     */
    void save(RiskTypeDto riskTypeDto);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    RiskTypeDto getById(Long id);


    /**
     * 删除
     * @param id
     */
    void delType(Long id);

    RiskTypeDto findByTypeName(String typeName);

    List<RiskTypeDto> findAll();
}

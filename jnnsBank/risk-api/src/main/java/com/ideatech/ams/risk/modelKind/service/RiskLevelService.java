package com.ideatech.ams.risk.modelKind.service;

import com.ideatech.ams.risk.modelKind.dto.RiskLevelDto;
import com.ideatech.ams.risk.modelKind.dto.RiskLevelSearchDto;

import java.util.List;

/**
 * @auther zhuqr
 * @date ${date} ${time}
 */
public interface RiskLevelService {
    /**
     * 新建风险等级
     * @param riskLevelDto
     */
    void save(RiskLevelDto riskLevelDto);

    /**
     * 分页模糊查询
     * @param riskLevelSearchDto
     * @return
     */
    RiskLevelSearchDto search(RiskLevelSearchDto riskLevelSearchDto);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    RiskLevelDto findById(Long id);


    void delLevel(Long id);

    RiskLevelDto findByLevelName(String leveLName);

    List<RiskLevelDto> findAll();

}

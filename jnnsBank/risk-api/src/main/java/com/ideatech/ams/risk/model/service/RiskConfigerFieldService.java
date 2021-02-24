package com.ideatech.ams.risk.model.service;


import com.ideatech.ams.risk.model.dto.RiskConfigerFieldDto;

import java.util.List;


public interface RiskConfigerFieldService {

    List<RiskConfigerFieldDto> findAll();

    RiskConfigerFieldDto findByField(String field, String code);

    void save(RiskConfigerFieldDto riskConfigerFieldDto);
}

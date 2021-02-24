package com.ideatech.ams.risk.model.service;

import com.ideatech.ams.risk.model.dto.ModelSearchExtendDto;
import com.ideatech.ams.risk.model.dto.RiskDataDto;
import com.ideatech.ams.risk.modelKind.dto.RiskTypeDto;
import com.ideatech.ams.risk.riskdata.dto.RiskDetailsSearchDto;
import com.ideatech.ams.risk.riskdata.entity.RiskTradeInfo;

import java.util.List;

public interface RiskDataServiceToExp {

    RiskDetailsSearchDto findRiskListDetails(RiskDetailsSearchDto riskDetailsSearchDto);

    String createQuerySQL(RiskDetailsSearchDto riskDetailsSearchDto);

    RiskDetailsSearchDto findRiskListExpDetails(RiskDetailsSearchDto riskDetailsSearchDto);

    RiskDetailsSearchDto findModelByRiskid(RiskDetailsSearchDto riskDetailsSearchDto, String code);

    List<Object[]> findRiskTradeInfo(String modelId, String code);

    String findModelType(String modelName, String code);
}

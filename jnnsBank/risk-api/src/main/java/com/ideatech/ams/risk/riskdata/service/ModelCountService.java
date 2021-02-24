package com.ideatech.ams.risk.riskdata.service;

import com.ideatech.ams.risk.model.dto.ModelDto;
import com.ideatech.ams.risk.riskdata.dto.ModelCountDto;

import java.util.List;

public interface ModelCountService {
    List<ModelDto> findModelCountByCjrq(String cjrq, String orgId, String khId);

    ModelCountDto findModelCountAndModelsId(Long id);

    ModelCountDto findById(Long id);

    void upDate(ModelCountDto modelCountDto);

    ModelCountDto findTradeNearDate();

}

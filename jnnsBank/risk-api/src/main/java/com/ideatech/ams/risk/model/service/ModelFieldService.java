package com.ideatech.ams.risk.model.service;

import com.ideatech.ams.risk.model.dto.ModelFieldDto;
import com.ideatech.ams.risk.riskdata.dto.RiskDetailsSearchDto;

import java.util.List;
import java.util.Map;

public interface ModelFieldService {

    /*List findRiskDataFieldZH(ModelFieldDto modelFieldDto);*/

    List findRiskDataFieldZH(ModelFieldDto modelFieldDto);

    List<ModelFieldDto> findAllByModelId(String tableName);

    List<ModelFieldDto> findAllByModelIdAndExportFlagOrderByOrderFlag(ModelFieldDto modelFieldDto);

    void saveModelField(ModelFieldDto modelFieldDto);

    List<Map<String, Object>> getRiskMapList(List list, RiskDetailsSearchDto riskDetailsSearchDto);

    void delete(ModelFieldDto modelFieldDto);

    boolean modelFieldInit();

    void initModelFields(String modelId);
}

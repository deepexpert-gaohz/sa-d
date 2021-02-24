package com.ideatech.ams.risk.modelFlow.service;

import com.ideatech.ams.risk.model.dto.ModelSearchDto;
import com.ideatech.ams.risk.modelFlow.dto.ModelFlowConfigerDto;
import com.ideatech.ams.risk.modelFlow.dto.ModelFlowConfigerSearchDto;

public interface ModelFlowConfigerService {
    /**
     * 新建方法
     * @param modelFlowConfigerDto 模型流程配置Dto
     */
    void saveModelFlowConfiger(ModelFlowConfigerDto modelFlowConfigerDto);


    /**
     * 逻辑删除
     * @param
     */
    void delModelFlowConfiger(Long id);

    ModelFlowConfigerSearchDto searchData(ModelFlowConfigerSearchDto modelFlowConfigerSearchDto);

    ModelFlowConfigerDto findById(Long id);

    ModelFlowConfigerDto findByModelId(String modelId);

    ModelFlowConfigerDto findFlowkeyAndNameByModelId(String key);
   // ModelFlowConfigerDto findFlowkeyAndNameByModelId(String modelId);

    ModelSearchDto getModels(ModelSearchDto modelSearchDto);
}

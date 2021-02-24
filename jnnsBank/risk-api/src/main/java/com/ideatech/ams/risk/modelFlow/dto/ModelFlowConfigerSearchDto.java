package com.ideatech.ams.risk.modelFlow.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class ModelFlowConfigerSearchDto extends PagingDto<ModelFlowConfigerDo> {
    private String flowKey;//流程编码
    private String modelId;//模型编号
    private String isAuto;// is '是否自动分配（1:是,0:否）'
}

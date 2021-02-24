package com.ideatech.ams.risk.model.service;

import com.ideatech.ams.risk.model.dto.ModelRelationDto;

import java.util.List;

public interface ModelRelationService  {
    List<ModelRelationDto> getBooldRelationList(ModelRelationDto modelRelationDto);

    String getModelsTableName(String mTable);
}

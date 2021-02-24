package com.ideatech.ams.risk.rely.service;

import com.ideatech.ams.risk.rely.dto.ModelRelyDto;
import com.ideatech.ams.risk.rely.dto.ModelRelyMapDto;
import com.ideatech.ams.risk.rely.dto.ModelRelySearchDto;

import java.util.List;


public interface ModelRelyService {
    void save(ModelRelyDto modelRelyDto);

    ModelRelySearchDto search(ModelRelySearchDto modelRelySearchDto);

    void deleteById(Long id);

    ModelRelyMapDto findByModelTable(String mTable);

    ModelRelyDto findModelRelyDtoByModelTable(String modelTable);

    ModelRelyDto getModelRelyById(Long id);
}

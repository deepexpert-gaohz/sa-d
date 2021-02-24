package com.ideatech.ams.risk.model.service;


import com.ideatech.ams.risk.account.dto.AccountTransactionRiskSearchDto;
import com.ideatech.ams.risk.model.dto.ModelSearchExtendDto;
import com.ideatech.ams.risk.model.dto.ModelsExtendDto;
import com.ideatech.ams.system.org.dto.OrganizationDto;

import java.util.HashMap;
import java.util.List;

public interface ModelCountFileService {

    ModelSearchExtendDto queryModelCountFile2(ModelSearchExtendDto modelSearchExtendDto);

    List<HashMap<String,String>> getAllModelTypeList();

    List<ModelsExtendDto> findModelExtendList(ModelsExtendDto modelsExtendDto);

    List<ModelsExtendDto> findModelExtendList(ModelsExtendDto modelsExtendDto, String code);

    List<OrganizationDto> findListByRisk(OrganizationDto organizationDto);


}


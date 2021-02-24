package com.ideatech.ams.kyc.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.kyc.dto.ManagementDto;

public interface ManagementService {
    void insertBatch(Long saicInfoId, List<ManagementDto> managementList);

    JSONObject getManagersInfoBySaicInfoIdInLocal(Long saicInfoId);
}

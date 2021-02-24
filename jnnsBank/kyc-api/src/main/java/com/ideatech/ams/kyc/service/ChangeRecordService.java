package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dto.ChangeRecordDto;

import java.util.List;

public interface ChangeRecordService {
    void insertBatch(Long saicInfoId, List<ChangeRecordDto> changeRecordDtos);

    List<ChangeRecordDto> findBySaicInfoId(Long saicInfoId);
}

package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dto.ChangeMessDto;

import java.util.List;

public interface ChangeMessService {

    void insertBatch(Long saicInfoId, List<ChangeMessDto> changemess);

    List<ChangeMessDto> findBySaicInfoId(Long saicInfoId);

}

package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dto.IllegalDto;

import java.util.List;

public interface IllegalService {

    void insertBatch(Long saicInfoId, List<IllegalDto> illegals);

    List<IllegalDto> findBySaicInfoId(Long saicInfoId);
}

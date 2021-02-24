package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dto.SuperviseDto;

import java.util.List;

public interface SuperviseService {
    void insertBatch(Long saicInfoId, List<SuperviseDto> superviseList);
}

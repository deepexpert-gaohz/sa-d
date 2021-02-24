package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dto.ReportDto;

import java.util.List;

public interface ReportService {
    void insertBatch(Long saicInfoId, List<ReportDto> reportDtos);

    List<ReportDto> findBySaicInfoId(Long saicInfoId);
}

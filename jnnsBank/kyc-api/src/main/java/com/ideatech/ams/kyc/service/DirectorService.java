package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dto.DirectorDto;

import java.util.List;

public interface DirectorService {
    void insertBatch(Long saicInfoId, List<DirectorDto> directorList);
}

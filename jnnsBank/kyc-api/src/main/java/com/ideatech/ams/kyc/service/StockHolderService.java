package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dto.StockHolderDto;

import java.util.List;

public interface StockHolderService {

    void insertBatch(Long saicInfoId,List<StockHolderDto> stockHolderList);

    List<StockHolderDto> findBySaicInfoId(Long saicInfoId);

}


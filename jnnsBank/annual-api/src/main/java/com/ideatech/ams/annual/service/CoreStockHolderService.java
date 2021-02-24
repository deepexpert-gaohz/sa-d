package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.CoreStockHolderDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

public interface CoreStockHolderService extends BaseService<CoreStockHolderDto> {
    List<CoreStockHolderDto> findByBatchNo(String batchNo);
}

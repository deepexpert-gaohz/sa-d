package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.CoreBeneficiaryDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

public interface CoreBeneficiarySerivce extends BaseService<CoreBeneficiaryDto> {
    List<CoreBeneficiaryDto> findByBatchNo(String batchNo);
}

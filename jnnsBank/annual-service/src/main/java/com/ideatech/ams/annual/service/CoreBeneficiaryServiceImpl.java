package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.CoreBeneficiaryDao;
import com.ideatech.ams.annual.dto.CoreBeneficiaryDto;
import com.ideatech.ams.annual.entity.CoreBeneficiaryPo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoreBeneficiaryServiceImpl extends BaseServiceImpl<CoreBeneficiaryDao, CoreBeneficiaryPo, CoreBeneficiaryDto> implements CoreBeneficiarySerivce  {
    @Override
    public List<CoreBeneficiaryDto> findByBatchNo(String batchNo) {
        List<CoreBeneficiaryPo> byBatchNo = getBaseDao().findByBatchNo(batchNo);
        return ConverterService.convertToList(byBatchNo, CoreBeneficiaryDto.class);
    }

}

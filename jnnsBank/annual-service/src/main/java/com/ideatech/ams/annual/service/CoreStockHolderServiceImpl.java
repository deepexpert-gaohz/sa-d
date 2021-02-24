package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.CoreStockHolderDao;
import com.ideatech.ams.annual.dto.CoreStockHolderDto;
import com.ideatech.ams.annual.entity.CoreStockHolderPo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoreStockHolderServiceImpl extends BaseServiceImpl<CoreStockHolderDao, CoreStockHolderPo, CoreStockHolderDto> implements CoreStockHolderService {
    @Override
    public List<CoreStockHolderDto> findByBatchNo(String batchNo) {
        List<CoreStockHolderPo> byBatchNo = getBaseDao().findByBatchNo(batchNo);
        return ConverterService.convertToList(byBatchNo, CoreStockHolderDto.class);
    }
}

package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.TaxPaymentInformationDao;
import com.ideatech.ams.mivs.dto.trd.TaxPaymentInformationDto;
import com.ideatech.ams.mivs.entity.TaxPaymentInformation;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/30.
 */

@Service
public class TaxPaymentInformationServiceImpl extends BaseServiceImpl<TaxPaymentInformationDao, TaxPaymentInformation, TaxPaymentInformationDto> implements TaxPaymentInformationService {

    @Override
    public List<TaxPaymentInformationDto> findByTaxInformationLogId(Long id) {
        return ConverterService.convertToList(getBaseDao().findAllByTaxInformationLogId(id),TaxPaymentInformationDto.class);
    }
}

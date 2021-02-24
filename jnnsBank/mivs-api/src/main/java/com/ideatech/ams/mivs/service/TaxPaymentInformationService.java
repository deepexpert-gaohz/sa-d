package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.trd.TaxPaymentInformationDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/30.
 */
public interface TaxPaymentInformationService extends BaseService<TaxPaymentInformationDto> {

    List<TaxPaymentInformationDto> findByTaxInformationLogId(Long id);
}

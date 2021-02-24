package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dto.CompanyPartnerLogInfo;

import java.util.List;

/**
 * @author jzh
 * @date 2019/3/20.
 */
public interface CompanyPartnerLogService {
    void save(List<CompanyPartnerLogInfo> companyPartnerLogInfoList);

    List<CompanyPartnerLogInfo> getAllByCustomerPublicLogId(Long customerPublicLogId);
}

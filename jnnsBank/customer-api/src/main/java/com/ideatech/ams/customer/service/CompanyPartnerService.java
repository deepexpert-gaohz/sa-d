package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dto.CompanyPartnerInfo;

import java.util.List;

/**
 * @author wanghongjie
 *
 * ion 2018-06-20 11:22
 */
public interface CompanyPartnerService {

    void save(CompanyPartnerInfo CompanyPartnerInfo);
    
    CompanyPartnerInfo getOne(Long id);

    /**
     * 根据companyPartnerId获取多条数据
     * @param customerPublicId
     * @return
     */
    List<CompanyPartnerInfo> getAllByCustomerPublicId(Long customerPublicId);

    /**
     * 保存多条数据
     * @param companyPartnerInfoList
     * @return
     */
     void save(List<CompanyPartnerInfo> companyPartnerInfoList);
}

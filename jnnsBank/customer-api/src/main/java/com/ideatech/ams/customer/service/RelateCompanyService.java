package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dto.RelateCompanyInfo;

import java.util.List;

/**
 * @author wanghongjie
 *
 * @version 2018-06-20 11:22
 */
public interface RelateCompanyService {

    void save(RelateCompanyInfo relateCompanyInfo);

    RelateCompanyInfo getOne(Long id);

    void save(List<RelateCompanyInfo> infos);

    /**
     * 根据customerId获取关联企业信息
     * @param customerId
     * @return
     */
    List<RelateCompanyInfo> getAllByCustomerId(Long customerId);

    /**
     * 根据customerPublicId获取关联企业信息
     * @param customerPublicId
     * @return
     */
    List<RelateCompanyInfo> getAllByCustomerPublicId(Long customerPublicId);
}

package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dto.RelateCompanyMidInfo;

import java.util.List;

/**
 * @author van
 * @date 20:51 2018/5/29
 */
public interface RelateCompanyMidService {

    void deleteByMidId(Long midId);

    /**
     * 保存多个
     * @param infos
     */
    void save(List<RelateCompanyMidInfo> infos);

    /**
     * 根据customerId获取关联企业信息
     * @param customerId
     * @return
     */
    List<RelateCompanyMidInfo> getAllByCustomerId(Long customerId);

    /**
     * 根据customerPublicId获取关联企业信息
     * @param customerPublicMidId
     * @return
     */
    List<RelateCompanyMidInfo> getAllByCustomerPublicMidId(Long customerPublicMidId);

}

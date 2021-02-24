package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dto.CompanyPartnerMidInfo;

import java.util.List;

/**
 * @author jzh
 * @date 2019/3/20.
 */
public interface CompanyPartnerMidService {

    /**
     * 单个保存
     * @param companyPartnerMidInfo
     */
    void save(CompanyPartnerMidInfo companyPartnerMidInfo);

    /**
     * 多个保存
     * @param companyPartnerMidInfoList
     */
    void save(List<CompanyPartnerMidInfo> companyPartnerMidInfoList);

    /**
     * 根据id获取单条数据
     * @param id
     * @return
     */
    CompanyPartnerMidInfo getOne(Long id);

    /**
     * 根据customerPublicMidId获取多条数据
     * @param customerPublicMidId
     * @return
     */
    List<CompanyPartnerMidInfo> getAllByCustomerPublicMidId(Long customerPublicMidId);

    /**
     * 根据customerPublicMidId删除股东中间表信息
     * @param customerPublicMidId
     */
    void deleteByCustomerPublicMidId(Long customerPublicMidId);


    void deleteByMidId(Long midId);

}

package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dto.CustomerPublicMidInfo;

import java.util.List;

/**
 * @author vantoo
 * @date 15:48 2018/5/25
 */
public interface CustomerPublicMidService {

    CustomerPublicMidInfo getOne(Long id);

    void save(CustomerPublicMidInfo customerPublicMidInfo);

    CustomerPublicMidInfo getByCustomerNo(String customerNo);

    CustomerPublicMidInfo getByBillId(Long billId);

    void delete(Long id);

    List<CustomerPublicMidInfo> findByOrganFullIdLike(String organFullId);

    List<CustomerPublicMidInfo> findByCustomerId(Long customerId);

}

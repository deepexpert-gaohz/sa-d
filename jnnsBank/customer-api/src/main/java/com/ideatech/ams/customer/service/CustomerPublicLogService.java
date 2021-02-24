package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.common.dto.PagingDto;

import java.util.List;
import java.util.Map;

/**
 * @author vantoo
 * @date 15:48 2018/5/25
 */
public interface CustomerPublicLogService {

    CustomerPublicLogInfo getOne(Long id);

    void save(CustomerPublicLogInfo customerPublicLogInfo);

    CustomerPublicLogInfo getMaxSeq(Long customerId);

    CustomerPublicLogInfo getByCustomerNo(String customerNo);

    Map<Long,CustomerPublicLogInfo> findAllInMap();

    List<CustomerPublicLogInfo> getByCustomerId(Long customerId);

    List<CustomerPublicLogInfo> getByDepositorName(String depositorName);

    List<CustomerPublicLogInfo> findByOrganFullIdLike(String organFullId);
}

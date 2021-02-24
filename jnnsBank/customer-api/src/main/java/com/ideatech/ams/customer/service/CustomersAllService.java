package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dto.CustomerAllResponse;
import com.ideatech.ams.customer.dto.CustomersAllInfo;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author vantoo
 * @date 10:13 2018/5/25
 */
public interface CustomersAllService {

    void save(CustomersAllInfo customersAllInfo);

    void saveCustomerInfo(CustomerAllResponse info);

    void editCustomerInfo(CustomerAllResponse info);

    CustomersAllInfo findByCustomerNo(String customerNo);

    CustomersAllInfo findOne(Long id);
    
    CustomersAllInfo findByDepositorName(String depositorName);

    TableResultResponse<CustomerAllResponse> query(CustomerAllResponse info, Pageable pageable);

    CustomerAllResponse findById(Long customerId);

    CustomersAllInfo getInfo(String depositorName, String organFullId);

    Map<Long,CustomersAllInfo> findAllInMap();

    List<CustomersAllInfo> findByOrganFullIdLike(String organFullId);

    List<CustomersAllInfo> findByDepositorNameList(String depositorName);
}

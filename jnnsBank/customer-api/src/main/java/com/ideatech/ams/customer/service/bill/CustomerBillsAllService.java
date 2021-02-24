package com.ideatech.ams.customer.service.bill;

import com.ideatech.ams.customer.dto.bill.CustomerBillsAllInfo;

import java.util.List;

/**
 * @author vantoo
 * @date 9:55 2018/5/25
 */
public interface CustomerBillsAllService {

    /**
     * @param customerBillsAllInfo
     */
    void save(CustomerBillsAllInfo customerBillsAllInfo);

    /**
     * 根据客户找到未完成流水
     * @param customerId
     * @return
     */
    long countUnfinishedByCustomerId(Long customerId);

    long countUnfinishedByCustomerNo(String customerNo);

    List<CustomerBillsAllInfo> findByOrganFullIdLike(String organFullId);

    List<CustomerBillsAllInfo> findByCustomerId(Long customerId);

}

package com.ideatech.ams.customer.service.bill;

import com.ideatech.ams.customer.dto.bill.CustomerBillsAllInfo;

/**
 * 处理客户信息流水与客户的所有操作
 *
 * @author vantoo
 * @date 10:17 2018/5/25
 */
public interface CustomerBillsPublicService {

    /**
     * @param customerBillsAllInfo
     * @param userId
     */
    void save(CustomerBillsAllInfo customerBillsAllInfo, Long userId) throws Exception;

}

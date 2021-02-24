package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dto.CustomerAllResponse;

/**
 * @author liangding
 */
public interface CustomerNoGenerateService {
    /**
     * 根据客户详情，生成客户号，缺省实现返回空
     * @param customerAllResponse 客户详情
     * @return 客户号
     */
    String generate(CustomerAllResponse customerAllResponse);
}

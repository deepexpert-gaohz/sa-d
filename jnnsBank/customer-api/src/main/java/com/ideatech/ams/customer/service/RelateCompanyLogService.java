package com.ideatech.ams.customer.service;

import com.ideatech.ams.customer.dto.RelateCompanyLogInfo;

import java.util.List;

/**
 * @author van
 * @date 20:51 2018/5/29
 */
public interface RelateCompanyLogService {

    void deleteByMidId(Long midId);

    void save(List<RelateCompanyLogInfo> infos);

    /**
     * 根据customerId获取关联企业信息
     *
     * @param customerId
     * @return
     */
    List<RelateCompanyLogInfo> getAllByCustomerId(Long customerId);

    /**
     * 根据customerPublicId获取关联企业信息
     *
     * @param customerPublicLogId
     * @return
     */
    List<RelateCompanyLogInfo> getAllByCustomerPublicLogId(Long customerPublicLogId);

}

package com.ideatech.ams.customer.dao;

import com.ideatech.ams.customer.entity.RelateCompanyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author wanghongjie
 * @version 2018-06-20 11:22
 */
public interface RelateCompanyLogDao extends JpaRepository<RelateCompanyLog, Long>, JpaSpecificationExecutor<RelateCompanyLog> {

    List<RelateCompanyLog> findAllByCustomerPublicLogIdOrderByString001Asc(Long customerPublicLogId);

    List<RelateCompanyLog> findAllByCustomerIdOrderByString001Asc(Long customerId);

    void deleteAllByCustomerPublicLogId(Long customerPublicLogId);

}

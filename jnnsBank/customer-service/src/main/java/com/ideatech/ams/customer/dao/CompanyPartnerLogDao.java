package com.ideatech.ams.customer.dao;

import com.ideatech.ams.customer.entity.CompanyPartnerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jzh
 * @date 2019/3/20.
 */

public interface CompanyPartnerLogDao extends JpaRepository<CompanyPartnerLog, Long>, JpaSpecificationExecutor<CompanyPartnerLog> {
    List<CompanyPartnerLog> findAllByCustomerPublicLogId(Long customerPublicLogId);
}

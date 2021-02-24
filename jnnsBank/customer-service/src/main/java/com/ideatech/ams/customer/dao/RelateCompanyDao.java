package com.ideatech.ams.customer.dao;

import com.ideatech.ams.customer.entity.RelateCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author wanghongjie
 * @version 2018-06-20 11:22
 */
public interface RelateCompanyDao extends JpaRepository<RelateCompany, Long>, JpaSpecificationExecutor<RelateCompany> {

    List<RelateCompany> findAllByCustomerIdOrderByString001Asc(Long customerId);

    List<RelateCompany> findAllByCustomerPublicIdOrderByString001Asc(Long customerPublicId);

}

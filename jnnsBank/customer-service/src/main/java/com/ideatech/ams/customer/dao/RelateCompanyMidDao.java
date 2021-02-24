package com.ideatech.ams.customer.dao;

import com.ideatech.ams.customer.entity.RelateCompanyMid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author wanghongjie
 * @version 2018-06-20 11:22
 */
public interface RelateCompanyMidDao extends JpaRepository<RelateCompanyMid, Long>, JpaSpecificationExecutor<RelateCompanyMid> {

    List<RelateCompanyMid> findAllByCustomerPublicMidIdOrderByString001Asc(Long customerPublicMidId);

    List<RelateCompanyMid> findAllByCustomerIdOrderByString001Asc(Long customerId);

    void deleteAllByCustomerPublicMidId(Long customerPublicMidId);

}

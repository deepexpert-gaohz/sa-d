package com.ideatech.ams.customer.dao;

import com.ideatech.ams.customer.entity.CompanyPartnerMid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jzh
 * @date 2019/3/19.
 */
public interface CompanyPartnerMidDao extends JpaRepository<CompanyPartnerMid, Long>, JpaSpecificationExecutor<CompanyPartnerMid> {

    List<CompanyPartnerMid> findAllByCustomerPublicMidIdOrderByString001Asc(Long customerPublicMidId);

    void deleteAllByCustomerPublicMidId(Long customerPublicMidId);

    void deleteByCustomerPublicMidId(Long customerPublicMidId);
}

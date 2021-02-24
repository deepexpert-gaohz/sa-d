package com.ideatech.ams.customer.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideatech.ams.customer.entity.CompanyPartner;

import java.util.List;

/**
 * @author wanghongjie
 *
 * @version 2018-06-20 11:22
 */
public interface CompanyPartnerDao  extends JpaRepository<CompanyPartner, Long>, JpaSpecificationExecutor<CompanyPartner> {

    List<CompanyPartner> findAllByCustomerPublicId(Long customerPublicId);
}

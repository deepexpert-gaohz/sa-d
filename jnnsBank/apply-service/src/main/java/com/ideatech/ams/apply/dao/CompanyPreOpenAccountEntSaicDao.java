package com.ideatech.ams.apply.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideatech.ams.apply.entity.CompanyPreOpenAccountEntSaic;

public interface CompanyPreOpenAccountEntSaicDao extends JpaRepository<CompanyPreOpenAccountEntSaic, Long>, JpaSpecificationExecutor<CompanyPreOpenAccountEntSaic> {
	CompanyPreOpenAccountEntSaic findByCompanyPreId(Long preId);
}

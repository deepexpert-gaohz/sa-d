package com.ideatech.ams.kyc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideatech.ams.kyc.entity.Management;

public interface ManagementDao extends JpaRepository<Management, Long>, JpaSpecificationExecutor<Management> {
	Management findById(Long id);
    List<Management> findBySaicinfoId(Long saicinfoId);
}

package com.ideatech.ams.kyc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideatech.ams.kyc.entity.Beneficiary;

public interface BeneficiaryDao  extends JpaRepository<Beneficiary, Long>, JpaSpecificationExecutor<Beneficiary> {
    List<Beneficiary> findBySaicinfoId(Long saicInfoId);
    Beneficiary findById(Long id);
}

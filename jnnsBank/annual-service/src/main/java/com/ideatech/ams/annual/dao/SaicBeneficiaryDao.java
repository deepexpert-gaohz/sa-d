package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.SaicBeneficiaryPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SaicBeneficiaryDao extends JpaRepository<SaicBeneficiaryPo, Long>, JpaSpecificationExecutor<SaicBeneficiaryPo> {
}

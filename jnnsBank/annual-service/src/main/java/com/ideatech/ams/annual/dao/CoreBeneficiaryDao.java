package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.CoreBeneficiaryPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CoreBeneficiaryDao extends JpaRepository<CoreBeneficiaryPo, Long>, JpaSpecificationExecutor<CoreBeneficiaryPo> {
    List<CoreBeneficiaryPo> findByBatchNo(String batchNo);
}

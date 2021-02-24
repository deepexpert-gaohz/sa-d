package com.ideatech.ams.kyc.dao;

import com.ideatech.ams.kyc.entity.UltimateOwnerPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UltimateOwnerDao extends JpaRepository<UltimateOwnerPo, Long>, JpaSpecificationExecutor<UltimateOwnerPo> {
    UltimateOwnerPo findBySaicinfoId(Long saicinfoId);
}

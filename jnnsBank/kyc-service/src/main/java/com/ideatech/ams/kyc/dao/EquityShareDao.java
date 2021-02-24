package com.ideatech.ams.kyc.dao;

import com.ideatech.ams.kyc.entity.EquityShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EquityShareDao extends JpaRepository<EquityShare, Long>, JpaSpecificationExecutor<EquityShare> {
    List<EquityShare> findBySaicinfoId(Long saicInfoId);
}

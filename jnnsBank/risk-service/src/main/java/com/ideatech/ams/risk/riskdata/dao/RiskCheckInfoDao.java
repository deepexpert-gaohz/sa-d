package com.ideatech.ams.risk.riskdata.dao;

import com.ideatech.ams.risk.riskdata.entity.RiskCheckInfo;
import com.ideatech.ams.risk.riskdata.entity.RiskRecordInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RiskCheckInfoDao extends JpaRepository<RiskCheckInfo, Long>, JpaSpecificationExecutor<RiskCheckInfo> {

    RiskCheckInfo findOne(Long id);

    @Transactional
    @Query(value = "delete from yd_risk_check_info  where  rownum<=?1 ", nativeQuery = true)
    @Modifying
    void deleteByCount(int i);

}

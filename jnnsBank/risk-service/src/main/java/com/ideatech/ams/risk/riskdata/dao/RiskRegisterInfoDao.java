package com.ideatech.ams.risk.riskdata.dao;

import com.ideatech.ams.risk.riskdata.entity.RiskRegisterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RiskRegisterInfoDao extends JpaRepository<RiskRegisterInfo,Long>, JpaSpecificationExecutor<RiskRegisterInfo> {
    List<RiskRegisterInfo> findAllByAccountNoAndStatusAndCorporateBank(String accountNo, String status, String corporateBank);
}

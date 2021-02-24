package com.ideatech.ams.risk.highRisk.dao;

import com.ideatech.ams.risk.highRisk.entity.HighRiskData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface HighRiskDataDao extends JpaRepository<HighRiskData,Long>, JpaSpecificationExecutor<HighRiskData> {

    List<HighRiskData> findHighRiskDataByDepositorcardNoAndCorporateBank(String depositorcardNo, String code);

    List<HighRiskData> findHighRiskDataByCustomerNoAndCorporateBank(String customerNo, String code);

    List<HighRiskData> findHighRiskDataByAccountNoAndCorporateBank(String accountNo, String code);

    List<HighRiskData> findHighRiskDataByAccountNoIsNullAndCorporateBankAndCustomerNo(String code, String customerNo);

    List<HighRiskData> findHighRiskDataByRiskDate(String date);

    void deleteByRiskDateAndCorporateBank(String date, String code);

    void deleteAllByCorporateBank(String code);

    HighRiskData findById(Long id);
}

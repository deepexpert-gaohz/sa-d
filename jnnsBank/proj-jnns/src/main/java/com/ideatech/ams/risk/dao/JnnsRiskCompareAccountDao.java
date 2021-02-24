package com.ideatech.ams.risk.dao;


import com.ideatech.ams.risk.domain.JnnsRiskCompareAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface JnnsRiskCompareAccountDao extends JpaRepository<JnnsRiskCompareAccount,Long> {

    List<JnnsRiskCompareAccount> findByAcctNo(String acctNo);

    JnnsRiskCompareAccount findByAcctNoAndRiskId(String acctNo, String riskId);

    @Transactional
    @Query(value = "delete from yd_jnns_risk_compare_account  where  rownum<=?1 ", nativeQuery = true)
    @Modifying
    void deleteByCount(int i);
}

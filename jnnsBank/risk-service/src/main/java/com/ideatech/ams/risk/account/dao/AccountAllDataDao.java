package com.ideatech.ams.risk.account.dao;

import com.ideatech.ams.risk.account.entity.AccountAllData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AccountAllDataDao extends JpaRepository<AccountAllData, Long>, JpaSpecificationExecutor<AccountAllData> {

    List<AccountAllData> findByCustomerNo(String customerNo);

    AccountAllData findByAcctNo(String acctNo);
    @Transactional
    @Query(value = "delete from YD_account_all_data  where  rownum<=?1 ", nativeQuery = true)
    @Modifying
    void deleteByCount(int i);
}

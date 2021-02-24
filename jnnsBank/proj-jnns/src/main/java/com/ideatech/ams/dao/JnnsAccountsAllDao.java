package com.ideatech.ams.dao;

import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface JnnsAccountsAllDao extends JpaRepository<AccountsAll, Long>, JpaSpecificationExecutor<AccountsAll> {

    @Query(value = "select t.yd_acct_name from yd_accounts_all t where t.yd_string003='1' group by t.yd_acct_name having count(*)>1" ,nativeQuery = true)
    List<String> selectCoreData();


    List<AccountsAll> findAllByAcctNameAndString003(String acctName,String string003);

    List<AccountsAll> findAllByAcctNoAndString003(String acctNo,String string003);

    @Transactional
    void deleteByAcctNo(String acctNo);
}

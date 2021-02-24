package com.ideatech.ams.dao;


import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.account.enums.bill.BillType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountsBillsAllDao extends JpaRepository<AccountBillsAll, Long>, JpaSpecificationExecutor<AccountBillsAll> {

    AccountBillsAll findById(long L);

    AccountBillsAll findTopByAcctNoAndBillTypeOrderByLastUpdateDateDesc(String acctNo, BillType billType);

    List<AccountBillsAll>  findByOrganFullId(String organFullId);

}

package com.ideatech.ams.dao;

import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.domain.Jhh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JhhDao extends JpaRepository<Jhh, Long>, JpaSpecificationExecutor<Jhh> {
    Jhh findTopByAcctNo(String acctNo);


}

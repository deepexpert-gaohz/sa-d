package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.entity.AccountChangeSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountChangeSummaryDao extends JpaRepository<AccountChangeSummary, Long>, JpaSpecificationExecutor<AccountChangeSummary> {


    AccountChangeSummary findByRefBillId(Long refBillId);
}

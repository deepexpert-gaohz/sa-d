package com.ideatech.ams.account.dao.bill;

import com.ideatech.ams.account.entity.bill.BillOperateLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BillOperateLockDao extends JpaRepository<BillOperateLock, Long>, JpaSpecificationExecutor<BillOperateLock> {
    BillOperateLock findByBillId(Long billId);

    @Modifying
    @Transactional
    @Query(value = "delete from BillOperateLock where billId =?1")
    void deleteByBillId(Long billId);
}

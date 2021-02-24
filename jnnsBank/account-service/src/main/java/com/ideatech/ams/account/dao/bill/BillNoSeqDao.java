package com.ideatech.ams.account.dao.bill;

import com.ideatech.ams.account.entity.bill.BillNoSeq;
import com.ideatech.ams.account.enums.bill.BillTypeNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;

/**
 * @author van
 * @date 21:22 2018/5/30
 */
public interface BillNoSeqDao extends JpaRepository<BillNoSeq, Long>, JpaSpecificationExecutor<BillNoSeq> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    BillNoSeq findByTypeAndDateAndOrgCode(BillTypeNo type, String date, String orgCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    BillNoSeq findByTypeAndDate(BillTypeNo type, String date);
}

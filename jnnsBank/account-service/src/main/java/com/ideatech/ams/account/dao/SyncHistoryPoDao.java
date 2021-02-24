package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.entity.SyncHistoryPo;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncHistoryPoDao extends JpaRepository<SyncHistoryPo, Long>, JpaSpecificationExecutor<SyncHistoryPo> {
    SyncHistoryPo findFirstByRefBillIdAndSyncStatusAndSyncTypeOrderBySyncDateTimeDesc(Long refBillId, CompanySyncStatus syncStatus,
                                                                                      EAccountType syncType);

}

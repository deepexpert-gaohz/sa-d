package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.dto.AccountPublicLogInfo;
import com.ideatech.ams.account.entity.AccountPublicLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author vantoo
 * @date 16:14 2018/5/28
 */
@Repository
public interface AccountPublicLogDao extends JpaRepository<AccountPublicLog, Long>, JpaSpecificationExecutor<AccountPublicLog> {

    @Query("from AccountPublicLog where accountId=?1 and sequence=(select max(sequence) from AccountPublicLog t where t.accountId=?1)")
    AccountPublicLog findByAccountIdAndMaxSequence(Long accountId);

    int deleteByAccountIdAndRefBillId(Long accountId, Long refBillId);
    
    List<AccountPublicLog> findByAccountId(Long accountId);

    List<AccountPublicLog> findByRefBillId(Long refBillId);

    List<AccountPublicLog> findByRefBillIdOrderBySequenceDesc(Long refBillId);

    List<AccountPublicLog> findByOrganFullIdLike(String organFullId);

    List<AccountPublicLog> findByCustomerNo(String customerId);
}

package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.entity.AccountPublic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author vantoo
 * @date 16:14 2018/5/28
 */
@Repository
public interface AccountPublicDao extends JpaRepository<AccountPublic, Long>, JpaSpecificationExecutor<AccountPublic> {

    AccountPublic findByAccountId(Long accountId);

    List<AccountPublic> findByOrganFullIdLike(String organFullId);

    void deleteByAccountId(Long accountId);

    Long countByOperatorIdcardDueGreaterThanEqualAndOperatorIdcardDueLessThanAndOrganFullIdLike(
            String nowDateStr, String before, String organFullId);

    Page<AccountPublic> findByOperatorIdcardDueGreaterThanEqualAndOperatorIdcardDueLessThanAndOrganFullIdLike(
            String nowDateStr, String before, String organFullId, Pageable pageable);

    @Query("update AccountPublic b set b.accountLicenseNo=?2 where b.accountId=?1")
    @Modifying
    @Transactional
    int updateCancelHezhun(Long accountId,String accountLicenseNo);

    @Query("update AccountPublic b set b.checkStatus='已核实' where b.id=?1")
    @Modifying
    @Transactional
    void updateCheckStatus1(Long id);



}

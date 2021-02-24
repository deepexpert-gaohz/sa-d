package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.FetchPbcInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FetchPbcDao  extends JpaRepository<FetchPbcInfo, Long>, JpaSpecificationExecutor<FetchPbcInfo> {
    @Modifying
    @Query("delete from FetchPbcInfo acct where acct.acctNo = ?1")
    @Transactional
    void deleteByAcctNo(String acctNo);

    List<FetchPbcInfo> findByBankCode(String bankCode);

    List<FetchPbcInfo> findByOrganFullId(String organFullId);

    @Query("select new FetchPbcInfo(a.depositorName,a.fileNo,a.acctCreateDate,a.accountStatus,a.acctNo) from FetchPbcInfo a where a.accountStatus = 'normal' ")
    List<FetchPbcInfo> findAcctNameAndFileNo();

	List<FetchPbcInfo> findByAnnualTaskId(Long taskId);

	List<FetchPbcInfo> findByCollectAccountId(Long collectAccountId);

    @Modifying
    @Query("delete from FetchPbcInfo where annualTaskId = ?1")
	void deleteByAnnualTaskId(Long taskId);
}

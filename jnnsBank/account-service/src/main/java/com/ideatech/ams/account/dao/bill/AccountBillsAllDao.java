package com.ideatech.ams.account.dao.bill;

import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillFromSource;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.enums.bill.CompanyAmsCheckStatus;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.common.enums.CompanyIfType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author vantoo
 * @date 11:01 2018/5/25
 */
public interface AccountBillsAllDao extends JpaRepository<AccountBillsAll, Long>, JpaSpecificationExecutor<AccountBillsAll> {

    List<AccountBillsAll> findByFinalStatusAndAcctNo(CompanyIfType finalStatus, String acctNo);

    List<AccountBillsAll> findByFinalStatusAndAccountId(CompanyIfType finalStatus, Long accountId);

    AccountBillsAll findTopByFinalStatusAndAcctNoAndBillTypeOrderByCreatedDateDesc(CompanyIfType finalStatus, String acctNo, BillType billType);

    AccountBillsAll findTopByFinalStatusAndAcctNoOrderByCreatedDateDesc(CompanyIfType finalStatus, String acctNo);

    long countByFinalStatusAndAcctNo(CompanyIfType finalStatus, String acctNo);

    long countByFinalStatusAndAccountId(CompanyIfType finalStatus, Long accountId);

    long countByBillTypeAndOrganFullIdStartsWithAndBillDateBetween(BillType billType, String organFullId, String createddatestart, String createddateend);

    @Query("update AccountBillsAll b set b.finalStatus='Yes' where b.id=?1")
    @Modifying
    @Transactional
    int updateFinalStatus(Long id);

    AccountBillsAll findByCustomerNo(String customerNo);

    AccountBillsAll findByBillNo(String billNo);

    long countByBillTypeAndOrganFullIdStartsWith(BillType billType, String organFullId);

    long countById(Long id);

    @Query("update AccountBillsAll b set b.customerLogId=?2 where b.id=?1")
    @Modifying
    @Transactional
    int updateCustomerLogId(Long id,Long customerLogId);

    @Query("update AccountBillsAll b set b.selectPwd=?2,b.openKey=?3,b.accountKey=?4 where b.id=?1")
    @Modifying
    @Transactional
    int updateCancelHezhun(Long id,String selectPwd,String openKey,String accountKey);

    AccountBillsAll findFirstByAcctNoOrderByCreatedDateDesc(String acctNo);

    AccountBillsAll findFirstByAccountIdOrderByCreatedDateDesc(Long accountId);

    AccountBillsAll findFirstByAccountIdOrderByCreatedDate(Long accountId);

    AccountBillsAll findFirstByCustomerLogIdOrderByCreatedDate(Long customerLogId);

    List<AccountBillsAll> findByPreOpenAcctId(String preOpenAcctId);

    List<AccountBillsAll> findByPreOpenAcctIdAndDepositorName(String preOpenAcctId,String depositorName);

    List<AccountBillsAll> findByAcctNoOrderByLastUpdateDate(String acctNo, Pageable pageable);

    List<AccountBillsAll> findByAcctNo(String acctNo);

    long countByAcctNo(String acctNo);

    List<AccountBillsAll> findByOrganFullIdLike(String organFullId);

    List<AccountBillsAll> findByAccountId(Long accountId);

    List<AccountBillsAll> findByAccountIdIn(List<Long> accountIds);

    List<AccountBillsAll> findByIdIn(List<Long> ids);

    List<AccountBillsAll> findByCustomerLogIdIn(List<Long> customerLogId);

    /**
     * 获取对应机构的存量数据
     * @param billType
     * @param fromSource
     * @param string005
     * @param organFullId
     * @return
     */
    List<AccountBillsAll> findAllByBillTypeAndFromSourceAndString005AndOrganFullId(BillType billType, BillFromSource fromSource,String string005, String organFullId);

    Long countByBillNoAndAcctNo(String billNo, String acctNo);

    Long countByBillNoAndCustomerNo(String billNo, String customerNo);
	
	  List<AccountBillsAll> findByAcctNoLike(String acctNo);

    @Query("update AccountBillsAll b set b.downloadstatus='已下载' where b.id=?1")
    @Modifying
    @Transactional
    void updateDownLoadStatus(Long id);

    @Query("update AccountBillsAll b set b.uploadstatus='已上传' where b.id=?1")
    @Modifying
    @Transactional
    void updateUploadstatus(Long id);



    AccountBillsAll findById(Long id);


    @Query("select a from AccountBillsAll a where a.acctType  ='jiben' and a.organFullId  = ?1 and a.billDate=?2 order by a.createdDate")
    @Transactional
    List<AccountBillsAll> findByOrganFullIdAndBillDate(String organFullId,String billDate);

    @Query("select a from AccountBillsAll a where a.acctNo  =?1 and a.billType=?2")
    @Transactional
    AccountBillsAll findByAcctNoAndBillType(String acctNo,BillType open);

    List<AccountBillsAll> findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(String organFullId, List<CompanyAcctType> acctTypes, List<String> depositorTypes, CompanySyncStatus pbcSyncStatus, CompanyAmsCheckStatus companyAmsCheckStatus, List<BillType> billTypes);





}

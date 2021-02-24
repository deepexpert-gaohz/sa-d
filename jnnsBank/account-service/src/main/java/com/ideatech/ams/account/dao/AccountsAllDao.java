package com.ideatech.ams.account.dao;

import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author vantoo
 * @date 15:53 2018/5/28
 */
@Repository
public interface AccountsAllDao extends JpaRepository<AccountsAll, Long>, JpaSpecificationExecutor<AccountsAll> {

    @Query("select count(a) from AccountsAll a where a.createdDate like CONCAT('%',?1,'%') and a.bankCode = ?2")
    int findByCreatedDateLikeAndBankCode(String date,String code);

    AccountsAll findByRefBillId(Long refBillId);

    AccountsAll findByAcctNo(String acctNo);

    // List<AccountsAll> findByAccountKey(String accountKey);
    List<AccountsAll> findByAccountKeyAndAcctType(String accountKey,CompanyAcctType acctType);

    List<AccountsAll> findByCustomerLogId(Long customerLogId);

    long countByCustomerLogId(Long customerLogId);

    AccountsAll findByAcctNoAndIdNot(String acctNo,Long id);

    List<AccountsAll> findByOrganFullId(String organFullId);

    @Query("select distinct a.acctName  from AccountsAll a where a.organFullId =?1 and a.accountStatus = ?2")
    List<String> findByOrganFullIdAndAccountStatus(String organFullId,AccountStatus accountStatus);

    @Query("select distinct a.acctName  from AccountsAll a where  a.accountStatus = ?1")
    List<String> findAllByAccountStatus(AccountStatus accountStatus);
    long countByAcctNo(String acctNo);

    List<AccountsAll> findByAccountStatus(AccountStatus accountStatus);

    List<AccountsAll> findByAccountStatusAndAcctCreateDateLessThanEqual(AccountStatus accountStatus, String date);

    @Query("select acctNo from AccountsAll where acctNo is not null")
    Set<String> findAcctNoAllInSet();

    Long countByAcctTypeInAndEffectiveDateLessThanAndOrganFullIdLike(List<CompanyAcctType> acctType, String before, String organFullId);

    Page<AccountsAll> findByAcctTypeInAndEffectiveDateLessThanAndOrganFullIdLike(List<CompanyAcctType> acctType, String before, String organFullId, Pageable pageable);

    Long countByString003AndOrganFullIdLike(String string003, String organFullId);

    List<AccountsAll> findByOrganFullIdLike(String organFullId);

    List<AccountsAll> findByCustomerNo(String customerNo);

    AccountsAll findFirstByCustomerNoOrderByCreatedDate(String customerNo);

    AccountsAll findFirstByCustomerLogIdOrderByCreatedDate(Long customerLogId);

    @Query("update AccountsAll b set b.selectPwd=?2 , b.openKey=?3 , b.accountKey=?4 where b.id=?1")
    @Modifying
    @Transactional
    int updateCancelHezhun(Long id,String selectPwd,String openKey,String accountKey);

    @Query("select a from AccountsAll a where a.createdDate like '2019%' and a.accountStatus='normal' ORDER BY a.bankCode,a.createdDate desc")
    List<AccountsAll> openList();

    @Query("select a from AccountsAll a where a.cancelDate like '2019%' and a.accountStatus='revoke' ORDER BY a.bankCode,a.cancelDate desc")
    List<AccountsAll> cancelList();


    List<AccountsAll> findByAccountStatusAndOrganFullIdStartsWith(AccountStatus accountStatus, String organFullId);

    List<AccountsAll> findByAccountStatusAndAcctCreateDateLessThanEqualAndOrganFullIdStartsWith(AccountStatus accountStatus, String date, String organFullId);

    @Query("update AccountsAll b set b.acctCreateDate=?2 where b.refBillId=?1")
    @Modifying
    @Transactional
    int updateAcctCreateDate(Long id,String date);

    @Query("select acctNo from AccountsAll where acctNo is not null and accountStatus=?1")
    Set<String> findAcctNoFromAccountStatus(AccountStatus accountStatus);

    List<AccountsAll> findAllByAcctTypeInAndOrganFullIdLike(List<CompanyAcctType> acctType,String organFullId);

    List<AccountsAll> findByCustomerNoAndAccountStatus(String customerNo,AccountStatus status);

    List<AccountsAll> findByBankCodeAndAccountStatus(String bankCode,AccountStatus status);

    @Query("update AccountsAll b set b.ckeckStatus='已核实' where b.id=?1")
    @Modifying
    @Transactional
    void updateckeckStatus(Long id);

    @Query("update AccountsAll b set b.isEffectiveDateOver=?2 where b.id=?1")
    @Modifying
    @Transactional
    void updateIsEffectiveDateOver(Long id, Boolean boolea);



    List<AccountsAll> findByAcctName(String acctNmae);

    @Query("update AccountsAll b set b.effectiveDate=?2 where b.id=?1")
    @Modifying
    @Transactional
    void updateTempAcct(Long id, String effectiveDate);




}

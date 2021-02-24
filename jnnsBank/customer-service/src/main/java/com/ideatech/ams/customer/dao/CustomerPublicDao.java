package com.ideatech.ams.customer.dao;

import com.ideatech.ams.customer.entity.CustomerPublic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author vantoo
 * @date 10:23 2018/5/25
 */
public interface CustomerPublicDao extends JpaRepository<CustomerPublic, Long>, JpaSpecificationExecutor<CustomerPublic> {

    CustomerPublic findByCustomerId(Long customerId);

    CustomerPublic findByDepositorName(String depositorName);

    List<CustomerPublic> findByOrganFullId(String organFullId);


    @Query("update CustomerPublic b set b.checkStatus='已核实' where b.id=?1")
    @Modifying
    @Transactional
    void updateCheckStatus1(Long id);

    @Query("update CustomerPublic b set b.isLegalIdcardDueOver=?2 where b.id=?1")
    @Modifying
    @Transactional
    void update_is_legal_idcard_due_over(Long id,Boolean booleab);

    @Query("update CustomerPublic b set b.isFileDueOver=?2 where b.id=?1")
    @Modifying
    @Transactional
    void update_is_file_due_over(Long id,Boolean booleab);

    @Query("update CustomerPublic b set b.legalIdcardDue=?2 ,b.fileDue=?3  where b.id=?1")
    @Modifying
    @Transactional
    void updateLegalIdcardDue(Long id,String legalIdcardDue,String fileDue);



}

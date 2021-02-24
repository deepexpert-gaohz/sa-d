package com.ideatech.ams.readData.dao;


import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.readData.AlteritemMointorInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface JnnsAlteritemDao extends JpaRepository<AlteritemMointorInfo, Long>, JpaSpecificationExecutor<AlteritemMointorInfo> {


   /* @Transactional
    TableResultResponse<AlteritemMointor> findAll();*/

    //AccountBillsAll findByCustomerNo(String );
    @Query(value = "select yd_organ_full_id from yd_account_bills_all a  where  a.yd_customer_no = ?1",nativeQuery = true)
    @Transactional
    String findByCustomerId(String customerId);



    @Query(value ="update YD_ALTERITEM_MOINTOR am set am.yd_organ_full_id = ?2,am.yd_code = ?3 where am.yd_customer_id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void update(String customerId, String orgFullId, String code);
}

package com.ideatech.ams.dao;

import com.ideatech.ams.domain.JnnsImageAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JnnsGetAcctNoDao extends JpaRepository<JnnsImageAll, Long>, JpaSpecificationExecutor<JnnsImageAll> {

   /* List<JnnsImageAll> findByAcctNo(String acctNo);*/


    /*@Query("select a from ZhjnCustomerInfo a where a.checkNo = ?1 and a.customerName like %?2% and a.customerStatus in (1,2,3) order by a.checkTime desc,a.clerkTime desc")
    */
    @Query(value = "select yd_image_code from yd_jnns_image_all  where yd_acct_no = ?1",nativeQuery = true)
    @Transactional
    List<String> findCCodeByAcctNo(String acctNo);










}

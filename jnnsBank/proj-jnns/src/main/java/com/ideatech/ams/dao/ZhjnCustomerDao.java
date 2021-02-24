package com.ideatech.ams.dao;

import com.ideatech.ams.domain.zhjn.ZhjnCustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ZhjnCustomerDao extends JpaRepository<ZhjnCustomerInfo, Long>, JpaSpecificationExecutor<ZhjnCustomerInfo> {

    @Transactional
    ZhjnCustomerInfo findZhjnCustomerInfoByOrderId(String orderId);


    @Transactional
    List<ZhjnCustomerInfo> findZhjnCustomerInfosByClerkNoOrderByClerkTimeDesc(String clerkNo);


    @Query("select a from ZhjnCustomerInfo a where a.clerkNo = ?1 and a.customerName like %?2% order by a.clerkTime desc")
    @Transactional
    List<ZhjnCustomerInfo> findCustomerByClerkAndName(String clerkNo, String customerName);


    @Query("select a from ZhjnCustomerInfo a where a.checkNo = ?1 and a.customerStatus in (1,2,3) order by a.checkTime desc,a.clerkTime desc")
    @Transactional
    List<ZhjnCustomerInfo> findCustomerByCheck(String checkNo);


    @Query("select a from ZhjnCustomerInfo a where a.checkNo = ?1 and a.customerName like %?2% and a.customerStatus in (1,2,3) order by a.checkTime desc,a.clerkTime desc")
    @Transactional
    List<ZhjnCustomerInfo> findCustomerByCheckAndName(String checkNo, String customerName);


}

package com.ideatech.ams.customer.dao;

import com.ideatech.ams.customer.entity.CustomerPublicLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author vantoo
 * @date 10:23 2018/5/25
 */
public interface CustomerPublicLogDao extends JpaRepository<CustomerPublicLog, Long>, JpaSpecificationExecutor<CustomerPublicLog> {

//    @Query("from CustomerPublicLog custLog where custLog.customerId=?1 and sequence=(select max(sequence) from CustomerPublicLog t where t.customerId=?1)")
    CustomerPublicLog findTop1ByCustomerIdOrderBySequenceDesc(Long customerId);

    CustomerPublicLog findByCustomerNo(String customerNo);

    List<CustomerPublicLog> findByCustomerId(Long customerId);

    List<CustomerPublicLog> findByOrganFullIdLike(String organFullId);

    List<CustomerPublicLog> findByDepositorName(String depositorName);

    List<CustomerPublicLog> findByFileNo(String cid);

}

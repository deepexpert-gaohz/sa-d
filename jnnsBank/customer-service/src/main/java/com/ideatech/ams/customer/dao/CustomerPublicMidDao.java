package com.ideatech.ams.customer.dao;

import com.ideatech.ams.customer.entity.CustomerPublicMid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author vantoo
 * @date 10:23 2018/5/25
 */
public interface CustomerPublicMidDao extends JpaRepository<CustomerPublicMid, Long>, JpaSpecificationExecutor<CustomerPublicMid> {

    CustomerPublicMid findByCustomerNo(String customerNo);

    CustomerPublicMid findFirstByRefBillIdOrderByIdDesc(Long billId);

    List<CustomerPublicMid> findByOrganFullIdLike(String organFullId);

    List<CustomerPublicMid> findByCustomerId(Long customerId);
}

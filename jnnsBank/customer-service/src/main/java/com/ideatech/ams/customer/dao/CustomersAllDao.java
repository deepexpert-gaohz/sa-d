package com.ideatech.ams.customer.dao;

import com.ideatech.ams.customer.entity.CustomersAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author vantoo
 * @date 10:23 2018/5/25
 */
public interface CustomersAllDao extends JpaRepository<CustomersAll, Long>, JpaSpecificationExecutor<CustomersAll> {

    CustomersAll findByCustomerNo(String customerNo);

    CustomersAll findByDepositorNameAndOrganFullId(String depositorName, String organFullId);

    List<CustomersAll> findByOrganFullIdLike(String organFullId);

    List<CustomersAll> findByDepositorName(String depositorName);



}

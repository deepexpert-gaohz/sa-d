package com.ideatech.ams.customer.dao.bill;

import com.ideatech.ams.customer.entity.bill.CustomerBillsAll;
import com.ideatech.common.enums.CompanyIfType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author vantoo
 * @date 11:01 2018/5/25
 */
public interface CustomerBillsAllDao extends JpaRepository<CustomerBillsAll, Long>, JpaSpecificationExecutor<CustomerBillsAll> {

    List<CustomerBillsAll> findByFinalStatusAndCustomerLogId(CompanyIfType finalStatus, Long customerLogId);

    long countByFinalStatusAndCustomerLogId(CompanyIfType finalStatus, Long customerLogId);

    long countByFinalStatusAndCustomerNo(CompanyIfType finalStatus, String customerNo);

    List<CustomerBillsAll> findByOrganFullIdLike(String organFullId);

    List<CustomerBillsAll> findByCustomerLogId(Long customerLogId);
}

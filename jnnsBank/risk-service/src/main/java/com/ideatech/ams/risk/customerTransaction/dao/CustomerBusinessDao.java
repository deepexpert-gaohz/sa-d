package com.ideatech.ams.risk.customerTransaction.dao;

import com.ideatech.ams.risk.customerTransaction.entity.CustomerBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CustomerBusinessDao extends JpaRepository<CustomerBusiness,Long>, JpaSpecificationExecutor<CustomerBusiness> {
    CustomerBusiness findByName(String name);
    List<CustomerBusiness> findByOrganFullId(String orgfullId);
    CustomerBusiness findByOrgCode(String orgCode);
}

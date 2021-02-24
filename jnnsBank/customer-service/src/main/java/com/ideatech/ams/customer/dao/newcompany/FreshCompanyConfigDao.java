package com.ideatech.ams.customer.dao.newcompany;

import com.ideatech.ams.customer.entity.newcompany.FreshCompanyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FreshCompanyConfigDao extends JpaRepository<FreshCompanyConfig, Long>, JpaSpecificationExecutor<FreshCompanyConfig> {

}

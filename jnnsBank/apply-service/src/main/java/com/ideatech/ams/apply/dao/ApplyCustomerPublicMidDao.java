package com.ideatech.ams.apply.dao;

import com.ideatech.ams.apply.entity.ApplyCustomerPublicMid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApplyCustomerPublicMidDao extends JpaRepository<ApplyCustomerPublicMid, Long>, JpaSpecificationExecutor<ApplyCustomerPublicMid> {
}

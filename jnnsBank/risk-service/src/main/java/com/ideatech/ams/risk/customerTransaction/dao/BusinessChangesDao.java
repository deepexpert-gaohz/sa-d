package com.ideatech.ams.risk.customerTransaction.dao;

import com.ideatech.ams.risk.customerTransaction.entity.BusinessChanges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BusinessChangesDao extends JpaRepository<BusinessChanges,Long>, JpaSpecificationExecutor<BusinessChanges> {
    List<BusinessChanges> findByName(String name);
    void deleteByCorporateFullId(String orgFullId);
}

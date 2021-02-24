package com.ideatech.ams.image.dao;

import com.ideatech.ams.image.entity.ImageAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ImageAccountDao extends JpaRepository<ImageAccount,Long>, JpaSpecificationExecutor<ImageAccount> {

    List<ImageAccount> findByDocCodeAndAcctId(String docCode,Long acctId);
    List<ImageAccount> findByAcctId(Long acctId);
}

package com.ideatech.ams.kyc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideatech.ams.kyc.entity.BaseAccount;

public interface BaseAccountDao extends JpaRepository<BaseAccount, Long>, JpaSpecificationExecutor<BaseAccount> {
    List<BaseAccount> findBySaicinfoId(Long saicInfoId);
}

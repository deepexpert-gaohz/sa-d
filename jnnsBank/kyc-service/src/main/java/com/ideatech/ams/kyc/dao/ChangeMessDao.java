package com.ideatech.ams.kyc.dao;

import com.ideatech.ams.kyc.entity.ChangeMess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ChangeMessDao extends JpaRepository<ChangeMess, Long>, JpaSpecificationExecutor<ChangeMess> {

    List<ChangeMess> findBySaicinfoId(Long saicInfoId);

}

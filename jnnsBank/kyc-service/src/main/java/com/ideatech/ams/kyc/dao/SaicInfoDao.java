package com.ideatech.ams.kyc.dao;

import com.ideatech.ams.kyc.entity.SaicInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaicInfoDao extends JpaRepository<SaicInfo, Long>, JpaSpecificationExecutor<SaicInfo> {

    @Query("from SaicInfo where name = ?1 or registno =?1 or unitycreditcode = ?1 order by createdDate desc ")
    List<SaicInfo> findSaicInfoByKeyword(String keyword);

    SaicInfo findById(Long id);

    long countByName(String name);

    long countByUnitycreditcode(String name);

    long countByRegistno(String name);

    SaicInfo findByName(String name);

    List<SaicInfo> findAllByName(String name);

    int deleteByName(String name);
}

package com.ideatech.ams.apply.dao;

import com.ideatech.ams.apply.entity.CompanyPreOpenAccountEnt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface CompanyPreOpenAccountEntDao extends JpaRepository<CompanyPreOpenAccountEnt, Long>, JpaSpecificationExecutor<CompanyPreOpenAccountEnt> {

    CompanyPreOpenAccountEnt findByApplyid(String applyId);
    
    CompanyPreOpenAccountEnt findById(Long id);

    @Query("SELECT MAX(cpoae.id) FROM CompanyPreOpenAccountEnt cpoae")
    String getMaxId();

    List<CompanyPreOpenAccountEnt> findByStatusAndAcceptTimesLessThan(String status, String endDate);

    List<CompanyPreOpenAccountEnt> findTop10ByHasocr(String hasocr);

    Long countByStatusAndCreatedDateLessThanAndOrganfullidLike(String status, Date dataBefore, String organFullId);

    Page<CompanyPreOpenAccountEnt> findByStatusAndCreatedDateLessThanAndOrganfullidLike(String status, Date dataBefore, String organFullId, Pageable pageable);

    CompanyPreOpenAccountEnt findByApplyidAndName(String applyid, String name);

}

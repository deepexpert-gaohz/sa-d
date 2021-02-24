package com.ideatech.ams.risk.model.dao;



import com.ideatech.ams.risk.model.entity.YdRiskTjjg;
import com.ideatech.ams.risk.model.dto.ModelSearchExtendDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ModelCountFileDao extends JpaRepository<YdRiskTjjg,Long>, JpaSpecificationExecutor<YdRiskTjjg> {



}

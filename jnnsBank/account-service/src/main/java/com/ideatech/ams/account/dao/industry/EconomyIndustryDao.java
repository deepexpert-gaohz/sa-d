package com.ideatech.ams.account.dao.industry;

import com.ideatech.ams.account.entity.industry.EconomyIndustry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EconomyIndustryDao extends JpaRepository<EconomyIndustry, Long>, JpaSpecificationExecutor<EconomyIndustry> {

    EconomyIndustry findByCode(String code);

}

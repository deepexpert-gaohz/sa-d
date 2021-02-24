package com.ideatech.ams.risk.highRisk.dao;

import com.ideatech.ams.risk.highRisk.entity.HighRiskApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface HighRiskApiDao extends JpaRepository<HighRiskApi, Long>, JpaSpecificationExecutor<HighRiskApi> {

    HighRiskApi getHighRiskApiByApiNoIs(String apiNo);

    List<HighRiskApi> findByCorporateFullIdAndApiName(String orgfullId, String apiName);

    List<HighRiskApi> findByCorporateBankAndApiName(String bank, String apiName);

    List<HighRiskApi> findByCorporateBank(String bank);

    HighRiskApi getHighRiskApiByApiNoAndCorporateFullId(String apiNo, String corporateFullId);

    HighRiskApi getHighRiskApiByApiNoAndCorporateBank(String apiNo, String bank);

    /**
     * @return
     * @Description 根据机构id获得该机构的外部接口信息
     * @author yangwz
     * @date 2019-11-04 14:09
     * @params corporateFullId:完整机构id
     */
    List<HighRiskApi> findByCorporateFullId(String corporateFullId);
}

package com.ideatech.ams.risk.riskdata.dao;

import com.ideatech.ams.risk.model.entity.ModelCount;
import com.ideatech.ams.risk.riskdata.dto.RiskDataSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface RiskDataDao extends JpaRepository<ModelCount,Long>, JpaSpecificationExecutor<ModelCount> {

    @Query(value = "select b.yd_cjrq,\n" +
            "       c.yd_name,\n" +
            "       b.yd_kh_id,\n" +
            "       b.yd_kh_name,\n" +
            "       c.yd_full_id ,\n" +
            "       a.yd_model_id,\n" +
            "       sum(b.yd_cn)\n" +
            "  from yd_risk_models a, yd_risk_model_count b, yd_sys_organization c\n" +
            " where a.yd_model_id = b.yd_model_id\n" +
            "   and b.yd_org_id = c.yd_code\n" +
            "   and b.yd_cjrq >=:#{#minDate}\n"+
            "   and b.yd_cjrq <=:#{#maxDate}\n"+
            "   and b.yd_kh_name like :#{#khName}\n"+
            "   and b.yd_kh_id like :#{#khId}\n"+
            " group by b.yd_cjrq,\n" +
            "       c.yd_name,\n" +
            "       b.yd_kh_id,\n" +
            "       b.yd_kh_name,\n" +
            "       c.yd_full_id ,\n" +
            "       a.yd_model_id"+
            " order  by ?#{#pageable}",nativeQuery = true)
    Page<Object[]> queryRiskData(RiskDataSearchDto riskDataSearchDto, Pageable pageable);


}

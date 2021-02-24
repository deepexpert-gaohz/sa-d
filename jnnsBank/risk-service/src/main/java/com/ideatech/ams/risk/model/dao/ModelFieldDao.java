package com.ideatech.ams.risk.model.dao;

import com.ideatech.ams.risk.model.entity.Model;
import com.ideatech.ams.risk.model.entity.ModelField;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ModelFieldDao extends JpaRepository<ModelField,Long>, JpaSpecificationExecutor<ModelField> {

    List<ModelField> findAllByModelIdAndExportFlagAndShowFlagOrderByOrderFlag(String modelId, int exportFlag, int showFlag);

/*    List<ModelField> findAllByModelId(String modelId);

    List<ModelField> findAllByModelIdAndExportFlagOrderByOrderFlag(String modelId, int exportFlag);

    List<ModelField> findAllByModelIdAndExportFlagAndCorporateBank(String modelId, int exportFlag,String code);

    List<ModelField> findByCorporateBank(String corporateBank);*/

    List<ModelField> findAllByModelIdAndShowFlag(String modelId, int showFlage, Sort sort);

    List<ModelField> findAllByModelId(String modelId);
    @Transactional
    @Query(value = "delete from yd_risk_model_field where yd_model_id = ?1 ", nativeQuery = true)
    @Modifying
    void deleteByModelId(String modelId);
}

package com.ideatech.ams.risk.model.dao;

import com.ideatech.ams.risk.model.entity.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelDao extends JpaRepository<Model,Long>, JpaSpecificationExecutor<Model> {


    Page<Model> findAll(Specification<Model> specification, Pageable pageable);

    Model findByNameAndCorporateBank(String name, String corporateBank);

    List<Model> findAllByTableNameAndCorporateBank(String tableName, String corporateBank);

    Model findByModelId(String modelId);

    List<Model> findByCorporateBank(String corporateBank);
//    @Query("select new com.ideatech.ams.risk.model.domain.ModelDo(m.modelId,m.name,m.mdesc,rl.levelName,rt.ruleName,rr.typeName,m.status) " +
//            "from Model m,RiskLevel rl,RiskType rt,RiskRule,rr " +
//            "where m.levelId = rl.id and m.typeIdId = rt.id and m.ruleId = rr.id")
//    List<ModelDo> searchModel();

    /**
     * @Description 根据模型类型查询模型
     * @author yangwz
     * @date 2020/8/31 15:57
    */
    List<Model> findByTypeId(String typeId);

    List<Model> findByTypeIdAndStatus(String typeId, String status);
}

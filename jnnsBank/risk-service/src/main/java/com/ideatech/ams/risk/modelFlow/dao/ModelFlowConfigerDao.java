package com.ideatech.ams.risk.modelFlow.dao;


import com.ideatech.ams.risk.modelFlow.entity.ModelFlowConfiger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ModelFlowConfigerDao extends JpaRepository<ModelFlowConfiger,Long>, JpaSpecificationExecutor<ModelFlowConfiger> {

    ModelFlowConfiger findByModelId(String modelId);

}

package com.ideatech.ams.risk.rely.dao;

import com.ideatech.ams.risk.rely.entity.ModelRely;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ModelRelyDao extends JpaRepository<ModelRely,Long>, JpaSpecificationExecutor<ModelRely> {
    List<ModelRely> findByModelTable(String mTable);
}

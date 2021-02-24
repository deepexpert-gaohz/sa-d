package com.ideatech.ams.risk.tableManager.dao;

import com.ideatech.ams.risk.tableManager.entity.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TableInfoDao extends JpaRepository<TableInfo,Long>, JpaSpecificationExecutor<TableInfo> {
    TableInfo findByEname(String cname);




}

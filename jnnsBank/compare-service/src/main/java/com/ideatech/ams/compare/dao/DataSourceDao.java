package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceDao extends JpaRepository<DataSource, Long>, JpaSpecificationExecutor<DataSource> {

    DataSource findByCode(String code);

    DataSource findByName(String name);

    DataSource findByNameAndIdNot(String name,Long id);
}

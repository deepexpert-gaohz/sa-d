package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.CompareField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompareFieldDao extends JpaRepository<CompareField,Long>, JpaSpecificationExecutor<CompareField> {
    List<CompareField> findByName(String name);

    List<CompareField> findByField(String field);

}

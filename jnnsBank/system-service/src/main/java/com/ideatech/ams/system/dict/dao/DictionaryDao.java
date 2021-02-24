package com.ideatech.ams.system.dict.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideatech.ams.system.dict.entity.DictionaryPo;

public interface DictionaryDao extends JpaRepository<DictionaryPo, Long>, JpaSpecificationExecutor<DictionaryPo> {
    DictionaryPo findByName(String name);

	List<DictionaryPo> findByNameStartingWith(String name);
}

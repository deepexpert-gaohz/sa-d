package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.CompareFields;
import com.ideatech.ams.annual.enums.CompareFieldEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompareFieldsDao extends JpaRepository<CompareFields, Long>, JpaSpecificationExecutor<CompareFields> {

	List<CompareFields> findByTaskId(Long taskId);

	CompareFields findByCompareFieldEnumAndTaskId(CompareFieldEnum compareFieldEnum,Long taskId);
}

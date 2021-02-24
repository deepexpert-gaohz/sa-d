package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.CompareRule;
import com.ideatech.ams.annual.enums.CompareFieldEnum;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CompareRuleDao extends JpaRepository<CompareRule, Long>, JpaSpecificationExecutor<CompareRule> {

	List<CompareRule> findByTaskId(Long taskId);

	CompareRule findByDataSourceEnumAndCompareFieldEnumAndTaskId(DataSourceEnum dataSourceEnum,CompareFieldEnum compareFieldEnum,Long taskId);
}

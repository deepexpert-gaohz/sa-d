package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.CompareRuleDataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jzh
 * @date 2019/1/17.
 */

@Repository
public interface CompareRuleDataSourceDao extends JpaRepository<CompareRuleDataSource,Long>, JpaSpecificationExecutor<CompareRuleDataSource> {

    List<CompareRuleDataSource> findByCompareRuleId(Long compareRuleId);

    List<CompareRuleDataSource> findAllByCompareRuleIdAndActive(Long compareRuleId, Boolean active);

    CompareRuleDataSource findAllByCompareRuleIdAndDataSourceIdAndActive(Long compareRuleId,Long dataSourceId,Boolean active);

    void deleteAllByCompareRuleId(Long compareRuleId);

    List<CompareRuleDataSource> findByDataSourceIdAndActive(Long dataSourceId,Boolean active);
}

package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.CompareDefine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jzh
 * @date 2019/1/17.
 */
@Repository
public interface CompareDefineDao extends JpaRepository<CompareDefine,Long>, JpaSpecificationExecutor<CompareDefine> {

    void deleteAllByCompareRuleId(Long compareRuleId);

    CompareDefine findByCompareRuleIdAndDataSourceIdAndCompareFieldId(Long compareRuleId,Long dataSourceId,Long compareFieldId);

    List<CompareDefine> findByCompareRuleIdAndActive(Long compareRuleId, Boolean active);

    List<CompareDefine> findByCompareRuleId(Long compareRuleId);

}

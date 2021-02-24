package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.CompareRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author jzh
 * @date 2019/1/16.
 */
@Repository("moduleCompareRuleDao")
public interface CompareRuleDao extends JpaRepository<CompareRule,Long> , JpaSpecificationExecutor<CompareRule> {

    CompareRule findByName(String name);
}

package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.CompareRuleFields;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jzh
 * @date 2019/1/17.
 */
@Repository
public interface CompareRuleFieldsDao extends JpaRepository<CompareRuleFields,Long>,JpaSpecificationExecutor< CompareRuleFields> {

    void deleteAllByCompareRuleId(Long compareRuleId);

    List<CompareRuleFields> findAllByCompareRuleId(Long compareRuleId);
}

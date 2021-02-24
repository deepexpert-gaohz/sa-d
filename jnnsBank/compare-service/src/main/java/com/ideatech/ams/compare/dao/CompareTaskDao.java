package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.CompareTask;
import com.ideatech.ams.compare.enums.CompareState;
import com.ideatech.ams.compare.enums.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompareTaskDao extends JpaRepository<CompareTask, Long> , JpaSpecificationExecutor<CompareTask> {
    List<CompareTask> findByTaskTypeAndStateIn(TaskType taskType, CompareState... state);
    CompareTask findByName(String name);
    CompareTask findByNameAndIdNot(String name,Long id);
    List<CompareTask> findByCompareRuleId(Long compareRuleId);
    List<CompareTask> findAllByNameLikeAndCreateNameOrderByCreatedDateDesc(String name,String createName);
}

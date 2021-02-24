package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.CollectTask;
import com.ideatech.ams.annual.entity.CollectTaskErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectTaskErrorMessageDao  extends JpaRepository<CollectTaskErrorMessage, Long>, JpaSpecificationExecutor<CollectTaskErrorMessage> {
    void deleteAllByTaskIdAndAnnualTaskId(Long taskId,Long annualTaskId);

    List<CollectTaskErrorMessage> findByTaskIdAndAnnualTaskId(Long taskId, Long annualTaskId);
}

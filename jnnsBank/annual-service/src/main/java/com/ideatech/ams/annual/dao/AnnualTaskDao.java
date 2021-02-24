package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.AnnualTask;
import com.ideatech.ams.annual.enums.TaskTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnualTaskDao extends JpaRepository<AnnualTask, Long>, JpaSpecificationExecutor<AnnualTask> {

	AnnualTask findById(Long id);

	List<AnnualTask> findByYearAndType(Integer year, TaskTypeEnum type);

}

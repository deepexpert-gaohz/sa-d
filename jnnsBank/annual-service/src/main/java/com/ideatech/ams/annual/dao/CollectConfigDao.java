package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.CollectConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 采集配置
 * @Author wanghongjie
 * @Date 2018/8/10
 **/
@Repository
public interface CollectConfigDao extends JpaRepository<CollectConfig, Long>, JpaSpecificationExecutor<CollectConfig> {
     List<CollectConfig> findByAnnualTaskIdOrderByCreatedDateDesc(Long annualTaskId);
}

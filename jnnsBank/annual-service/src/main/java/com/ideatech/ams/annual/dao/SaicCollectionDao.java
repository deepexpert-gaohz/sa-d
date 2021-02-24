package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.FetchSaicInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 年检的工商数据Dao层
 * @Author wanghongjie
 * @Date 2018/8/7
 **/
@Repository
public interface SaicCollectionDao extends JpaRepository<FetchSaicInfo, Long>, JpaSpecificationExecutor<FetchSaicInfo> {

	List<FetchSaicInfo> findByAnnualTaskId(Long taskId);

}

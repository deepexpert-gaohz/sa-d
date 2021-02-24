package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.CoreCollection;
import com.ideatech.ams.annual.entity.FetchPbcInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 年检的人行数据Dao层
 * @Author wanghongjie
 * @Date 2018/8/7
 **/
@Repository
public interface PbcCollectionDao extends JpaRepository<FetchPbcInfo, Long>, JpaSpecificationExecutor<FetchPbcInfo> {

	List<FetchPbcInfo> findByAnnualTaskId(Long taskId);

}

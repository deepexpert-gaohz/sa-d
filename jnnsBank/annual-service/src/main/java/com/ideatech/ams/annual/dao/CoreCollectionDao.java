package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.CoreCollection;
import com.ideatech.ams.annual.enums.CollectState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 年检的核心数据Dao层
 * @Author wanghongjie
 * @Date 2018/8/7
 **/
@Repository
public interface CoreCollectionDao extends JpaRepository<CoreCollection, Long>, JpaSpecificationExecutor<CoreCollection> {

	List<CoreCollection> findByAnnualTaskId(Long taskId);

	List<CoreCollection> findByCollectTaskId(Long collectTaskId);

	List<CoreCollection> findByAnnualTaskIdAndCollectState(Long taskId,CollectState collectState);

	Long countAllByCollectState(CollectState collectState);

	@Modifying
	@Query("delete from CoreCollection where annualTaskId = ?1")
	void deleteByAnnualTaskId(Long taskId);

	List<CoreCollection> findByAnnualTaskIdAndCollectStateNotIn(Long taskId,CollectState... state);

//	Long countByAnnualTaskIdAndCollectStateNotIn(Long taskId,CollectState... state);

	List<CoreCollection> findByCollectTaskIdAndAcctNoAndCollectState(Long collectTaskId, String acctNo, CollectState collectState);

}

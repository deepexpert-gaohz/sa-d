package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.FetchSaicInfo;
import com.ideatech.ams.annual.enums.CollectState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FetchSaicDao extends JpaRepository<FetchSaicInfo, Long>, JpaSpecificationExecutor<FetchSaicInfo> {
    /**
     * 查询状态不为特定值的数据
     *
     * @param taskId
     * @param collectState
     * @return
     */
    List<FetchSaicInfo> findByCollectTaskIdAndCollectStateNot(Long taskId, CollectState collectState);

    /**
     * 查询状态为特定值的数据
     *
     * @param taskId
     * @param collectState
     * @return
     */
    List<FetchSaicInfo> findByCollectTaskIdAndCollectState(Long taskId, CollectState collectState);

    @Modifying
    @Query("delete from FetchSaicInfo where annualTaskId = ?1")
	void deleteByAnnualTaskId(Long taskId);

}

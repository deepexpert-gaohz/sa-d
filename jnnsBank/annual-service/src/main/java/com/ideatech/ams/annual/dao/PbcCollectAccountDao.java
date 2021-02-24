package com.ideatech.ams.annual.dao;

import com.ideatech.ams.annual.entity.PbcCollectAccount;
import com.ideatech.ams.annual.enums.CollectState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PbcCollectAccountDao extends JpaRepository<PbcCollectAccount, Long>, JpaSpecificationExecutor<PbcCollectAccount> {
    List<PbcCollectAccount> findByCollectOrganId(Long id);

    /**
     * 根据机构号并不等于采集成功状态
     *
     * @param id
     * @param state
     * @return
     */
    List<PbcCollectAccount> findByCollectOrganIdAndCollectStateNotIn(Long id, CollectState... state);

    PbcCollectAccount findByAcctNo(String acctNo);

    /**
     * 根据TaskId获取全部的人行数据
     *
     * @param annualTaskId
     * @return
     */
    List<PbcCollectAccount> findByAnnualTaskId(Long annualTaskId);

    long countByCollectStateNotIn(CollectState... state);
}

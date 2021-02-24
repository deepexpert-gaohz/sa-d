package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.PbcCollectAccountDto;
import com.ideatech.ams.annual.dto.PbcCollectResultSearchDto;
import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.common.service.BaseService;

import java.util.List;

public interface PbcCollectAccountService  extends BaseService<PbcCollectAccountDto> {

    List<PbcCollectAccountDto> findByCollectOrganId(Long id);

    /**
     * 根据机构号并不等于采集成功状态
     *
     * @param id
     * @param state
     * @return
     */
    List<PbcCollectAccountDto> findByCollectOrganIdAndCollectStateNot(Long id, CollectState... state);

    PbcCollectAccountDto findByAcctNo(String acctNo);

    /**
     * 根据TaskId获取全部的人行数据
     *
     * @param annualTaskId
     * @return
     */
    List<PbcCollectAccountDto> findByAnnualTaskId(Long annualTaskId);

    PbcCollectResultSearchDto search(final PbcCollectResultSearchDto pbcCollectResultSearchDto, final Long taskId);

    void deleteAll();

    Long count();

    long countByCollectStateNot(CollectState... state);
}

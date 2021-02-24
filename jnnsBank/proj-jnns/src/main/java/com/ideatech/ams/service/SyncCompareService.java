package com.ideatech.ams.service;

import com.ideatech.ams.domain.SyncCompare;
import com.ideatech.ams.dto.SyncCompareInfo;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

/**
 * 上报账户实时比对报表Service
 *
 * @auther ideatech
 * @create 2018-11-29 9:42 AM
 **/
public interface SyncCompareService {

    /**
     * 分页查询
     *
     * @param info
     * @param pageable
     * @return
     */
    TableResultResponse<SyncCompareInfo> query(SyncCompareInfo info, Pageable pageable);

    /**
     * 保存
     *
     * @param syncCompareInfo
     */
    void create(SyncCompareInfo syncCompareInfo);

    /**
     * 修改
     *
     * @param syncCompareInfo
     */
    void update(SyncCompareInfo syncCompareInfo);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    SyncCompare findTopByAcctNoAndKaixhubzAndBusinessDateOrderByLastUpdateDateDesc(String acctNo, String kaixhubz, String BusinessDate);
}

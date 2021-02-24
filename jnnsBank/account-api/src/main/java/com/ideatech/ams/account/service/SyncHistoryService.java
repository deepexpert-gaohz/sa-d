package com.ideatech.ams.account.service;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.SyncHistoryDto;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

public interface SyncHistoryService extends BaseService<SyncHistoryDto> {
    /**
     *  分页查询
     * @param syncHistoryDto
     * @param pageable
     * @return
     */
    TableResultResponse query(SyncHistoryDto syncHistoryDto, Pageable pageable);

    /**
     * 记录上报日志
     * @param billsPublic
     * @param syncType
     */
    void write(AllBillsPublicDTO billsPublic, EAccountType syncType, CompanySyncStatus syncStatus,String errorMessage);

    /**
     * 获取人行、机构信用、影像上报错误信息
     * @return
     */
    JSONObject getSyncErrorMsg(Long refBillId);

}

package com.ideatech.ams.account.service.bill;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.bill.BillOperateLockDTO;

/**
 * 流水操作锁定表service
 */
public interface BillOperateLockService {
    /**
     * 上锁
     *
     * @param billId 流水id
     */
    void billLock(Long billId) throws Exception;

    /**
     * 解锁
     *
     * @param billId 流水id
     */
    void billUnLock(Long billId);

    /**
     * 判断指定流水有没有人员正在操作
     *
     * @param billId 流水id
     * @return 正在操作人员名称和所在机构名称
     */
    JSONObject getBillIsBusy(Long billId);

    BillOperateLockDTO getByBillId(Long billId);

}

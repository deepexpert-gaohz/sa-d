package com.ideatech.ams.ws.api.service;

import com.ideatech.common.dto.ResultDto;

public interface BillOperateLockApiService {

    /**
     * 上锁
     *
     * @param billId 流水id
     */
    ResultDto billLock(Long billId);

    /**
     * 解锁
     *
     * @param billId 流水id
     */
    ResultDto billUnLock(Long billId);
}

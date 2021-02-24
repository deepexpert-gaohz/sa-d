package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.account.service.bill.BillOperateLockService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class BillOperateLockApiServiceImpl implements BillOperateLockApiService {

    @Autowired
    private BillOperateLockService billOperateLockService;

    @Override
    public ResultDto billLock(Long billId) {
        try {
            billOperateLockService.billLock(billId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDtoFactory.toNack(e.getMessage());
        }
        return ResultDtoFactory.toAck();
    }

    @Override
    public ResultDto billUnLock(Long billId) {
        try {
            billOperateLockService.billUnLock(billId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDtoFactory.toNack("解锁失败");
        }
        return ResultDtoFactory.toAck();
    }
}

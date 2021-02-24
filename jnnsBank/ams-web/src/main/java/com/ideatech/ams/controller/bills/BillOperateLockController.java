package com.ideatech.ams.controller.bills;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.service.bill.BillOperateLockService;
import com.ideatech.common.msg.ObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/billOperateLock")
@Slf4j
public class BillOperateLockController {

    @Autowired
    private BillOperateLockService billOperateLockService;

    /**
     * 获取指定流水是否有人员正在操作
     *
     * @param billId 流水id
     * @return 操作人员数据
     */
    @RequestMapping(value = "/getBillIsBusy")
    public ObjectRestResponse getBillIsBusy(Long billId) {
        JSONObject jsonObject = billOperateLockService.getBillIsBusy(billId);
        return new ObjectRestResponse<>().rel(jsonObject != null).result(jsonObject);
    }

    /**
     * 上锁
     *
     * @param billId 流水id
     */
    @RequestMapping(value = "/billLock")
    public ObjectRestResponse billLock(Long billId) {
        try {
            billOperateLockService.billLock(billId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjectRestResponse<>().rel(false).msg(e.getMessage());
        }
        return new ObjectRestResponse<>().rel(true);
    }

    /**
     * 解锁
     *
     * @param billId 流水id
     */
    @RequestMapping(value = "/billUnLock")
    public ObjectRestResponse billUnLock(Long billId) {
        try {
            billOperateLockService.billUnLock(billId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjectRestResponse<>().rel(false);
        }
        return new ObjectRestResponse<>().rel(true);
    }

}

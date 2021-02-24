package com.ideatech.ams.account.executor;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ApplyFinalStatusUpdateExecutor implements Runnable {

    private AllBillsPublicService allBillsPublicService;

    private Boolean isSyncSuccess;

    private AllBillsPublicDTO billsPublic;

    @Override
    public void run() {
        allBillsPublicService.applyFinalStatusUpdate(isSyncSuccess, billsPublic);

    }
}

package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.common.dto.ResultDto;

public interface CoreOpenSyncService {
    ResultDto getCoreOpenSyncResult(AllBillsPublicDTO billsPublic);
}

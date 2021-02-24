package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.common.dto.ResultDto;

public interface CoreChangeSyncService {
    ResultDto getCoreChangeSyncResult(AllBillsPublicDTO billsPublic);
}

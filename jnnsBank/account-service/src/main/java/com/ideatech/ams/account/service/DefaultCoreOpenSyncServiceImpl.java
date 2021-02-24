package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;

public class DefaultCoreOpenSyncServiceImpl implements CoreOpenSyncService {

    @Override
    public ResultDto getCoreOpenSyncResult(AllBillsPublicDTO billsPublic) {
        return ResultDtoFactory.toCustom("", "", null);
    }

}

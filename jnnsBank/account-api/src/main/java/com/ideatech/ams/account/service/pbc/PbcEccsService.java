package com.ideatech.ams.account.service.pbc;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.pbc.exception.SyncException;

public interface PbcEccsService {

    /**
     * 信用代码上报
     * @param billsPublic
     * @throws Exception
     * @throws SyncException
     */
    void eccsAccountSync(AllBillsPublicDTO billsPublic) throws Exception, SyncException;

}

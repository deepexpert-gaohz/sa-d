package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.msg.ObjectRestResponse;

public interface AccountApiService {
    ObjectRestResponse<AllBillsPublicDTO> getAccountInfo(String billNo);

    ObjectRestResponse<AllBillsPublicDTO> updateBillsInfo(String billNo, String acctNo, String customerNo);

    ResultDto saveChangeBills(String organCode, String acctNo, CompanyAcctType acctType, AllBillsPublicDTO billsPublic);

    ObjectRestResponse getAccountByAcctTypeDepositorName(String acctType, String depositorName);

    ObjectRestResponse getAccountByAcctNo(String acctNo);

    ObjectRestResponse<AllBillsPublicDTO> updateBillsAccount(String billNo, String acctNo);
}

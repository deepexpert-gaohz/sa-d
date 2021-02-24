package com.ideatech.ams.controller.account;


import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.ws.api.service.AccountApiService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.msg.ObjectRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accountApi")
public class AccountTestApiController {

    @Autowired
    private AccountApiService accountApiService;

    @RequestMapping(value = "/saveChangeBills", method = RequestMethod.GET)
    public ResultDto saveChangeBills(String organCode, String acctNo, CompanyAcctType acctType, AllBillsPublicDTO billsPublic) {
        return accountApiService.saveChangeBills(organCode, acctNo, acctType, billsPublic);
    }

    @RequestMapping(value = "/getAccountInfo", method = RequestMethod.GET)
    public ObjectRestResponse<AllBillsPublicDTO> saveChangeBills(String billNo) {
        return accountApiService.getAccountInfo(billNo);
    }

    /**
     * 取消核准的打印数据
     */
    @RequestMapping(value = "/getAccountByAcctTypeDepositorName", method = RequestMethod.GET)
    public ObjectRestResponse getAccountByAcctTypeDepositorName(String acctType, String depositorName) {
        return accountApiService.getAccountByAcctTypeDepositorName(acctType, depositorName);
    }

    /**
     * 取消核准的打印数据
     */
    @RequestMapping(value = "/getAccountByAcctNo", method = RequestMethod.GET)
    public ObjectRestResponse getAccountByAcctNo(String acctNo) {
        return accountApiService.getAccountByAcctNo(acctNo);
    }

}

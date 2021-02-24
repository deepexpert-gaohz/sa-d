package com.ideatech.ams.ws.api.restful;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.ws.api.service.AccountApiService;
import com.ideatech.common.msg.ObjectRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountApiController.class);

    @Autowired
    private AccountApiService accountApiService;

    /**
     * 根据流水号获取本地账户数据和客户数据
     * @param billNo
     * @return
     */
    @RequestMapping(value = "/getAccountInfo", method = RequestMethod.GET)
    public ObjectRestResponse<AllBillsPublicDTO> getAccountInfo(String billNo) {
        if(billNo != null) {
            return accountApiService.getAccountInfo(billNo);
        }

        return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("流水号为空");
    }

    @RequestMapping(value = "/updateBillsInfo", method = RequestMethod.GET)
    public ObjectRestResponse<AllBillsPublicDTO> updateBillsInfo(String billNo, String acctNo, String customerNo) {
        return accountApiService.updateBillsInfo(billNo, acctNo, customerNo);

    }

    @RequestMapping(value = "/updateBillsAccount", method = RequestMethod.GET)
    public ObjectRestResponse<AllBillsPublicDTO> updateBillsAccount(String billNo, String acctNo) {
        return accountApiService.updateBillsAccount(billNo, acctNo);

    }

}

package com.ideatech.ams.account.spi.processor;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.system.user.dto.UserDto;

import java.util.Map;

public class BasicAllPublicAccountChangeFormDataProcessor extends BasicAllPublicAccountFormDataProcessor {

    @Override
    protected AllBillsPublicDTO doProcess(UserDto userInfo, Map<String, String> formData) {

        AllBillsPublicDTO result = super.doProcess(userInfo, formData);

        formData.put("refBillId", result.getRefBillId() + "");
        //保存变更数据流水
        accountChangeSummaryService.saveAccountChangeSummary(formData);

        return result;
    }

}

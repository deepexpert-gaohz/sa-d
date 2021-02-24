package com.ideatech.ams.account.validate;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author yang
 * @Date 2019/10/30 16:35
 * @Version 1.0
 */
@Slf4j
public class AmsCloseSuspendValidate extends AbstractAllPublicAccountOpenValidate {
    @Override
    protected void doValidate(AllBillsPublicDTO dto) throws Exception {
        if(dto.getAccountStatus()!=AccountStatus.suspend){
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0114:账户不是久悬状态，不能撤销久悬!");
        }
    }
}

package com.ideatech.ams.account.validate;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractAllPublicAccountValidate implements AllPublicAccountValidate {

    @Override
    public void validate(AllBillsPublicDTO dto) throws Exception {
        log.info("开始校验数据:" + dto.getAcctType().getValue() + "-" + dto.getBillType().getValue() + " 处理器:" + getClass().getSimpleName());
        doValidate(dto);
        log.info("校验数据完毕:" + dto.getAcctType().getValue() + "-" + dto.getBillType().getValue() + "完毕");
    }

    /**
     * @param dto
     */
    protected abstract void doValidate(AllBillsPublicDTO dto) throws Exception;
}

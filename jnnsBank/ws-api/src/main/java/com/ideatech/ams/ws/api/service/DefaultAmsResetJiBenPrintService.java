package com.ideatech.ams.ws.api.service;

import com.ideatech.common.dto.ResultDto;

public interface DefaultAmsResetJiBenPrintService {
    /**
     * 基本户补打校验
     * @param acctNo
     * @param pbcCode
     * @return
     */
    ResultDto jiBenResetDetails(String acctNo, String pbcCode);

    /**
     * 基本户补打反显新的编号以及注册号
     * @param acctNo
     * @param pbcCode
     * @return
     */
    ResultDto jiBenResetPrint(String acctNo, String pbcCode);
}

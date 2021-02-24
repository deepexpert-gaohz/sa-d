package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.common.dto.ResultDto;

public interface AnnualResultApiService {
    public void submitPbcFinished(String acctNo) throws Exception;

    /**
     * 年检结果查询
     * @param acctNo 账号
     */
    AnnualResultDto annualResultSearch(String acctNo) throws Exception;

    /**
     * 修改年检结果
     * @param acctNo
     * @return
     */
    ResultDto updateAnnualResult(String acctNo) throws Exception;
}

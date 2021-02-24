package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.annual.dto.AnnualResultDto;

import java.util.Date;

public class AnnualResultNoticeApiServiceImpl implements AnnualResultNoticeApiService {
    /**
     * 年检结果通知核心
     * @param acctNo
     * @param depositorName
     * @param createdDate
     * @param result
     * @param legalTelephone
     */
    @Override
    public void annualResultNotice(String acctNo, String depositorName, Date createdDate, String result, String legalTelephone) {

    }

    /**
     * 年检结果短信通知(待处理、年检失败)
     * @param annualResultDto
     * @param formatMessage  短信内容
     * @param legalTelephone 法人联系电话
     */
    @Override
    public void annualResultMessageNotice(AnnualResultDto annualResultDto, String formatMessage, String legalTelephone) {

    }

}

package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.annual.dto.AnnualResultDto;

import java.util.Date;

public interface AnnualResultNoticeApiService {

    public void annualResultNotice(String acctNo, String depositorName, Date createdDate, String result, String legalTelephone);

    public void annualResultMessageNotice(AnnualResultDto annualResultDto, String formatMessage, String legalTelephone);

}

package com.ideatech.ams.risk.riskdata.service;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.risk.riskdata.dto.RiskRecordInfoDto;
import com.ideatech.ams.risk.riskdata.entity.RiskRecordInfo;

import java.util.List;

public interface RiskApiService {
    void generateRisk2003Data(String riskData, Boolean isHighRsikApi);

    List<RiskRecordInfo> generateRisk2007Data(String riskData, Boolean isHighRsikApi) throws InterruptedException;

    void save(RiskRecordInfoDto riskRecordInfoDto);

    RiskRecordInfoDto findById(String countId);

    String syncAccountInfo(AllBillsPublicDTO billsPublic);

    void syncRiskData(String date);
}

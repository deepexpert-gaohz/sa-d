package com.ideatech.ams.risk.riskdata.service;

import com.ideatech.ams.risk.riskdata.RiskRegisterInfoDto;
import com.ideatech.ams.risk.riskdata.dto.RiskDataSearchDto;
import com.ideatech.ams.risk.riskdata.entity.RiskHandleInfo;
import com.ideatech.ams.risk.riskdata.entity.RiskRegisterInfo;

import java.util.List;

public interface RiskRegisterInfoService {

    void saveRegisterInfo(RiskRegisterInfoDto riskRegisterInfoDto);

    RiskRegisterInfoDto findById(String id);

    RiskDataSearchDto queryRegisterRiskInfo(RiskDataSearchDto riskDataSearchDto);

    RiskDataSearchDto countRegisterRiskInfo(RiskDataSearchDto riskDataSearchDto);

    String getBankCodeByAccount(String accountNo);

    List<RiskRegisterInfo> findAllByAccountNoAndStatusAndCorporateBank(String accountNo, String s, String corporateBank);
}

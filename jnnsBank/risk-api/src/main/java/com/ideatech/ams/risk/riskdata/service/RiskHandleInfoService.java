package com.ideatech.ams.risk.riskdata.service;

import com.ideatech.ams.risk.riskdata.dto.RiskDataSearchDto;
import com.ideatech.ams.risk.riskdata.dto.RiskHandleInfoDto;
import com.ideatech.ams.risk.riskdata.entity.RiskHandleInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RiskHandleInfoService {
    void saveHandleInfo(RiskHandleInfoDto riskHarndleInfoDto);


    RiskDataSearchDto querytodoRiskInfo(RiskDataSearchDto riskDataSearchDto);

    RiskHandleInfoDto findByRId(String id);

    void deleteById(String id);

    RiskDataSearchDto getRiskDataSearchDto(RiskDataSearchDto riskDataSearchDto, String sql, String countSql, String countStSql, String handleList);

    List<RiskHandleInfo> findIdByAccountNoAndStatus(String accountNo, String status);
}

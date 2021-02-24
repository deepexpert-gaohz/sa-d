package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dto.JudicialInformationDto;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JudicialInformationService {

    TableResultResponse<JudicialInformationDto> getDetailFromCompanyName(String companyName, Pageable pageable);

    JudicialInformationDto findByCaseNo(String caseNo);
}

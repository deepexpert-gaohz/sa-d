package com.ideatech.ams.system.proof.service;

import com.ideatech.ams.system.proof.dto.ProofReportDto;
import com.ideatech.ams.system.proof.enums.ProofType;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProofReportService extends BaseService<ProofReportDto> {

    ProofReportDto findByAcctNoAndType(String acctNo, ProofType type);

    TableResultResponse query(ProofReportDto accountKycReportDto, Pageable pageable);

    Map<String,Object> searchAll(ProofReportDto accountKycReportDto);

    IExcelExport export(ProofReportDto accountKycReportDto);

    Long count(ProofReportDto accountKycReportDto);
}

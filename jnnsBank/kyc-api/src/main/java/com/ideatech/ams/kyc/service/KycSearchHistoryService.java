package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dto.KycSearchHistoryDto;
import com.ideatech.ams.kyc.dto.KycSearchHistorySearchDto;
import com.ideatech.ams.kyc.dto.SaicSearchHistorySearchDto;
import com.ideatech.ams.kyc.enums.SearchStatus;
import com.ideatech.ams.kyc.enums.SearchType;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.List;

public interface KycSearchHistoryService {
    /**
     * 保存客户尽调查询历史记录
     * @param kycSearchHistoryDto
     */
    void save(KycSearchHistoryDto kycSearchHistoryDto);

    /**
     * 分页查询客户尽调历史记录
     * @param kycSearchHistorySearchDto
     * @return
     */
    KycSearchHistorySearchDto search(KycSearchHistorySearchDto kycSearchHistorySearchDto);

    /**
     * 分页查询客户尽调历史记录
     * @param kycSearchHistorySearchDto
     * @return
     */
    IExcelExport searchAll(KycSearchHistorySearchDto kycSearchHistorySearchDto);

    List<KycSearchHistoryDto> findByEntName(String entname);
}


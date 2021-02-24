package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.CompareResultDto;

import java.util.List;

public interface CompareExportService {
    String exportXlsResult(Long taskId, String matchType, Long organId);

    String exportTxtResult(Long taskId, String matchType, Long organId);

    void batchCreateCompareResult(Long taskId);
}

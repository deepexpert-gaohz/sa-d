package com.ideatech.ams.customer.service.illegal;

import com.ideatech.ams.customer.dto.illegal.IllegalQueryBatchDto;
import com.ideatech.ams.customer.dto.illegal.IllegalQueryDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IllegalQueryService {

    void save(IllegalQueryDto illegalQueryDto);

    Long saveIllegalQueryBatch(IllegalQueryBatchDto illegalQueryBatchDto);

    /**
     *根据批次号去调用
     */
    void illegalCheck(Long batchId);

    IllegalQueryDto illegalQueryCheck(Long id, String keyWord);

    TableResultResponse<IllegalQueryBatchDto> queryBatch(IllegalQueryBatchDto dto, Pageable pageable);

    TableResultResponse<IllegalQueryDto> query(IllegalQueryDto dto, Pageable pageable);

    IExcelExport exportIllegalQueryExcel(IllegalQueryDto dto);

    void saveIllegalQuery(List<IllegalQueryDto> illegalQueryDtoList, List<String> regNoList, Long batchId, List<Integer> strNum);

    Boolean checkIllegalStatus(Long batchId);

    long getIllegalQueryNum(Long batchId);

    Boolean checkIllegalExpired(Long batchId);
}

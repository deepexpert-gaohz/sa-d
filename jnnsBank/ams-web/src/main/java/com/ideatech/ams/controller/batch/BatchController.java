package com.ideatech.ams.controller.batch;

import com.ideatech.ams.controller.BaseController;
import com.ideatech.ams.kyc.dto.KycSearchHistoryDto;
import com.ideatech.ams.kyc.dto.KycSearchHistorySearchDto;
import com.ideatech.ams.kyc.service.KycSearchHistoryService;
import com.ideatech.ams.system.batch.dto.BatchDto;
import com.ideatech.ams.system.batch.dto.BatchSearchDto;
import com.ideatech.ams.system.batch.service.BatchService;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * 批次管理
 */
@Controller
@RequestMapping("/batch")
public class BatchController extends BaseController<BatchService, BatchDto> {

    @Autowired
    private KycSearchHistoryService kycSearchHistoryService;

    @Autowired
    private BatchService batchService;

    @GetMapping(value = "/query")
    public TableResultResponse query(BatchSearchDto info, Pageable pageable) {
        pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),new Sort(Sort.Direction.DESC, "processTime") );
        TableResultResponse response = getBaseService().query(info, pageable);
        return response;
    }


    @GetMapping(value = "/updateBatchCount")
    public void updateBatchCount(String batchNo) {
        KycSearchHistorySearchDto kycSearchHistorySearchDto = new KycSearchHistorySearchDto();

        kycSearchHistorySearchDto.setBatchNo(batchNo);
        KycSearchHistorySearchDto search = kycSearchHistoryService.search(kycSearchHistorySearchDto);

        batchService.updateBatchCount(batchNo, search.getTotalRecord());
        batchService.finishBatch(batchNo);

    }

}

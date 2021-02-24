package com.ideatech.ams.controller.batch;

import com.ideatech.ams.account.dto.BatchSuspendDto;
import com.ideatech.ams.account.dto.BatchSuspendSearchDto;
import com.ideatech.ams.account.service.BatchSuspendService;
import com.ideatech.ams.controller.BaseController;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 批次管理
 */
@Controller
@RequestMapping("/batchSuspend")
public class BatchSuspendController extends BaseController<BatchSuspendService, BatchSuspendDto> {

    @GetMapping(value = "/query")
    public TableResultResponse query(BatchSuspendSearchDto info, Pageable pageable) {
        if(StringUtils.isBlank(info.getOrganFullId())){
            info.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        }
        pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),new Sort(Sort.Direction.DESC, "createdDate") );
        TableResultResponse response = getBaseService().query(info, pageable);
        return response;
    }

    @GetMapping(value = "/submitSync")
    public ResultDto submitSync(@RequestParam("billIds[]") Long[] billIds, @RequestParam("ids[]") Long[] ids) {
        getBaseService().submitSync(ids, billIds);
        return ResultDtoFactory.toAck();
    }
}

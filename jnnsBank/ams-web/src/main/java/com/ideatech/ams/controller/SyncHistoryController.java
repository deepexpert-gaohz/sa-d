package com.ideatech.ams.controller;

import com.ideatech.ams.account.dto.SyncHistoryDto;
import com.ideatech.ams.account.service.SyncHistoryService;
import com.ideatech.common.msg.TableResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/syncHistory")
@Slf4j
public class SyncHistoryController {
    @Autowired
    private SyncHistoryService syncHistoryService;
    @RequestMapping(value = "/search")
    public TableResultResponse query(SyncHistoryDto syncHistoryDto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable){
        return syncHistoryService.query(syncHistoryDto,pageable);
    }

}

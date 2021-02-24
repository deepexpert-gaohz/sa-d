package com.ideatech.ams.controller;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.dto.SaicQuery.*;
import com.ideatech.ams.dto.ZhjnCustomerDto;
import com.ideatech.ams.dto.esb.*;
import com.ideatech.ams.service.EsbService;
import com.ideatech.ams.service.JnnsSaicTestService;
import com.ideatech.ams.utils.HttpIntefaceUtils;
import com.ideatech.common.msg.TableResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/jnns")
public class JnnsSaicTestController {

    @Autowired
    private JnnsSaicTestService jnnsSaicTestService;


    @RequestMapping("/doQuery")
    public void doGet(){
        jnnsSaicTestService.doQuery();
    }


    /**
     * 老赖 分页查询
     * @param dto
     * @param pageable
     * @return
     */
    @GetMapping("/queryDeabbeatList")
    public TableResultResponse<Deabbeat> listByDeabbrat(Deabbeat dto, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {

        TableResultResponse<Deabbeat> tableResultResponse = jnnsSaicTestService.queryDeabbeat(dto, pageable);

        return tableResultResponse;
    }

    /**
     * 欠税名单
     * @param dto
     * @param pageable
     * @return
     */
    @GetMapping("/queryOwingList")
    public TableResultResponse<Owing> listByOwing(Owing dto, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {

        TableResultResponse<Owing> tableResultResponse = jnnsSaicTestService.queryOwing(dto, pageable);

        return tableResultResponse;
    }



    /**
     * 违法
     * @param dto
     * @param pageable
     * @return
     */
    @GetMapping("/queryBreakLawList")
    public TableResultResponse<BreakLaw> listByBreak(BreakLaw dto, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {

        TableResultResponse<BreakLaw> tableResultResponse = jnnsSaicTestService.queryBreakLaw(dto, pageable);

        return tableResultResponse;
    }


}

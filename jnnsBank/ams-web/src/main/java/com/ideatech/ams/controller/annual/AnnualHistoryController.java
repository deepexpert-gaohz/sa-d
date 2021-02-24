package com.ideatech.ams.controller.annual;

import com.alibaba.fastjson.JSONArray;
import com.ideatech.ams.annual.service.AnnualTaskService;
import com.ideatech.common.msg.ObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 历史年检
 */
@RestController
@RequestMapping("/annualHistory")
@Slf4j
public class AnnualHistoryController {

    @Autowired
    private AnnualTaskService annualTaskService;

    /**
     * 获取历史年检json数据
     */
    @RequestMapping(value = "/getAnnualHistory")
    public ObjectRestResponse getAnnualHistory() {
        JSONArray jsonArray = annualTaskService.getAnnualHistory();
        return new ObjectRestResponse<>().rel(true).result(jsonArray);
    }
}

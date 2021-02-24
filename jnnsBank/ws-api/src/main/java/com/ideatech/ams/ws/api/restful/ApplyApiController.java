package com.ideatech.ams.ws.api.restful;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.service.CompanyPreOpenAccountEntService;
import com.ideatech.ams.apply.vo.CompanyPreOpenAccountEntVo;
import com.ideatech.ams.ws.api.service.ApplyApiService;
import com.ideatech.ams.ws.api.service.SaicApiService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by houxianghua on 2018/11/13.
 * service接口
 * 预约数据推送接口
 */
@RestController
@RequestMapping(value = "/api/apply")
@Slf4j
public class ApplyApiController {

    @Autowired
    private ApplyApiService applyApiService;

//    /**
//     * 添加预约记录，并发送短信给柜员
//     * 预约数据推送接口
//     * 由预约渠道将预约信息推送到AMS系统中进行后续的接洽和开户流程
//     *
//     * @param companyPreOpenAccountEntDto
//     * @param bankCode                    核心机构代码
//     */
//    @RequestMapping(value = "/save")
//    public ResultDto saveApply(CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto, String bankCode) {
//        return applyApiService.saveApply(companyPreOpenAccountEntDto, bankCode);
//    }

    /**
     * 为渠道提供预约查询的接口，通过接口获取预约详情信息；返回时支持多条数据
     */
    @RequestMapping(value = "/searchApplyList")
    public ResultDto searchApplyList(CompanyPreOpenAccountEntDto dto) {
        List<CompanyPreOpenAccountEntVo> listVo = applyApiService.searchApplyList(dto);
        return ResultDtoFactory.toApiSuccess(listVo);
    }

}

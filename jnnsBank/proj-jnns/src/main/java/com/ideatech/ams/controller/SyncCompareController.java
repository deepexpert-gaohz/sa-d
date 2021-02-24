package com.ideatech.ams.controller;

import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.AllAccountPublicDTO;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.AllAccountPublicService;
import com.ideatech.ams.dto.SyncCompareInfo;
import com.ideatech.ams.service.SyncCompareService;
import com.ideatech.ams.service.SyncCoreComparOpenAcctService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @auther ideatech
 * @create 2018-11-29 11:17 AM
 **/
@RestController
@RequestMapping("/syncCoreCompare")
public class SyncCompareController {

    private static final Logger log = LoggerFactory.getLogger(SyncCompareController.class);
    @Autowired
    private SyncCompareService syncCompareService;

    @Autowired
    private AllAccountPublicService allAccountPublicService;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private SyncCoreComparOpenAcctService syncCoreComparOpenAcctService;

    @GetMapping("/queryList")
    public TableResultResponse<SyncCompareInfo> list(SyncCompareInfo dto, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("页面展示方法开始。。。");
        //开户时间
        if (dto.getBusinessbeginDate() != null) {
            dto.setBusinessbeginDate(dto.getBusinessbeginDate().replaceAll("-", ""));
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String format = simpleDateFormat.format(new Date());
            dto.setBusinessbeginDate(format);
        }
        //截止时间
        if (dto.getBusinessendDate() != null) {
            dto.setBusinessendDate(dto.getBusinessendDate().replaceAll("-", ""));
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String format = simpleDateFormat.format(new Date());
            dto.setBusinessendDate(format);
        }
        if(StringUtils.isBlank(dto.getAcctType())){
            List<String> list1 = new ArrayList<>();
            list1.add("jiben");
            list1.add("yiban");
            list1.add("zhuanhu");
            list1.add("linshi");
            dto.setAcctTypeList(list1);
        }else{
            List<String> list2 = new ArrayList<>();
            list2.add(dto.getAcctType());
            dto.setAcctTypeList(list2);
        }
        //信用代码证系统报送状态
        if (StringUtils.isBlank(dto.getEccsStarts())) {
            List<String> eccsStartslist = new ArrayList<>();
            eccsStartslist.add("tongBuShiBai");
            eccsStartslist.add("weiShangBao");
            eccsStartslist.add("buTongBu");
            eccsStartslist.add("buTongBu");
            eccsStartslist.add("tongBuChengGong");
            dto.setEccsStartsList(eccsStartslist);
        } else if (dto.getEccsStarts().equals("all")) {//查询所有状态
            dto.setEccsStarts(null);
        }
        if (StringUtils.isBlank(dto.getPbcStarts())) {
            List<String> pbcStartslist = new ArrayList<>();
            pbcStartslist.add("tongBuShiBai");
            pbcStartslist.add("buTongBu");
            pbcStartslist.add("weiShangBao");
            pbcStartslist.add("tongBuChengGong");
            dto.setPbcStartsList(pbcStartslist);
        } else if (dto.getPbcStarts().equals("all")) {//查询所有状态
            dto.setPbcStarts(null);
        }

        log.info("页面传过来的实体类：" + dto.toString());
        TableResultResponse<SyncCompareInfo> tableResultResponse = syncCompareService.query(dto, pageable);
        return tableResultResponse;
    }

    @GetMapping("/sync")
    public ResultDto sync() {
        syncCoreComparOpenAcctService.syncCoreCompare();
        return ResultDtoFactory.toAck();
    }

    @RequestMapping(
            value = {"/details"},
            method = {RequestMethod.GET}
    )
    public ObjectRestResponse<AllAccountPublicDTO> getFormInitData(String acctNo) {
        AccountsAllInfo accountsAllInfo = accountsAllService.findByAcctNo(acctNo);
        Long id = accountsAllInfo.getId();
        return this.allAccountPublicService.getDetailsByAccountId(id);
    }
}

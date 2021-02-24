package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.service.AccountPublicService;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by houxianghua on 2018/11/15.
 */
@Service
@Transactional
@Slf4j
public class ImageClearingTypeApiServiceImpl implements ImageClearingTypeApiService {
    @Autowired
    private AccountsAllService accountsAllService;
    @Autowired
    private AccountPublicService accountPublicService;
    @Override
    public ResultDto changeImageStatus(String acctNo) {
        AccountsAllInfo accountsAllInfo = accountsAllService.findByAcctNo(acctNo);
        if (accountsAllInfo == null) {
            return ResultDtoFactory.toNack("acctNo无效");
        }
        if (!"1".equals(accountsAllInfo.getString003())) {
            return ResultDtoFactory.toNack("非存量数据");
        }
        if ("1".equals(accountsAllInfo.getString004())) {
            return ResultDtoFactory.toNack("该数据影像已补录");
        }
        accountsAllInfo.setString004("1");
        accountsAllService.save(accountsAllInfo);

        AccountPublicInfo api = accountPublicService.findByAcctNo(acctNo);
        if (api != null && "1".equals(api.getString003())
                && (api.getString004() == null || "0".equals(api.getString004()))) {
            api.setString004("1");
            accountPublicService.save(api);
        }
        return ResultDtoFactory.toAck();
    }
}

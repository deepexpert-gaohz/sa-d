package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.account.dto.AccountImageInfo;
import com.ideatech.ams.account.service.AccountImageService;
import com.ideatech.common.enums.CompanyIfType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description 对外的账户影像获取接口
 * @Author wanghongjie
 * @Date 2018/11/9
 **/
@Service
@Transactional
@Slf4j
public class AccountImageApiServiceImpl implements AccountImageApiService{
    @Autowired
    private AccountImageService accountImageService;

    @Override
    public List<AccountImageInfo> findTop10BySyncStatus() {
        return accountImageService.findTop10BySyncStatus(CompanyIfType.No);
    }
}

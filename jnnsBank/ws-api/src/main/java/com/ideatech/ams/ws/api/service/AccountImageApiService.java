package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.account.dto.AccountImageInfo;
import com.ideatech.common.enums.CompanyIfType;

import java.util.List;

/**
 * @Description 对外的账户影像获取接口
 * @Author wanghongjie
 * @Date 2018/11/9
 **/
public interface AccountImageApiService {
    /**
     *  获取10条未上传的影像记录
     * @return
     */
    List<AccountImageInfo> findTop10BySyncStatus();
}

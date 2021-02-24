package com.ideatech.ams.ws.api.service;

import com.ideatech.common.dto.ResultDto;

public interface DefaultSelectPwdService {

    /**
     * 存款人密码重置接口开放
     * @param accountKey
     * @param selectPwd
     * @param organCode
     * @return
     */
    ResultDto resetSelectPwd(String accountKey, String selectPwd, String organCode);
}

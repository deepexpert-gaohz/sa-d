package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.compare.service.CustomerSmsService;

/**
 * 默认实现客户异动-发送短信
 * @author jzh
 * @date 2019/6/28.
 */
public class DefaultCustomerSmsServiceImpl implements CustomerSmsService {
    @Override
    public boolean sendMessage(String phone, String messsage) {
        if (phone.equals("18812345678")){//模拟发送成功
            return true;
        }
        return false;
    }
}

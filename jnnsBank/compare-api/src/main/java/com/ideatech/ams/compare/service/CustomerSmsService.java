package com.ideatech.ams.compare.service;

/**
 * 客户异动-发送短信
 * @author jzh
 * @date 2019/6/28.
 */
public interface CustomerSmsService {

    /**
     * 发送短信
     * @param phone
     * @param messsage
     * @return false 发送失败；true 发送成功
     */
    boolean sendMessage(String phone,String messsage);
}

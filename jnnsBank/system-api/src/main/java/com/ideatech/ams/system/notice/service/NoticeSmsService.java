package com.ideatech.ams.system.notice.service;

/**
 * 到期超期短信提醒接口
 * @author jzh
 * @date 2019/5/6.
 */
public interface NoticeSmsService {
    /**
     * 发送短信
     * @param phone
     * @param messsage
     * @return
     */
    String sendMessage(String phone,String messsage);
}

package com.ideatech.ams.image.service;

/**
 * @author jzh
 * @date 2019-12-03.
 */
public interface ImageSmsService {

    /**
     * 发送远程双录短信
     * @param phone
     * @param messsage
     * @return
     */
    void sendMessage(String phone,String messsage);
}

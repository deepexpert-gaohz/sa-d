package com.ideatech.ams.apply.service;


/**
 * 短信接口
 * 
 * @author cxf
 * @company ydrx
 * @date Jan 5, 2018 10:28:45 AM
 */
public interface SmsService {

  /**
   * 发送预约人短信
   * @param phone
   * @param messsage
   * @return
   */
  String sendMessage(String phone,String messsage);

}

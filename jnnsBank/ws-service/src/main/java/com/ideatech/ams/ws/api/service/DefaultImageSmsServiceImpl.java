package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.image.service.ImageSmsService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jzh
 * @date 2019-12-03.
 */

@Slf4j
public class DefaultImageSmsServiceImpl implements ImageSmsService {
    @Override
    public void sendMessage(String phone, String messsage) {
        log.info(phone+":"+messsage);
    }
}

package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.image.dto.ImageVideoDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author jzh
 * @date 2020/5/7.
 */

@Slf4j
public class DefaultImageRecordApiServiceImpl implements ImageRecordApiService {
    @Override
    public String createNo() {
        return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + RandomStringUtils.randomNumeric(6);
    }

    @Override
    public void syncRecord(ImageVideoDto imageVideoDto) {
        log.info("同步远程双录视频开始【需现场实现】");
        log.info("双录视频信息[{}]",imageVideoDto);
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("同步远程双录视频结束【需现场实现】");
    }
}

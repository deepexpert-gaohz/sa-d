package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.image.dto.ImageVideoDto;

/**
 * 双录相关接口
 * @author jzh
 * @date 2020/5/7.
 */
public interface ImageRecordApiService {
    /**
     * 生成双录视频唯一编号
     * @return
     */
    String createNo();

    /**
     * 下载双录视频，并上传影像平台
     * @param imageVideoDto
     */
    void syncRecord(ImageVideoDto imageVideoDto);
}

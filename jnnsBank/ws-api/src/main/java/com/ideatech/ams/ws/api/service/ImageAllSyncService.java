package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.image.dto.ImageAllInfo;
import com.ideatech.ams.image.dto.ImageAllSyncDto;

import java.util.List;

/**
 * 影像上报接口
 */
public interface ImageAllSyncService {
    /**
     * 人行上报接口（需现场实现），data是数据
     * @throws Exception
     */
    void sync(List<ImageAllSyncDto> data) throws Exception;

    /**
     * 上传到影像平台接口（现场实现）
     * @param data
     *  @throws
     * @return 返回批次号，如果为空则表示上传失败
     */
    String syncToImage(ImageAllInfo data) throws Exception;

    /**
     * 根据批次号去影像平台查询并返回URL(现场实现)
     * @param batchNumber 批次号
     * @return 可访问的url
     * @throws Exception
     */
    String selectToImage(String batchNumber) throws Exception;
}

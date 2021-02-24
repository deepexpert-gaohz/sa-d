package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.image.dto.ImageVideoDto;

import java.io.File;
import java.util.List;

/**
 * 视频与影像平台交互接口（现场实现该接口）
 */
public interface ImageVideoSyncService {
    /**
     * 同步到影像平台接口
     * @param data 同步数据 里面包含视频存储地址
     * @return 成功即返回批次号，失败返回空
     */
    String sync(ImageVideoDto data) throws Exception;

    /**
     * 下载影像平台视频
     * @param batchNumber 批次号
     * @return
     */
    List<File> download(String batchNumber);

    /**
     * 通过批次号查询url
     * @param batchNumber
     * @return
     */
    String searchUrl(String batchNumber);
}

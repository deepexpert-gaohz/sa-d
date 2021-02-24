package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.image.dto.ImageAllInfo;
import com.ideatech.ams.image.dto.ImageAllSyncDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class ImageAllSyncServiceImpl implements ImageAllSyncService{
    @Override
    public void sync(List<ImageAllSyncDto> data) throws Exception {
        log.info("人行影像上报成功");
    }

    @Override
    public String syncToImage(ImageAllInfo data) {
        log.info("上传到影像平台");
        return null;
    }

    @Override
    public String selectToImage(String batchNumber) throws Exception {
        log.info("影像平台查询url");
        return null;
    }
}

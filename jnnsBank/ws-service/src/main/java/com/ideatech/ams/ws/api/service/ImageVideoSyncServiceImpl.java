package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.image.dto.ImageVideoDto;
import com.ideatech.ams.ws.api.service.ImageVideoSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
public class ImageVideoSyncServiceImpl implements ImageVideoSyncService {

    @Override
    public String sync(ImageVideoDto data) throws Exception {
        log.info("同步视频到影像平台。。。。");
        return null;
    }

    @Override
    public List<File> download(String batchNumber) {
        log.info("下载影像平台视频。。。。");
        return null;
    }

    @Override
    public String searchUrl(String batchNumber) {
        log.info("查询影像平台访问url");
        return null;
    }
}

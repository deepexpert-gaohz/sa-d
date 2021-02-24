package com.ideatech.ams.pbc.service;

import org.springframework.stereotype.Service;

/**
 * @Author yang
 * @Date 2019/11/20 15:29
 * @Version 1.0
 */
@Service
public class PbcSyncConfigServiceImpl implements PbcSyncConfigService {
    @Override
    public boolean isJibenSync() {
        return false;
    }

    @Override
    public boolean isHtmlLog() {
        return false;
    }

    @Override
    public Long getStopDate() {
        return 2000L;
    }
}

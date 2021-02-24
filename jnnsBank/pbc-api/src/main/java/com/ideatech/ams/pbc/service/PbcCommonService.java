package com.ideatech.ams.pbc.service;

import org.springframework.stereotype.Service;

/**
 * @Author yang
 * @Date 2019/11/20 17:03
 * @Version 1.0
 */
@Service
public interface PbcCommonService {
    /**
     * 记录html
     * @param logflag
     * @param html
     */
     void writeLog(String logflag, String html);
}

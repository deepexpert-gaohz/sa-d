package com.ideatech.ams.ws.api.service;

public interface AmsPushCoreService {

    /**
     * 检查是否核准并推送核心
     */
    void checkAndPushCore();

    /**
     * 取消核准推送核心
     */
    void checkAndCancelHeZhunPushCore();
}

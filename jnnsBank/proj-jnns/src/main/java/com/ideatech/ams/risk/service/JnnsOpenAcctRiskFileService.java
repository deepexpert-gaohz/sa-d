package com.ideatech.ams.risk.service;

/**
 * 开户风险监测数据获取服务
 */
public interface JnnsOpenAcctRiskFileService {

    /**
     * 获取开户风险监测数据
     *
     * @param date
     */
    void pullOdsFile(String date);

}

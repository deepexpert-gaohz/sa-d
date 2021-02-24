package com.ideatech.ams.annual.service;

public interface AnnualStatisticsService {

    void saveStatistics(Long taskId);

    /**
     * 更新统计结果成功数
     * @param taskId
     * @param organCode
     * @param type
     */
    void updateStatisticsSuccess(Long taskId, String organCode, String type);

    void subStatisticsCount(Long taskId, String orgCode);
}

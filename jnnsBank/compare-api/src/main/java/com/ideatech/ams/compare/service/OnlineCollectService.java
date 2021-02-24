package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.enums.CollectType;

/**
 * @Description 比对管理--在线采集接口
 * @Author wanghongjie
 * @Date 2019/2/11
 **/
public interface OnlineCollectService {
    /**
     * 采集
     * @param collectType
     * @param annualTaskId
     */
    void collect(CollectType collectType, Long annualTaskId);

    /**
     * 清理线程
     */
    void clearFuture();

    /**
     * 判断线程是否结束
     * @param taskId
     * @throws Exception
     */
    void valiCollectCompleted(final Long taskId) throws Exception;
}

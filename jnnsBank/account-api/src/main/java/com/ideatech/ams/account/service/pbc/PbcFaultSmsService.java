package com.ideatech.ams.account.service.pbc;

import com.alibaba.fastjson.JSONArray;

/**
 * 上报失败短信提醒接口
 */
public interface PbcFaultSmsService {
    /**
     * 上报失败提醒短信
     *
     * @param statisticsJson 各个机构的统计数据json
     *                       code：核心机构号
     *                       name：机构名称
     *                       telephone：电话号码
     *                       createdDate：日期
     *                       allNum：当日增量业务数
     *                       pbcNumAll：需上报人行
     *                       pbcNum：上报人行成功数
     *                       eccsNumAll：需上报信用代码数
     *                       eccsNum：上报信用代码成功数
     */
    String sendMessage(JSONArray statisticsJson);
}

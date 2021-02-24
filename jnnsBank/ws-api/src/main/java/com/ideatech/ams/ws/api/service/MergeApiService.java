package com.ideatech.ams.ws.api.service;


import com.ideatech.common.dto.ResultDto;

/**
 * 合并接口
 */
public interface MergeApiService {

    /**
     * 工商人行合并接口
     *
     * @param saicParam (工商注册号)(注册号)(名称)
     * @param accountKey 基本户开户许可证
     * @return
     */
    ResultDto querySaicAndPbc(String saicParam, String accountKey, String organCode);

    /**
     * 工商人行合并接口(包含地区代码)
     *
     * @param saicParam
     * @param accountKey
     * @param regAreaCode
     * @return
     */
    ResultDto querySaicAndPbc(String saicParam, String accountKey, String organCode, String regAreaCode);

}

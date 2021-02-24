package com.ideatech.ams.ws.api.service;


import com.ideatech.common.dto.ResultDto;

/**
 * 黑名单校验接口
 */
public interface BlacklistValidationService {
    /**
     * 检查黑名单
     * @param name 姓名
     * @param bod 生日
     * @param nationality 国籍
     * @return code为ACK表示调用成功，message为校验结果，code为NACK或者其他表示调用失败，message为错误信息
     */
    ResultDto check(String name, String bod, String nationality);
}

package com.ideatech.ams.image.service;

import java.util.Map;

/**
 * @author jzh
 * @date 2019-12-26.
 */
public interface FaceRecognitionService {

    /**
     * 获取人脸识别认证地址
     * @param name 姓名
     * @param identificationNumber 省份证号
     * @param successRedirect 认证成功跳转
     * @param failRedirect 认证失败跳转
     * @return
     */
    Map<String,Object> getVerifyUrl(String name, String identificationNumber, String successRedirect, String failRedirect);

    /**
     * 获取人脸识别结果
     * @param id
     * @return
     */
    Map<String,Object> getVerifyResult(String id);
}

package com.ideatech.ams.system.sm4.service;

public interface EncryptPwdService {
    /**
     * 是否启用SM4加密方式进行密码加密
     * @param password
     * @return
     */
    String encryptPwd(String password);

    /**
     * 是否启用SM4加密方式进行密码加密
     * @param password
     * @return
     */
    String decryptEcbPwd(String password) throws Exception;

}

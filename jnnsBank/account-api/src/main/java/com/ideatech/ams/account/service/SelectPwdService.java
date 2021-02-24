package com.ideatech.ams.account.service;


public interface SelectPwdService {
    /**
     * 存款人查询查询密码重置
     * @param accountKey
     * @param pwd
     * @throws Exception
     */
    String[] resetSelectPwd(String accountKey, String pwd);
}

package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.dto.auth.LoginAuth;

/**
 * 基本存款账户查询密码重置
 */
public interface AmsSelectPwdResetService {
    /**
     *
     * @param accountKey 基本存款账户编号
     * @param pwd 密码
     * @return 密码重置后打印信息
     */
    String[] resetSelectPwd(LoginAuth auth,String accountKey, String pwd) throws Exception;
}

package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;

/**
 * 已开立其他银行的账户情况查询
 */
public interface AmsOpenAccSearchService {
    /**
     * 第一步》》已开立其他银行账户情况查询
     */
    void openAccSearch(LoginAuth auth, AllAcct condition)throws  Exception;

    /**
     * d第二步》》已开立其他银行账户情况查询－>存款人密码检验并返回打印信息
     * @param auth
     * @param condition
     * @throws Exception
     */
    void checkSdepPassword(LoginAuth auth, AllAcct condition)throws  Exception;
}

package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.dto.AmsFeilinshiSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;

public interface AmsFeilinshiOpenBeiAnService {
    /**
     * 非临时机构临时存款账户备案制开户第一步---校验是否满足开户条件
     *
     * @param auth
     * @return
     * @throws SyncException
     * @throws Exception
     */
    void openAccountFirstStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws  Exception;

    /**
     * 非临时机构临时存款账户备案制开户第二步---信息核对
     * @param auth
     * @param condition
     * @throws Exception
     */
    void openAccountSecondStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception;

    /**
     * 非临时机构临时存款账户备案制开户第三步---核发开户信息
     * @param auth
     * @param condition
     * @throws Exception
     */
    void openAccountThirdStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception;

    /**
     * 非临时机构临时存款账户备案制开户第四步---打印信息
     * @param auth
     * @param condition
     * @throws Exception
     */
    void openAccountLastStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception;

    /**
     * 非临时第一步参数 - 征询通用
     * @param allAcct
     * @return
     * @throws SyncException
     * @throws Exception
     */
    String getFirstBodyUrlParmsByFirstStep(AmsFeilinshiSyncCondition allAcct) throws SyncException, Exception;
}

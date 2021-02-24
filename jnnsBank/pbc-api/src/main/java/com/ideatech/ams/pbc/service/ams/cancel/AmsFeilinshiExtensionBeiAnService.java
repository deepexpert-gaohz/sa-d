package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.dto.AmsFeilinshiSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;

/**
 * 非临时户展期
 */
public interface AmsFeilinshiExtensionBeiAnService {
    /**
     * 非临时机构临时存款账户备案制展期第一步---校验是否满足变更条件
     * @param auth
     * @param condition
     * @throws Exception
     */
    void openAccountFirstStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception;
    /**
     * 非临时机构临时存款账户备案制展期第二步---信息录入
     * @param auth
     * @param condition
     * @throws Exception
     */
    void openAccountSecondStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception;
    /**
     * 非临时机构临时存款账户备案制展期第三步---完成
     * @param auth
     * @param condition
     * @throws Exception
     */
    void openAccountLastStep(LoginAuth auth, AmsFeilinshiSyncCondition condition) throws Exception;

}

package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 *取消核准的基本户变更
 * @author yang
 */
public interface AmsJibenChangeBeiAnService {


    /**
     * 基本户备案制变更第一步---校验是否满足变更条件
     *
     * @param auth
     * @param
     * @return
     * @throws SyncException
     * @throws Exception
     */
    void changeAccountFirstStep(LoginAuth auth, AllAcct condition) throws SyncException, Exception;

    /**
     * 基本户备案制变更第二步---变更核对信息
     * @param auth
     * @param condition
     * @return
     * @throws SyncException
     * @throws Exception
     */
    void changeAccountSecondStep(LoginAuth auth, AllAcct condition) throws SyncException, Exception;

    /**
     * 基本户备案制变更第二步---变更成功
     * @param auth
     * @param condition
     * @throws SyncException
     * @throws Exception
     */
    void changeAccountLastStep(LoginAuth auth, AllAcct condition) throws SyncException, Exception;

    /**
     * 基本户备案制变更查询获取页面字段信息
     *
     * @param auth
     * @param
     * @return
     * @throws SyncException
     * @throws Exception
     */
    AmsAccountInfo getAmsAccountInfoFromChangeAccountFirstStep(LoginAuth auth, AllAcct condition) throws SyncException, Exception;
}

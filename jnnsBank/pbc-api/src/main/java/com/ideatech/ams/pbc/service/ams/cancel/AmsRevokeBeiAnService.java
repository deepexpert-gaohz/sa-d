package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsRevokeBeiAnSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;

public interface AmsRevokeBeiAnService {
    /**
     * 取消核准销户第一步---校验是否满足销户条件
     *
     * @param auth
     * @return
     * @throws SyncException
     * @throws Exception
     */
    void revokeAccountFirstStep(LoginAuth auth, AmsRevokeBeiAnSyncCondition condition) throws SyncException, Exception;

    /**
     * 取消核准销户第二步 当销户原因时需要打印账户信息
     * @param auth
     * @param condition
     * @return
     * @throws SyncException
     * @throws Exception
     */
    void revokeAccountSecondStep(LoginAuth auth, AmsRevokeBeiAnSyncCondition condition) throws SyncException, Exception;

    /**
     * 取消核准销户第一步---校验是否满足销户条件
     *
     * @param auth
     * @return
     * @throws SyncException
     * @throws Exception
     */
    AmsAccountInfo getAmsAccountInfoRevokeAccountFirstStep(LoginAuth auth, AmsRevokeBeiAnSyncCondition condition) throws SyncException, Exception;


}

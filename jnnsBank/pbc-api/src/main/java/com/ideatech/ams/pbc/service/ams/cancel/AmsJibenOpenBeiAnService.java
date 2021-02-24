package com.ideatech.ams.pbc.service.ams.cancel;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;

/**
 * 取消核准后基本开户分为三步，三步同时满足则开户成功
 */
public interface AmsJibenOpenBeiAnService {
    /**
     * 基本户备案制开户第一步---校验是否满足开户条件
     *
     * @param auth
     * @return
     * @throws SyncException
     * @throws Exception
     */
    void openAccountFirstStep(LoginAuth auth, AllAcct condition) throws  Exception;

    /**
     * 基本户备案制开户第二步---开始录入基本信息并提示开户成功
     * @param auth
     * @param condition
     * @return
     * @throws SyncException
     * @throws Exception
     */
    void openAccountSecondStep(LoginAuth auth, AllAcct condition) throws Exception;
    /**
     * 基本户备案制开户最后一步---信息核对页面点击确定
     *
     * @param auth
     * @throws Exception
     * @throws SyncException
     */
    void openAccountLastStep(LoginAuth auth, AllAcct condition) throws  Exception;

    /**
     * 基本户核对页面确定之后执行这一步获取打印信息
     * @param auth
     * @return
     */
    void getPrintInfo(LoginAuth auth, AllAcct condition)throws  Exception;


    /**
     * 获取第一步body内容，征询通用
     * @param condition
     * @return
     */
    String getFirstBodyUrlParmsByFirstStep(AllAcct condition) throws SyncException, Exception;
}

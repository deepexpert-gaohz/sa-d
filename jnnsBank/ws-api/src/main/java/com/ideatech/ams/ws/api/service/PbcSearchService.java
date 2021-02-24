package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.pbc.dto.AmsJibenUniqueCheckCondition;
import com.ideatech.common.dto.ResultDto;

public interface PbcSearchService {
    /**
     * 根据三个参数查询查询该基本户的所有账户情况
     * @param depositorName 存款人名称
     * @param accountKey 基本存款账户开户许可证核准号
     * @param selectPwd 存款人查询密码
     * @param organCode 机构代码
     * @return
     */
    ResultDto searchAllAccount(String depositorName, String accountKey, String selectPwd, String organCode);

    /**
     * 根据三个参数查询查询该基本户的所有账户情况
     * @param depositorName 存款人名称
     * @param accountKey 基本存款账户开户许可证核准号
     * @param selectPwd 存款人查询密码
     * @param organCode 机构代码
     * @param username 用户名
     * @return
     */
    ResultDto searchAllAccount(String depositorName, String accountKey, String selectPwd, String organCode, String username);


    /**
     * 基本户唯一性校验接口
     * @param amsJibenUniqueCheckCondition
     * @param organCode
     * @return
     */
    ResultDto jiBenUniqueCheck(AmsJibenUniqueCheckCondition amsJibenUniqueCheckCondition, String organCode);

    /**
     * 基本户补打校验
     * @param
     * @param
     * @return
     */
    ResultDto jiBenResetDetails(String acctNo,String pbcCode);

    /**
     * 基本户补打反显新的编号以及注册号
     * @param
     * @param
     * @return
     */
    ResultDto jiBenResetPrint(String acctNo,String pbcCode);

}

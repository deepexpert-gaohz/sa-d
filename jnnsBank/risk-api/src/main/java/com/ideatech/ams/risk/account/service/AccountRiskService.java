package com.ideatech.ams.risk.account.service;

import com.ideatech.ams.risk.account.dto.AccountTransactionRiskSearchDto;


/**
 * @Author: yinjie
 * @Date: 2019/5/9 17:06
 */
public interface AccountRiskService {

    /**
     * @author:yinjie
     * @date:2019/5/11
     * @time:11:14
     * @description:
     * 账户风险监测---账户交易风险监测
     * 可携带搜索条件的表格数据查询
     */
    AccountTransactionRiskSearchDto queryAccountRisk(AccountTransactionRiskSearchDto accountTransactionRiskSearchDto);

}

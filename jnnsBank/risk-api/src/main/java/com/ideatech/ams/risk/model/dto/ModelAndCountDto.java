package com.ideatech.ams.risk.model.dto;

import lombok.Data;

/**
 * @Author: yinjie
 * @Date: 2019/4/30 9:46
 */

@Data
public class ModelAndCountDto {

    private String  id;
    private String riskDate;//数据日期
    private String riskId;//风险点编号
    private String bankCode;//开户机构号
    private String bankName;//开户机构名称
    private String accountNo;//账户号
    private String accountName;//账户名称
    private String riskCnt;//数据条数
    private String customerNo;//客户号
    private String modelName;//模型名称
    private String flowName;//流程名称
    private String key;//流程标识
    private String status;//流程状态
    private String accountStatus;//账户状态
    private String accCreateDate;//开户日期
    public ModelAndCountDto( String id, String riskDate, String riskId, String bankCode, String bankName, String accountNo, String accountName,
                             String riskCnt, String customerNo, String modelName, String flowName, String key, String status,String accountStatus,String accCreateDate) {
        this.id = id;
        this.riskDate = riskDate;
        this.riskId = riskId;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.accountNo = accountNo;
        this.accountName = accountName;
        this.riskCnt = riskCnt;
        this.customerNo = customerNo;
        this.modelName = modelName;
        this.flowName = flowName;
        this.key = key;
        this.modelName = modelName;
        this.status = status;
        this.accountStatus = accountStatus;
        this.accCreateDate = accCreateDate;
    }

}

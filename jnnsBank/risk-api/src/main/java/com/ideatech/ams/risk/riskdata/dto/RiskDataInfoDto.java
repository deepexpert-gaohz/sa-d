package com.ideatech.ams.risk.riskdata.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RiskDataInfoDto {
    private Long id;
    private String riskDate;//监测日期
    private String bankName;//机构名称
    private String accountNo;//客户idN
    private String accountName;//客户名称
    private String customerNo;//客户号
    private String bankCode;//机构完整ID
    private String riskId;//风险编号
    private String riskCnt;//数量
    private String riskDesc;//风险描述
    private String accountStatus;//账户状态
    private String accCreateDate;//开户日期
    private String riskType;//风险类型
    private String finTel;//财务主管电话
    private String legTel;//法人电话
    private String operTel;//经办人电话
    private String riskPoint;
    private String name;//模型名称
    private String status;//
    private String ordNum;//序号
    private String depositorName;//账户名称
    private String handleDate;//处理时间
    private String handler;//处理时间
    private String handleMode;//处理方式 0： 不处理，1：止付，2：非柜面
    private String dubiousReason;//暂停非柜面可疑原因
    private String fullId;//机构层级

    private String organCode;//机构号
    public RiskDataInfoDto( Long id ,String accountNo,String customerNo,String riskDate, String accountName,String accCreateDate, String bankCode,
                            String bankName,String accountStatus,String riskDesc, String riskCnt,String riskType, String riskId) {
        this.id = id;
        this.accountNo = accountNo;
        this.customerNo = customerNo;
        this.riskDate = riskDate;
        this.accountName = accountName;
        this.accCreateDate = accCreateDate;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.accountStatus = accountStatus;
        this.riskDesc = riskDesc;
        this.riskCnt = riskCnt;
        this.riskType = riskType;
        this.riskId = riskId;
    }
}

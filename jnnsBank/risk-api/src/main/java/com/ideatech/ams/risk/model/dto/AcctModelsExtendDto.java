package com.ideatech.ams.risk.model.dto;


import lombok.Data;

@Data
public class AcctModelsExtendDto {
    /**
     * id
     */
    private Long id;
    /**
     * 账号
     */
    private String acctNo;
    /**
     * 账户名称
     */
    private String acctName;
    /**
     * 账户状态
     */
    private String accountStatus;
    /**
     * 账户开户日期
     */
    private String acctCreateDate;
    /**
     * 账户销户日期
     */
    private String cancelDate;
    /**
     * 开户银行金融机构编码
     */
    private String bankCode;
    /**
     * 开户银行名称
     */
    private String bankName;
    /**
     * 客户号
     */
    private String customerNo;
    /**
     * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7 非预算单位专用存款账户)
     */
    private String acctType;
    private String ordNum;//序号
    private String modelNum;//触发模型数量
    private String cjrq;//采集日期
    public AcctModelsExtendDto() {
    }

}

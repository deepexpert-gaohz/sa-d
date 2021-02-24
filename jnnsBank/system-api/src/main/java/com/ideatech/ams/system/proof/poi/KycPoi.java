package com.ideatech.ams.system.proof.poi;

import lombok.Data;

@Data
public class KycPoi {
    /**
     * 账号
     */
    private String acctNo;
    /**
     * 账户名称
     */
    private String acctName;

    private String acctTypeStr;

    /**
     * 开户行
     */
    private String openBankName;

    private String kycFlagStr;
    /**
     * 尽调机构
     */
    private String proofBankName;

    /**
     * 尽调人
     */
    private String username;
    /**
     * 最后一次尽调时间
     */
    private String dateTime;
}

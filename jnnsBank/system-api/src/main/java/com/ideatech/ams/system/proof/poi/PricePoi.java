package com.ideatech.ams.system.proof.poi;

import lombok.Data;

@Data
public class PricePoi {
    /**
     * 企业名称
     */
    private String entname;
    /**
     * 电话号码
     */
    private String phone;
    private String typeStr;
    /**
     *
     */
    private String proofBankName;
    /**
     *
     */
    private String username;
    /**
     *
     */
    private String dateTime;
    /**
     * 价格
     */
    private String price;
}

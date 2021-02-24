package com.ideatech.ams.poi;

import lombok.Data;

@Data
public class CustomerPoi {

    private String customerId;
    /**
     * 客户号
     */
    private String customerNo;

    /**
     *企业名称
     */
    private String depositorName;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构号
     */
    private String organCode;

}


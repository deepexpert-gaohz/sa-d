package com.ideatech.ams.annual.dto;

import lombok.Data;

/**
 * 电信运营商信息
 */
@Data
public class TelecomValidationSearchDto {
    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 客户号
     */
    private String customerNo;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 核心机构号
     */
    private String bankCode;

}

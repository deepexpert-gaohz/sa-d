package com.ideatech.ams.annual.dto;

import lombok.Data;

/**
 * 核心控股股东信息
 */
@Data
public class CoreStockHolderDto {
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
     * 控股股东名称
     */
    private String stockHolderName;

    /**
     * 控股股东持股比例
     */
    private Double stockHolderRatio;

    /**
     * 核心机构号
     */
    private String organCode;
}

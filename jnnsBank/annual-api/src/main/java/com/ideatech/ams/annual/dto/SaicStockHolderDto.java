package com.ideatech.ams.annual.dto;

import lombok.Data;

/**
 * 工商控股股东信息
 */
@Data
public class SaicStockHolderDto {
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
     * 核心控股股东名称
     */
    private String coreStockHolderName;

    /**
     * 核心控股股东持股比例
     */
    private Double coreStockHolderRatio;

    /**
     * 工商与核心是否一致
     */
    private Boolean isSame;

    /**
     * 核心机构号
     */
    private String organCode;
}

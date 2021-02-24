package com.ideatech.ams.annual.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 工商控股股东信息
 */
@Entity
@Data
@Table(name = "saic_stock_holder")
public class SaicStockHolderPo extends BaseMaintainablePo {
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

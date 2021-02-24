package com.ideatech.ams.annual.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 核心受益人及法人信息
 */
@Entity
@Table(name = "core_beneficiary")
@Data
public class CoreBeneficiaryPo extends BaseMaintainablePo {
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
     * 受益人名称1
     */
    private String beneficiaryName1;

    /**
     * 受益人比例1
     */
    private Double beneficiaryRatio1;

    /**
     * 受益人名称2
     */
    private String beneficiaryName2;

    /**
     * 受益人比例2
     */
    private Double beneficiaryRatio2;

    /**
     * 受益人名称3
     */
    private String beneficiaryName3;

    /**
     * 受益人比例3
     */
    private Double beneficiaryRatio3;

    /**
     * 受益人名称4
     */
    private String beneficiaryName4;

    /**
     * 受益人比例4
     */
    private Double beneficiaryRatio4;

    /**
     * 法人
     */
    private String legalName;

    /**
     * 核心机构号
     */
    private String organCode;
}

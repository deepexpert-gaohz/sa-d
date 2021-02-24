package com.ideatech.ams.annual.dto;

import com.ideatech.ams.kyc.dto.BeneficiaryDto;
import lombok.Data;

import java.util.List;

/**
 * 工商受益人及法人信息
 */
@Data
public class SaicBeneficiaryDto {
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
     * 核心受益人名称1
     */
    private String coreBeneficiaryName1;

    /**
     * 核心受益人比例1
     */
    private Double coreBeneficiaryRatio1;

    /**
     * 核心受益人名称2
     */
    private String coreBeneficiaryName2;

    /**
     * 核心受益人比例2
     */
    private Double coreBeneficiaryRatio2;

    /**
     * 核心受益人名称3
     */
    private String coreBeneficiaryName3;

    /**
     * 核心受益人比例3
     */
    private Double coreBeneficiaryRatio3;

    /**
     * 核心受益人名称4
     */
    private String coreBeneficiaryName4;

    /**
     * 核心受益人比例4
     */
    private Double coreBeneficiaryRatio4;

    /**
     * 核心法人
     */
    private String coreLegalName;

    /**
     * 工商与核心是否一致
     */
    private Boolean isSame;

    /**
     * 核心机构号
     */
    private String organCode;

    /**
     * 暂存受益人列表
     */
    private List<BeneficiaryDto> beneficiaryDtos;
}

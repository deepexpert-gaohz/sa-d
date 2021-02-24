package com.ideatech.ams.annual.dto;

import lombok.Data;

/**
 * 电信三要输验证
 */
@Data
public class TelecomValidationDto {
    private Long id;

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

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idNo;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 校验结果
     */
    private String result;

    /**
     * 机构fullId
     */
    private String organFullId;
}

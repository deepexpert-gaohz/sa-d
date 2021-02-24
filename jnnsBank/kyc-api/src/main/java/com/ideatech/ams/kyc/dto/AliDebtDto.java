package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class AliDebtDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 年龄
     */
    private String ageclean;

    /**
     * 省份
     */
    private String areanameclean;

    /**
     * 身份证号码/企业注册号
     */
    private String cardnumclean;

    /**
     * 贷款到期时间
     */
    private String dkdqsj;

    /**
     * 贷款期限
     */
    private String dkqx;

    /**
     * 欠贷人姓名/名称
     */
    private String inameclean;

    /**
     * 法定代表人
     */
    private String legalperson;

    /**
     * 欠款额度
     */
    private String qked;

    /**
     * 性别
     */
    private String sexyclean;

    /**
     * 淘宝账户
     */
    private String tbzh;

    /**
     * 违约情况
     */
    private String wyqk;

    /**
     * 身份证原始发地
     */
    private String ysfzd;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}
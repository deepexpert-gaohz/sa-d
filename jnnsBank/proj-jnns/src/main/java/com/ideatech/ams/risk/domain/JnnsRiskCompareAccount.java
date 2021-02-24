package com.ideatech.ams.risk.domain;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class JnnsRiskCompareAccount extends BaseMaintainablePo {

    /**
     * 风险ID
     */
    private String riskId;

    /**
     * 模型描述
     */
    private String mdesc;

    /**
     * 对账地址
     */
    private String dzAddress;

    /**
     * 办公地址
     */
    private String workAddress;

    /**
     * 对账电话
     */
    private String dzTelephone;

    /**
     * 账户名称
     */
    private String acctName;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 客户号
     */
    private String customerNo;

    /**
     * 对账月份  如 202011
     */
    private String compareMonth;

    /**
     * 对账标识
     * 0:未对账
     * 非0 已对账
     */
    private String reachType;

    /**
     * 数据来源方式
     * 1：纸质对账
     * 2：电子对账
     * 3：对账地址与经营地址
     */
    private String sourceType;

    /**
     * 核心机构号
     */
    private String organCode;
    /**
     * 机构FullId
     */
    private String organFullId;
}

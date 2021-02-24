package com.ideatech.ams.risk.highRisk.entity;


import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "yd_risk_high_rule")
@Data
public class HighRiskRule extends BaseMaintainablePo {

    /**
     * ID
     */
    @Id
    private Long id;

    private String ruleId;
    /**
     * 选择的模型规则
     */

    private String ruleModel;
    /**
     * 选择的外部接口
     */

    private String externalData;
    /**
     * 跑批状态
     */

    private String runState;


}

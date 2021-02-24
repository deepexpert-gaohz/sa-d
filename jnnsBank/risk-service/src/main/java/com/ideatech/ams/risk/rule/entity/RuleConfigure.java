package com.ideatech.ams.risk.rule.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "risk_rule_configuration")
//@SQLDelete(sql = "update yd_risk_rule_configuration set yd_deleted=1 where yd_id=? and yd_version_ct=?")
//@Where(clause = "yd_deleted = 0")
public class RuleConfigure extends BaseMaintainablePo {
    private String ruleId;//配置规则id

    private String condition;//条件

    private String value;//值

    private String conAndVal;//拼装后的值

    private Boolean deleted = Boolean.FALSE;//删除标记

}

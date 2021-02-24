package com.ideatech.ams.risk.rule.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "risk_rule_field")
//@SQLDelete(sql = "update yd_risk_rule_field set yd_deleted=1 where yd_id=? and yd_version_ct=?")
//@Where(clause = "yd_deleted = 0")
public class RuleField extends BaseMaintainablePo {

    private String modelId;
    private String modelName;
    private String field;
    private String fieldName;
    private Boolean deleted = Boolean.FALSE;//删除标记


}

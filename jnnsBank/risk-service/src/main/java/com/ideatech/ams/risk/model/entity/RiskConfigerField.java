package com.ideatech.ams.risk.model.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "risk_configer_field")
public class RiskConfigerField extends BaseMaintainablePo {
    @Id
    @Column(name = "yd_id")
    private Long id;
    private String field;
    private String fieldName;
}

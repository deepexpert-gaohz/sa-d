package com.ideatech.ams.risk.model.entity;


import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="yd_risk_tjjg")
public class YdRiskTjjg{
    @Id
   // @Column(name = "YD_ORG_ID")
    private String orgId;
  //  @Column(name = "YD_CJRQ")
    private String cjrq;
  //  @Column(name = "YD_TJJG")
    private String tjjg;
   // @Column(name = "YD_RISK_TABLE")
    private String riskTable;
  //  @Column(name = "YD_RISK_ID")
    private String riskId;

}

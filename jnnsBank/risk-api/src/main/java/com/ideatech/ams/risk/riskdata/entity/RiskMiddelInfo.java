package com.ideatech.ams.risk.riskdata.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="risk_middle_info")
@Getter
@Setter
@Where(clause = "yd_deleted = 0")
public class RiskMiddelInfo  extends BaseMaintainablePo {

    private String riskId;   //风险编号
    private String riskDesc;   //风险描述
    private String riskCnt;   //风险数量
    private String riskType;   //风险类型
    private String customerId;   //客户号
    private Date riskDate;   //风险日期
    private String accountNo;   //账号
    private String riskStatus;//账户类型
    private Boolean deleted = Boolean.FALSE;//删除标记
}

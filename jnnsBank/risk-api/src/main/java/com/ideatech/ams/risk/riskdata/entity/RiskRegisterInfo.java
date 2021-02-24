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

/**
 * 风险数据处理登记簿
 * @author yangcq
 * @dae 20190714
 * @address nanj
 */
@Entity
@Table(name="risk_register_info")
@Getter
@Setter
@Where(clause = "yd_deleted = 0")
public class RiskRegisterInfo  extends BaseMaintainablePo {
    @Id
    @Column(name = "yd_id")
    private Long id;
    private String riskId;   //风险编号
    private String riskDesc;   //风险描述
    private Date riskDate;   //风险日期
    private String accountNo;   //账号
    private String riskPoint;//风险点
    private String handleMode;//处理方式
    private Date handleDate;//处理时间
    private String status;//处理状态
    private String handler;//处理人
    private String dubiousReason;//暂停非柜面可疑原因
    private Boolean deleted = Boolean.FALSE;//删除标记
}

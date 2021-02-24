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
 * 风险数据待处理表
 * @author yangcq
 * @dae 20190622
 * @address wulmq
 */
@Entity
@Table(name="risk_handle_info")
@Getter
@Setter
@Where(clause = "yd_deleted = 0")
public class RiskHandleInfo  extends BaseMaintainablePo {
    @Id
    @Column(name = "yd_id")
    private Long id;
    private String riskId;   //风险编号
    private String riskDesc;   //风险描述
    private Date riskDate;   //风险日期
    private String accountNo;   //账号
    private String riskPoint;//风险点
    private String status;//风险状态
    private Boolean deleted = Boolean.FALSE;//删除标记
}

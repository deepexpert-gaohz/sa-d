package com.ideatech.ams.risk.riskdata.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 账户对账风险数据加工结果表
 *
 * @author yangcq
 * @date 20190708
 * @address nanj
 */
@Entity
@Table(name = "risk_check_info")
@Data
public class RiskCheckInfo extends BaseMaintainablePo {
    @Id
    @Column(name = "yd_id")
    private Long id;
    private String riskId;   //风险编号
    private String riskDesc;   //风险描述
    private String riskType;   //风险类型
    private String customerId;   //客户号
    private Date riskDate;   //风险日期
    private String accountNo;   //账号
    private String status;//流程状态
    private String riskPoint;//风险点
    private String serialId;//交易流水号
    private Boolean deleted = Boolean.FALSE;//删除标记
    private String riskCnt;



    /**
     * 核心机构号
     */
    private String organCode;
    /**
     * 机构FullId
     */
    private String organFullId;
    /**
     * 数据来源方式
     * 1：纸质对账
     * 2：电子对账
     */
    private String sourceType;
}

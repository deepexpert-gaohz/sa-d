package com.ideatech.ams.risk.riskdata.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 存储从核心获取的数据用于进行
 * 1.新开户15天内发生100万以上交易，
 * 2.印鉴卡变更15天内发生50万以上交易的）
 * 两个交易的监测
 */
@Entity
@Table(name="risk_core_info")
@Data
public class RiskCoreInfo  extends BaseMaintainablePo {
    @Id
    @Column(name = "yd_id")
    private Long id;
    private String riskId;   //风险编号
    private String tradeDate;//交易日期
    private String tradeTime;//交易时间
    private String accountNo;//账号
    private String serialId;//流水ID
    private String amount;//发生额
    private String openDate;//开户日期
    private String startDate;//印鉴卡启用日期
    private String flag;//判断是否为印鉴卡
    private String serialDate;//流水日期
    private String code;//机构号
    private String organFullId;//完整机构号
}

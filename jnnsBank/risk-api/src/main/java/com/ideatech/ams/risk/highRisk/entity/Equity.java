package com.ideatech.ams.risk.highRisk.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yangwz
 * @Description 股权出质登记信息
 * @date 2019-10-16 11:01
 */
@Entity
@Table(name = "risk_external_equity")
@Data
public class Equity extends BaseMaintainablePo {
    /**
     * 关键字name
     */
    private String keyName;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 登记编号
     */
    private String registNumber;
    /**
     * 出质人
     */
    private String thePledgee;
    /**
     * 出质股权数额
     */
    private String pledgeOfStockRights;
    /**
     * 质权人
     */
    private String pledgor;
    /**
     * 股权出质设立登记日期
     */
    private String pledgeDate;
    /**
     * 状态
     */
    private String status;


}

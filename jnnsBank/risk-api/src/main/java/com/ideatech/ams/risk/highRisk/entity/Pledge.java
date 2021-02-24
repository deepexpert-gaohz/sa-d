package com.ideatech.ams.risk.highRisk.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yangwz
 * @Description动产抵押登记信息
 * @date 2019-10-16 11:15
 */

@Entity
@Table(name = "risk_external_pledge")
@Data
public class Pledge extends BaseMaintainablePo {
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
     * 登记日期
     */
    private String registDate;
    /**
     * 登记机关
     */
    private String registOrgan;
    /**
     * 被担保债权数额
     */
    private String amountOfSecuredClaim;
    /**
     * 公示日期
     */
    private String announcementDate;
    /**
     * 状态
     */
    private String status;
    /**
     * 详情
     */
    private String details;


}

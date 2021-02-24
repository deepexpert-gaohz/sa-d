package com.ideatech.ams.risk.highRisk.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yangwz
 * @Description企业经营异常信息
 * @date 2019-10-16 11:27
 */
@Entity
@Table(name = "risk_external_anomalya")
@Data
public class Anomalya extends BaseMaintainablePo {

    /**
     * 关键字name
     */
    private String keyName;
    /**
     * 单位名称
     */
    private String name;
    /**
     * 统一社会信用代码
     */
    private String unityCreditCode;
    /**
     * 异常类型
     */
    private String type;
    /**
     * 列入原因
     */
    private String inReason;
    /**
     * 列入日期
     */
    private String inDate;
    /**
     * 列入决定机构
     */
    private String inOrgan;
    /**
     * 移出原因
     */
    private String outReason;
    /**
     * 移出日期
     */
    private String outDate;
    /**
     * 移出决定机构
     */
    private String outOrgan;

    private String organFullId;//机构fullId
    private String corporateBank;//法人机构行标识

    /**
     * 组织机构代码
     */
    private String orgCode;
}

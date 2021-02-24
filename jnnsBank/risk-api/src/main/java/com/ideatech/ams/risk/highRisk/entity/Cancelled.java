package com.ideatech.ams.risk.highRisk.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yangwz
 * @Description企业简易注销公告
 * @date 2019-10-16 11:34
 */
@Entity
@Table(name = "risk_external_cancelled")
@Data
public class Cancelled extends BaseMaintainablePo {
    /**
     * 关键字name
     */
    private String keyName;
    /**
     * 企业名称
     */
    private String name;
    /**
     * 统一社会信用代码
     */
    private String unityCreditCode;
    /**
     * 注册号
     */
    private String registNo;
    /**
     * 登记机关
     */
    private String registOrgan;
    /**
     * 公告期
     */
    private String publicDate;
    /**
     * 全体投资人承诺书Url
     */
    private String docUrl;
    /**
     * 异常申请人内容
     */
    private String dissentInfos;
    /**
     * 简易注销内容
     */
    private String cancellationResults;

}

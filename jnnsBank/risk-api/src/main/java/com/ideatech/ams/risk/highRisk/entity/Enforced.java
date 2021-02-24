package com.ideatech.ams.risk.highRisk.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yangwz
 * @Description 被执行信息
 * @date 2019-10-16 10:38
 */
@Entity
@Table(name = "risk_external_enforced")
@Data
public class Enforced extends BaseMaintainablePo {
    /**
     * 关键字name
     */
    private String keyName;
    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 被执行姓名
     */
    private String pname;
    /**
     * 立案时间
     */
    private String sortTime;
    /**
     * 案号
     */
    private String caseNo;
    /**
     * 执行法院名称
     */
    private String court;
    /**
     * 执行标的
     */
    private String execMoney;
    /**
     * 身份证/组织机构代码
     */
    private String idcardNo;

}

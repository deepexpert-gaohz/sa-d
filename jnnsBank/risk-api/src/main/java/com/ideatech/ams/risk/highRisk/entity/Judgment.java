package com.ideatech.ams.risk.highRisk.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yangwz
 * @Description 裁判文书
 * @date 2019-10-16 10:24
 */
@Entity
@Table(name = "risk_external_judgment")
@Data
public class Judgment extends BaseMaintainablePo {

    /**
     * 关键字name
     */
    private String keyName;
    /**
     * 执行法院
     */
    private String court;

    /**
     * 裁判文书编号
     */
    private String caseNo;
    /**
     * 裁判文书类型
     */
    private String caseType;
    /**
     * 提交时间
     */
    private String submitDate;
    /**
     * 修改时间
     */
    private String updateDate;
    /**
     * 是否原告
     */
    private String isProsecutor;
    /**
     * 是否被告
     */
    private String isDefendant;
    /**
     * 开庭时间年份
     */
    private String courtYear;

    /**
     * 涉案人员角色
     */
    @Column(length = 1000)
    private String caseRole;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 裁判文书标题
     */
    private String title;
    /**
     * 审结时间
     */
    private String sortTime;
    /**
     * 内容
     */
    private String body;
    /**
     * 案由
     */
    private String caseCause;
    /**
     * 裁决结果
     */
    private String judgeResult;
    /**
     * 依据
     */
    private String yiju;
    /**
     * 审判员
     */
    private String judge;
    /**
     * 审理程序
     */
    private String trialProcedure;
}

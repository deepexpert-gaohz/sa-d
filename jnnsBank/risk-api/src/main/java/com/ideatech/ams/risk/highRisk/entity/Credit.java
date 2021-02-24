package com.ideatech.ams.risk.highRisk.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yangwz
 * @Description 失信信息
 * @date 2019-10-16 10:43
 */
@Entity
@Table(name = "risk_external_credit")
@Data
public class Credit extends BaseMaintainablePo {
    /**
     * 关键字name
     */
    private String keyName;
    /**
     * 所在省份缩写
     */
    private String province;

    /**
     * 生效法律文书确定的义务
     */
    private String yiwu;
    /**
     * 记录更新时间
     */
    private String updatedate;
    /**
     * 已履行
     */
    private String performedpart;
    /**
     * 未履行
     */
    private String unperformpart;
    /**
     * 组织类型（1：自然人，2：企业，3：社会组织，空白：无法判定）
     */
    private String orgType;
    /**
     * 组织类型名称
     */
    private String orgTypeName;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 被执行人姓名
     */
    private String pname;
    /**
     * 立案时间
     */
    private String sortTime;
    /**
     * 执行法院名称
     */
    private String court;
    /**
     * 被执行人的履行情况
     */
    private String lxqk;
    /**
     * 执行依据文号
     */
    private String yjCode;
    /**
     * 身份证/组织机构代码证
     */
    private String idcardNo;
    /**
     * 做出执行依据单位
     */
    private String yjdw;
    /**
     * 失信被执行人行为具体情形
     */
    private String jtqx;
    /**
     * 案号
     */
    private String caseNo;
    /**
     * 发布时间
     */
    private String postTime;
    /**
     * 法定代表人或者负责人姓名
     */
    private String ownerName;

}

package com.ideatech.ams.risk.account.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "account_all_data")
@Data
public class AccountAllData {
    @Id
    @Column(name = "yd_id")
    @GenericGenerator(name = "ideaIdGenerator", strategy = "com.ideatech.common.entity.id.IdeaIdGenerator")
    @GeneratedValue(generator = "ideaIdGenerator")
    private Long id;
    private String acctNo;    //账号
    private String acctType; //账户性质
    private String acctName;//	账户名称
    private String accountStatus; //	账户状态
    private String acctCreateDate; //	账户开户日期
    private String effectiveDate; //	账户有效期(临时账户)
    private String accountKey; //	开户许可证号
    private String organFullId; //	完整机构fullId
    private String organCode;//核心机构号
    private String bankName; //	开户银行名称
    private String customerNo; //	客户号
    private String depositorName; //	存款人名称
    private String depositorType; //	存款人类别
    private String regProvince; //	注册省份
    private String regCity; //	注册城市
    private String regArea; //	注册地区
    private String regAddress; //	注册详细地址
    private String industryCode; //	行业归属
    private String regOffice; //	登记部门
    private String fileType; //	证明文件1种类(工商注册类型)
    private String fileNo; //	证明文件1编号(工商注册号)
    private String fileSetUpdate; //	证明文件1设立日期
    private String fileDue; //	证明文件1到期日
    private String regCurrencyType; //	注册资本币种
    private String registerEdcapital; //	注册资金（元）
    @Column(length = 1000)
    private String businessScope; //	经营（业务）范围
    private String corpScale; //	企业规模
    private String legalType; //	法人类型（法定代表人、单位负责人）
    private String legalName; //	法人姓名
    private String legalIdcardType; //	法人证件类型
    private String legalIdcardNo; //	法人证件编号
    private String legalIdcardDue; //	法人证件到期日
    private String legalTelephone; //	法人联系电话
    private String orgCode; //	组织机构代码
    private String orgCodeDue; //	组织机构代码证件到期日
    private String stateTaxregNo; //	纳税人识别号（国税）
    private String stateTaxDue; //	国税证件到期日
    private String taxRegNo; //	纳税人识别号（地税）
    private String taxDue; //	地税证件到期日
    private String workAddress; //	办公详细地址
    private String telephone; //	联系电话
    private String zipCode; //	邮政编码
    private String economyType; //	经济类型

    /**
     * 注册地地区代码
     */
    @Column(length = 50)
    private String regAreaCode;

    /**
     * 办公地区
     */
    @Column(length = 30)
    private String workArea;

}

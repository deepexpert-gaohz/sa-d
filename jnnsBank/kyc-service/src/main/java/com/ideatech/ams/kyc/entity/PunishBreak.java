package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 失信被执行人信息
 */
@Table(name = "yd_saicinfo_punishbreak")
@Entity
@Data
public class PunishBreak extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 年龄
     */
    @Column(name = "yd_ageclean")
    private String ageclean;

    /**
     * 省份
     */
    @Column(name = "yd_areanameclean")
    private String areanameclean;

    /**
     * 法定代表人/负责人姓名
     */
    @Column(name = "yd_businessentity")
    private String businessentity;

    /**
     * 身份证号码
     */
    @Column(name = "yd_cardnum")
    private String cardnum;

    /**
     * 案号
     */
    @Column(name = "yd_casecode")
    private String casecode;

    /**
     * 执行法院
     */
    @Column(name = "yd_courtname")
    private String courtname;

    /**
     * 失信被执行人为具体情形
     */
    @Column(name = "yd_disrupttypename")
    private String disrupttypename;

    /**
     * 生效法律文书确定的义务
     */
    @Column(name = "yd_duty")
    private String duty;

    /**
     * 关注次数
     */
    @Column(name = "yd_focusnumber")
    private String focusnumber;

    /**
     * 执行依据文号
     */
    @Column(name = "yd_gistid")
    private String gistid;

    /**
     * 做出执行依据单位
     */
    @Column(name = "yd_gistunit")
    private String gistunit;

    /**
     * 被执行人姓名/名称
     */
    @Column(name = "yd_inameclean")
    private String inameclean;

    /**
     * 被执行人的履情况
     */
    @Column(name = "yd_performance")
    private String performance;

    /**
     * 已履行
     */
    @Column(name = "yd_performedpart")
    private String performedpart;

    /**
     * 公布时间
     */
    @Column(name = "yd_publishdateclean")
    private String publishdateclean;

    /**
     * 立案时间
     */
    @Column(name = "yd_regdateclean")
    private String regdateclean;

    /**
     * 性别
     */
    @Column(name = "yd_sexyclean")
    private String sexyclean;

    /**
     * 失信人类型
     */
    @Column(name = "yd_type")
    private String type;

    /**
     * 未履行
     */
    @Column(name = "yd_unperformpart")
    private String unperformpart;

    /**
     * 身份证原始发地
     */
    @Column(name = "yd_ysfzd")
    private String ysfzd;

    /**
     * 序列号 来自IDP
     */
    @Column(name = "yd_index")
    private Integer index;

}
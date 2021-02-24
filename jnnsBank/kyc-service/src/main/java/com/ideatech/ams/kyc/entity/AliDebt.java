package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 欠贷信息(未存储)
 */
@Table(name = "yd_saicinfo_alidebt")
@Entity
@Data
public class AliDebt extends BaseMaintainablePo {
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
     * 身份证号码/企业注册号
     */
    @Column(name = "yd_cardnumclean")
    private String cardnumclean;

    /**
     * 贷款到期时间
     */
    @Column(name = "yd_dkdqsj")
    private String dkdqsj;

    /**
     * 贷款期限
     */
    @Column(name = "yd_dkqx")
    private String dkqx;

    /**
     * 欠贷人姓名/名称
     */
    @Column(name = "yd_inameclean")
    private String inameclean;

    /**
     * 法定代表人
     */
    @Column(name = "yd_legalperson")
    private String legalperson;

    /**
     * 欠款额度
     */
    @Column(name = "yd_qked")
    private String qked;

    /**
     * 性别
     */
    @Column(name = "yd_sexyclean")
    private String sexyclean;

    /**
     * 淘宝账户
     */
    @Column(name = "yd_tbzh")
    private String tbzh;

    /**
     * 违约情况
     */
    @Column(name = "yd_wyqk")
    private String wyqk;

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
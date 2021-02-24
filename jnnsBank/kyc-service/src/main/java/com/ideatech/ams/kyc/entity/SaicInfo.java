package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

/**
 * 工商基本信息
 */
@Entity
@Table(name = "yd_saicinfo")
@Data
public class SaicInfo extends BaseMaintainablePo {
    /**
     * 地址
     */
    @Column(name = "yd_address")
    private String address;

    /**
     * 最后年检年度 YYYY
     */
    @Column(name = "yd_ancheyear")
    private String ancheyear;

    /**
     * 最后年检年日期
     */
    @Column(name = "yd_ancheyeardate")
    private String ancheyeardate;

    /**
     * 营业期限终止日期
     */
    @Column(name = "yd_enddate")
    private String enddate;

    /**
     * 法人
     */
    @Column(name = "yd_legalperson")
    private String legalperson;

    /**
     * 法人类型可能的值为“法定代表人”，“经营者”，“负责人”
     */
    @Column(name = "yd_legalpersontype")
    private String legalpersontype;

    /**
     * 核准日期
     */
    @Column(name = "yd_licensedate")
    private String licensedate;

    /**
     * 名称
     */
    @Column(name = "yd_name")
    private String name;

    /**
     * 开业日期
     */
    @Column(name = "yd_opendate")
    private String opendate;

    /**
     * 省
     */
    @Column(name = "yd_province")
    private String province;

    /**
     * 注册资金
     */
    @Column(name = "yd_registfund")
    private String registfund;

    /**
     * 注册资金币种
     */
    @Column(name = "yd_registfundcurrency")
    private String registfundcurrency;

    /**
     * 注册号
     */
    @Column(name = "yd_registno")
    private String registno;

    /**
     * 登记机关
     */
    @Column(name = "yd_registorgan")
    private String registorgan;

    /**
     * 全国企业信用信息公示系统代码
     */
    @Column(name = "yd_saiccode")
    private String saiccode;

    /**
     * 经营范围
     */
    @Column(name = "yd_scope")
    @Lob
    private String scope;

    /**
     * 营业期限起始日期
     */
    @Column(name = "yd_startdate")
    private String startdate;

    /**
     * 经营状态
     */
    @Column(name = "yd_state")
    private String state;

    /**
     * 类型
     */
    @Column(name = "yd_type")
    private String type;

    /**
     * 数据更新时间
     */
    @Column(name = "yd_updatetime")
    private Date updatetime;

    /**
     * 失败JSON
     */
    @Column(name = "yd_failjsonstr")
    private String failjsonstr;

    /**
     * 内部工商号
     */
    @Column(name = "yd_idpno")
    private String idpno;

    /**
     * 消息公告
     */
    @Column(name = "yd_notice")
    private String notice;

    /**
     * 注册地地区
     */
    @Column(name = "yd_regarea")
    private String regarea;

    /**
     * 注册地地区代码
     */
    @Column(name = "yd_regareacode")
    private String regareacode;

    /**
     * 注册地城市
     */
    @Column(name = "yd_regcity")
    private String regcity;

    /**
     * 注册地省份
     */
    @Column(name = "yd_regprovince")
    private String regprovince;

    /**
     * 注销或吊销日期
     */
    @Column(name = "yd_revokedate")
    private String revokedate;

    /**
     * 工商注册号
     */
    @Column(name = "yd_unitycreditcode")
    private String unitycreditcode;

    /**
     * idpId
     */

    private String idpId;
}
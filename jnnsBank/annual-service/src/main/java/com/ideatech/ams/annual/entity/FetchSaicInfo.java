package com.ideatech.ams.annual.entity;

import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.common.entity.AmsEntityLength;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 工商采集表
 * @Author wanghongjie
 * @Date 2018/8/6
 **/
@Table(name = "yd_fetch_saic", indexes = {@Index(name = "fetch_saic_atid_idx",columnList = "annualTaskId")})
@Entity
@Data
public class FetchSaicInfo extends AmsEntityLength implements Serializable {


    /**
     * 采集任务ID
     */
    private Long collectTaskId;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 核心和人行的存款人不相同时，人行的存款人复制到该对象中
     */
    private String customerNameOther;

    /**
     * 注册号
     */
    private String regNo;

    /**
     * 失败状态
     */
    @Column(length = 2000)
    private String failReason;

    /**
     * 经营状态
     */
    private String state;

    /**
     * 工商注册号
     */
    private String unitycreditcode;

    /**
     * 营业期限终止日期
     */
    private String enddate;

    /*
     * 每条记录添加采集状态字段
     */
    @Enumerated(EnumType.STRING)
    private CollectState collectState;

    /**
     * idp返回json
     */
    @Column(length = DEFAULT_CLOB_COLUMN_LENGTH)
    @Lob
    private String idpJsonStr;

    /**
     * 年检任务ID
     */
    private Long annualTaskId;

}

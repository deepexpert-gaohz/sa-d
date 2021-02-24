package com.ideatech.ams.kyc.dto.poi;

import lombok.Data;

/**
 * 受益人工商基本信息公共属性
 * @author wangqingan
 * @version 20/04/2018 2:32 PM
 */
@Data
public class SaicPoi {
    /**
     * 名称
     */
    private String name;

    /**
     * 统一社会信用代码
     */
    private String unitycreditcode;

    /**
     * 注册号
     */
    private String registno;

    /**
     * 类型
     */
    private String type;

    /**
     * 法人
     */
    private String legalperson;

    /**
     * 注册资金
     */
    private String registfund;

    /**
     * 开业日期
     */
    private String opendate;

    /**
     * 营业期限起始日期
     */
    private String startdate;

    /**
     * 营业期限终止日期
     */
    private String enddate;

    /**
     * 登记机关
     */
    private String registorgan;

    /**
     * 核准日期
     */
    private String licensedate;

    /**
     * 登记状态
     */
    private String state;

    /**
     * 地址
     */
    private String address;

    /**
     * 经营范围
     */
    private String scope;


    /**
     * 注销或吊销日期
     */
    private String revokedate;

    /**
     * 董事
     */
    private String directors;

    /**
     * 监事
     */
    private String supervises;

    /**
     * 高管
     */
    private String managements;

}

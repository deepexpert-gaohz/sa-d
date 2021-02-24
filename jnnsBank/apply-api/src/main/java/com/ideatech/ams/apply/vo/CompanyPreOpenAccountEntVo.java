package com.ideatech.ams.apply.vo;

import lombok.Data;

@Data
public class CompanyPreOpenAccountEntVo{
    /**
     * 预约编号
     */
    private String applyid;

    /**
     * 预约时间
     */
    private String applytime;

    /**
     * 预约人员
     */
    private String operator;

    /**
     * 预约手机
     */
    private String phone;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 地区
     */
    private String area;

    /**
     * 银行机构号
     */
    private String organid;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 预约类型（基本户/一般户）
     */
    private String type;

    /**
     * 接洽时间
     */
    private String acceptTimes;

    /**
     * 接洽人员
     */
    private String accepter;

    /**
     * 受理回执
     */
    private String applynote;

    /**
     * 银行受理时间
     */
    private String banktime;

    /**
     * 预约状态
     */
    private String status;
}

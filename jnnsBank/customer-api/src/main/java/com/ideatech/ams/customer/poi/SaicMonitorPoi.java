package com.ideatech.ams.customer.poi;

import lombok.Data;

@Data
public class SaicMonitorPoi {

    /**
     * 调用时间
     */
    private String createTime;

    /**
     * 调用机构
     */
    private String organName;

    /**
     * 调用用户
     */
    private String userName;

    /**
     * 调用企业名称
     */
    private String companyName;

    /**
     * 工商注册号
     */
    private String regNo;

    /**
     * 调用类型
     */
    private String checkType;

}

package com.ideatech.ams.customer.vo;

import com.ideatech.ams.customer.enums.SaicMonitorEnum;
import lombok.Data;

@Data
public class SaicMonitorVo {

    /**
     * 调用用户
     */
    private String userName;

    /**
     * 调用机构
     */
    private String organName;

    /**
     * 调用企业名称
     */
    private String companyName;

    /**
     * 调用时间
     */
    private String createTime;

    /**
     * 调用类型
     */
    private SaicMonitorEnum checkType;

    /**
     * 调用机构FullId
     */
    private String organFullId;

    /**
     * 工商查询对象Id
     */
    private Long saicInfoId;

    /**
     * 工商查询是否成功  success   fail
     */
    private String status;

    private String regNo;

    private String beginDate;

    private String endDate;

}

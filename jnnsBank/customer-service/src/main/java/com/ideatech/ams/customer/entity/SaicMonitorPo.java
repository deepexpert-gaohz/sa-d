package com.ideatech.ams.customer.entity;

import com.ideatech.ams.customer.enums.SaicMonitorEnum;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 工商统计查询
 */
@Table(name = "yd_saic_monitor")
@Entity
@Data
public class SaicMonitorPo extends BaseMaintainablePo {

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
     * 工商注册号
     */
    private String regNo;

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
}

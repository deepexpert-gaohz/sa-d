package com.ideatech.ams.kyc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 基本户履历
 */
@Table(name = "yd_saicinfo_baseaccount")
@Entity
@Data
public class BaseAccount extends BaseMaintainablePo {
    /**
     * 内部工商ID
     */
    @Column(name = "yd_saicinfo_id")
    private Long saicinfoId;

    /**
     * 审批日期
     */
    @Column(name = "yd_licensedate")
    private String licensedate;

    /**
     * 基本户许可证号
     */
    @Column(name = "yd_licensekey")
    private String licensekey;

    /**
     * 审批机关
     */
    @Column(name = "yd_licenseorg")
    private String licenseorg;

    /**
     * 许可类型
     */
    @Column(name = "yd_licensetype")
    private String licensetype;

    /**
     * 单位名称/账户名称
     */
    @Column(name = "yd_name")
    private String name;

}
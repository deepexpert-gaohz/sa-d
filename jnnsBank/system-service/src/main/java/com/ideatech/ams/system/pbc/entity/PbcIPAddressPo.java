package com.ideatech.ams.system.pbc.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 人行年检提交配置
 */
@Data
@Entity
@Table(name = "yd_sys_pbc_ip_address")
public class PbcIPAddressPo extends BaseMaintainablePo {
    /**
     * 人行ip地址
     */
    private String ip;

    /**
     * 省份
     */
    private String provinceName;

    /**
     * 人行年检是否提交标记(true: 人行年检提交  false: 人行年检不提交)
     */
    private Boolean isAnnualSubmit;

}

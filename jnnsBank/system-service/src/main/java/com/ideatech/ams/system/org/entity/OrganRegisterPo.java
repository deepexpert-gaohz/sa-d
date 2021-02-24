package com.ideatech.ams.system.org.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 取消核准制报备 机构关联登记表  （保存取消核准上报的机构）
 */

@Entity
@Table(name = "sys_organ_register")
@Data
public class OrganRegisterPo extends BaseMaintainablePo {

    private Long organId;

    private String name;

    private String pbcCode;

    /**
     * 机构完整ID
     */
    @Column(name = "full_id")
    private String fullId;
}

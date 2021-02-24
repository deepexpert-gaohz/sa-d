package com.ideatech.ams.system.role.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色
 * @author liangding
 * @create 2018-05-05 下午8:19
 **/
@Entity
@Table(name = "sys_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update yd_sys_role set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class RolePo extends BaseMaintainablePo {
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 启用
     */
    private Boolean enabled;
    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;
    /**
     * 级别
     */
    private String level;
}

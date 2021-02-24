package com.ideatech.ams.system.permission.entity;

import com.ideatech.common.entity.BasePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色权限关联表
 * @author liangding
 * @create 2018-05-06 下午9:55
 **/
@Entity
@Table(name = "sys_role_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionPo extends BasePo {
    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 权限ID
     */
    @Column(name = "permission_id")
    private Long permissionId;
}

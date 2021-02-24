package com.ideatech.ams.system.permission.dao;

import com.ideatech.ams.system.permission.entity.RolePermissionPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface RolePermissionDao extends JpaRepository<RolePermissionPo, Long>, JpaSpecificationExecutor<RolePermissionPo> {
    List<RolePermissionPo> findByRoleId(Long roleId);

    List<RolePermissionPo> findByRoleIdIn(Collection<Long> roleId);

    void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId);

    List<RolePermissionPo> findByRoleIdAndPermissionId(Long roleId, Long permissionId);

    RolePermissionPo findByPermissionIdAndRoleId(Long permissionId, Long roleId);

}

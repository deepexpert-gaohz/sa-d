package com.ideatech.ams.system.permission.dao;

import com.ideatech.ams.system.permission.entity.PermissionPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PermissionDao extends JpaRepository<PermissionPo, Long>, JpaSpecificationExecutor<PermissionPo> {
    List<PermissionPo> findALlByIdInAndPermissionTypeOrderByOrderNumAscTitleAsc(Iterable<Long> ids, String type);

    List<PermissionPo> findByCode(String code);

    Page<PermissionPo> findAllByIdInAndPermissionTypeAndParentIdOrderByOrderNumAscTitleAsc(Pageable pageable,Iterable<Long> ids,String permissionType,Long parentId);

    Page<PermissionPo> findAllByIdInAndPermissionTypeOrderByOrderNumAscTitleAsc(Pageable pageable,Iterable<Long> ids, String permissionType);
    List<PermissionPo> findByParentIdOrderByOrderNumAsc(Long parentId);
}

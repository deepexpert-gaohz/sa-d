package com.ideatech.ams.system.role.dao;

import com.ideatech.ams.system.role.entity.RolePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoleDao extends JpaRepository<RolePo, Long>, JpaSpecificationExecutor<RolePo> {
    List<RolePo> findAllByNameLike(String name);

    RolePo findByCode(String code);

    List<RolePo> findByName(String name);
}

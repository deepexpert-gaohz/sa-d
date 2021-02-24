package com.ideatech.ams.system.role.service;

import com.ideatech.ams.system.role.dto.RoleDto;

import java.util.List;

/**
 * @author liangding
 * @create 2018-05-06 下午7:53
 **/
public interface RoleService {
    RoleDto findById(Long roleId);

    List<RoleDto> findAll();

    List<RoleDto> findbyLevel(Long roleId);

    List<RoleDto> findByName(String name);

    List<RoleDto> findByNameEquals(String name);

    void save(RoleDto roleDto);

    void delete(Long id);
    RoleDto findByCode(String code);

}

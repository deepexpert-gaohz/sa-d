package com.ideatech.ams.system.permission.service;

import com.ideatech.ams.system.permission.dto.PermissionDto;
import com.ideatech.ams.system.permission.dto.PermissionSearchDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PermissionService {
    List<PermissionDto> findMenusByRoleId(Long roleId);

    List<PermissionDto> findMenusByRoleIds(Collection<Long> roleId);

    List<PermissionDto> findMenusByTitle(String title);

    List<PermissionDto> findAllMenus();

    PermissionSearchDto searchElement(PermissionSearchDto permissionSearchDto);

    PermissionSearchDto searchElementByRoleId(PermissionSearchDto permissionSearchDto,Long roleId);

    void save(PermissionDto permissionDto);

    PermissionDto findMenuById(Long id);

    PermissionDto findElementById(Long id);

    void deleteMenu(Long id);

    void deleteElement(Long id);

    List<PermissionDto> findElementsByRoleId(Long id);

    List<PermissionDto> findElementsByParentId(Long parentId);

    List<PermissionDto> findElementsByRoleIdAndParentId(Long parentId,Long id);

    void addPermission(Long id, Long elementId);

    void removePermission(Long id, Long elementId);

    void updateRoleMenu(Long id, List<Long> ids);

    Boolean findByCode(String code);

    void addPermissions(Long id, Long[] elementIds);

    void removePermissions(Long id, Long[] elementIds);

   void move(Long id ,boolean up);

    /**
     * 根据角色id获取权限map
     * @param roleId
     * @param type (menu,button)
     * @return
     */
    Map<Long,PermissionDto> getPermissionsMap(Long roleId,String type);

}

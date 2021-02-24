package com.ideatech.ams.controller;

import com.ideatech.ams.system.permission.dto.PermissionDto;
import com.ideatech.ams.system.permission.service.PermissionService;
import com.ideatech.ams.system.role.dto.RoleDto;
import com.ideatech.ams.system.role.service.RoleService;
import com.ideatech.ams.system.trace.aop.OperateLog;
import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.RegexUtils;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author liangding
 * @create 2018-05-06 下午7:52
 **/
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/all")
    public ResultDto all() {
        Long roleId = SecurityUtils.getCurrentUser().getRoleId();
        List<RoleDto> all = roleService.findbyLevel(roleId);
        return ResultDtoFactory.toAckData(all);
    }

    @GetMapping("/currentRole")
    public ResultDto currentRole() {
        Long roleId = SecurityUtils.getCurrentUser().getRoleId();
        RoleDto role = roleService.findById(roleId);
        return ResultDtoFactory.toAckData(role);
    }

    @GetMapping("/list")
    public ResultDto list(String name) {
        List<RoleDto> roleDtos = roleService.findByName(name);
        return ResultDtoFactory.toAckData(roleDtos);
    }

    @OperateLog(operateModule = OperateModule.ROLE,operateType = OperateType.INSERT)
    @PostMapping("/")
    public ResultDto save(RoleDto roleDto) {
        //找到当前用户的等级  不能创建等级比自己大,或同等级的角色
        Long roleId = SecurityUtils.getCurrentUser().getRoleId();
        RoleDto rp = roleService.findById(roleId);

        RoleDto roleDto1 = roleService.findByCode(roleDto.getCode());
        if(roleDto1 != null){
            return ResultDtoFactory.toNack("角色编码已存在，请重新输入角色编码...");
        }

        List<RoleDto> list = roleService.findByNameEquals(roleDto.getName().trim());
        if(CollectionUtils.isNotEmpty(list)){
            return ResultDtoFactory.toNack("角色名称已存在，请重新输入角色名称...");
        }

        if(!RegexUtils.isNumeric(roleDto.getLevel())){
            return ResultDtoFactory.toNack("等级字段输入非法，请输入数字！");
        }
        if(Integer.parseInt(roleDto.getLevel()) <= Integer.parseInt(rp.getLevel())){
            return ResultDtoFactory.toNack("不能创建同等级以及大于当前等级的角色！");
        }
        roleService.save(roleDto);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/{id}")
    public ResultDto findById(@PathVariable("id") Long id) {
        RoleDto byId = roleService.findById(id);
        return ResultDtoFactory.toAckData(byId);
    }

    @OperateLog(operateModule = OperateModule.ROLE,operateType = OperateType.UPDATE)
    @PutMapping("/{id}")
    public ResultDto update(@PathVariable("id") Long id, RoleDto roleDto) {
        Long roleId = SecurityUtils.getCurrentUser().getRoleId();
        RoleDto rp = roleService.findById(id);

        if (!rp.getName().equals(roleDto.getName().trim())) {
            List<RoleDto> list = roleService.findByNameEquals(roleDto.getName().trim());
            if (CollectionUtils.isNotEmpty(list)) {
                return ResultDtoFactory.toNack("角色名称已存在，请重新输入角色名称...");
            }
        }

        if(!RegexUtils.isNumeric(roleDto.getLevel())){
            return ResultDtoFactory.toNack("等级字段输入非法，请输入数字！");
        }
        //获取当前登录的角色的登记  等级: 0为最大，依次递减
        RoleDto roleDto1 = roleService.findById(roleId);
        if(Integer.parseInt(roleDto1.getLevel()) >= Integer.valueOf(rp.getLevel())) {
            return ResultDtoFactory.toNack("不能修改为同等级以及大于当前等级的角色！");
        }
        if(Integer.parseInt(roleDto1.getLevel()) >= Integer.valueOf(roleDto.getLevel())){
            return ResultDtoFactory.toNack("修改等级大于当前登录用户等级，不允许修改！");
        }

        if(!rp.getCode().equals(roleDto.getCode())){
            RoleDto roleDto2 = roleService.findByCode(roleDto.getCode());
            if(roleDto2 != null){
                return ResultDtoFactory.toNack("编码已存在，请重新输入......");
            }
        }

        roleDto.setId(id);
        roleService.save(roleDto);
        return ResultDtoFactory.toAck();
    }

    @OperateLog(operateModule = OperateModule.ROLE,operateType = OperateType.DELETE)
    @DeleteMapping("/{id}")
    public ResultDto delete(@PathVariable("id") Long id) {
        List<UserDto> byRoleId = userService.findByRoleId(id);
        if (CollectionUtils.isNotEmpty(byRoleId)) {
            return ResultDtoFactory.toNack("该角色下有关联用户，不能删除");
        }
        roleService.delete(id);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/{id}/user")
    public ResultDto findUsers(@PathVariable("id") Long id) {
        List<UserDto> byRoleId = userService.findByRoleId(id);
        return ResultDtoFactory.toAckData(byRoleId);
    }

    @GetMapping("/{id}/authority/menu")
    public ResultDto getAuthorityMenu(@PathVariable("id") Long id) {
        List<PermissionDto> menusByRoleId = permissionService.findMenusByRoleId(id);
        return ResultDtoFactory.toAckData(menusByRoleId);
    }

    @OperateLog(operateModule = OperateModule.ROLE,operateType = OperateType.OTHER,operateContent = "权限配置",cover = true)
    @PostMapping("/{id}/authority/menu")
    public ResultDto updateMenu(@PathVariable("id") Long id, String menuIds) {
        String[] split = StringUtils.split(menuIds, ',');
        List<Long> ids = new ArrayList<>();
        for (String s : split) {
            ids.add(Long.parseLong(s));
        }
        permissionService.updateRoleMenu(id, ids);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/{id}/authority/element")
    public ResultDto getAuthorityElement(@PathVariable("id") Long id) {
        List<PermissionDto> elementsByRoleId = permissionService.findElementsByRoleId(id);
        Collection<Long> collection = CollectionUtils.collect(elementsByRoleId, new Transformer() {
            @Override
            public Object transform(Object input) {
                return ((PermissionDto) input).getId();
            }
        });
        return ResultDtoFactory.toAckData(collection);
    }

    @PostMapping("/{id}/authority/element/add")
    public ResultDto addElement(@PathVariable("id") Long id, Long elementId) {
        permissionService.addPermission(id, elementId);
        return ResultDtoFactory.toAck();
    }

    @PostMapping("/{id}/authority/element/remove")
    public ResultDto removeElement(@PathVariable("id") Long id, Long elementId) {
        permissionService.removePermission(id, elementId);
        return ResultDtoFactory.toAck();
    }

    @PostMapping("/{id}/authority/element/adds")
    public ResultDto addElements(@PathVariable("id") Long id, @RequestParam(value = "eleIds") Long[] elementIds) {
        permissionService.addPermissions(id, elementIds);
        return ResultDtoFactory.toAck();
    }

    @PostMapping("/{id}/authority/element/removes")
    public ResultDto removeElements(@PathVariable("id") Long id, @RequestParam(value = "eleIds") Long[] elementIds) {
        permissionService.removePermissions(id, elementIds);
        return ResultDtoFactory.toAck();
    }

    @GetMapping("/getRoleName")
    public String getRoleName() {
        Long roleId = SecurityUtils.getCurrentUser().getRoleId();
        RoleDto roleDto = roleService.findById(roleId);
        return roleDto.getName();
    }

    @PostMapping("/{id}/authority/element/save")
    public ResultDto save(@PathVariable("id") Long id, @RequestParam(value = "addIds") Long[] elementIds1 ,@RequestParam(value = "removeIds") Long[] elementIds2) {
        permissionService.removePermissions(id, elementIds2);
        permissionService.addPermissions(id, elementIds1);
        return ResultDtoFactory.toAck();
    }
}

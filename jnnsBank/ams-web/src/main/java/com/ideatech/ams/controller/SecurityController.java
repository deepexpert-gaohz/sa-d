package com.ideatech.ams.controller;

import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.permission.dto.PermissionDto;
import com.ideatech.ams.system.permission.service.PermissionService;
import com.ideatech.ams.system.role.dto.RoleDto;
import com.ideatech.ams.system.role.service.RoleService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.vo.LoginInfoVo;
import com.ideatech.ams.vo.MenuTreeVo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.SecurityUtils;
import com.ideatech.common.util.TreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author liangding
 * @create 2018-05-06 下午7:52
 **/
@RestController
@RequestMapping("/security")
@Slf4j
public class SecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/menus")
    public ResultDto menus() {
        SecurityUtils.UserInfo currentUser = SecurityUtils.getCurrentUser();
        Long[] roleIds = new Long[]{currentUser.getRoleId()};
        List<PermissionDto> menus = permissionService.findMenusByRoleIds(Arrays.asList(roleIds));
        return ResultDtoFactory.toAckData(buildMenuTree(menus));
    }

    private List<MenuTreeVo> buildMenuTree(List<PermissionDto> menus) {
        List<MenuTreeVo> menuTree = new ArrayList<>();
        for (PermissionDto menu : menus) {
            MenuTreeVo menuTreeVo = new MenuTreeVo(menu.getIcon(), menu.getTitle(), menu.getUrl(), Boolean.FALSE);
            menuTreeVo.setId(menu.getId());
            menuTreeVo.setParentId(menu.getParentId());
            menuTree.add(menuTreeVo);
        }
        return TreeUtil.bulid(menuTree, -1L);
    }

    @GetMapping("/cname")
    public ResultDto<String> getCurrentUserCname() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId != null) {
            UserDto byId = userService.findById(currentUserId);
            return ResultDtoFactory.toAckData(byId.getCname());
        }
        return ResultDtoFactory.toUnauthorized("用户未登录");
    }

    @GetMapping("/info")
    ResultDto<LoginInfoVo> getLoginInfo() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId != null) {
            UserDto byId = userService.findById(currentUserId);
            LoginInfoVo loginInfoVo = ConverterService.convert(byId, LoginInfoVo.class);
            loginInfoVo.setUserId(currentUserId);
            OrganizationDto organizationDto = organizationService.findById(byId.getOrgId());
            loginInfoVo.setBankCode(organizationDto.getCode());
            return ResultDtoFactory.toAckData(loginInfoVo);
        }
        return ResultDtoFactory.toUnauthorized("用户未登录");
    }

    @GetMapping("/crole")
    public ResultDto<RoleDto> getCurrentUserRole() {
        Long roleId = SecurityUtils.getCurrentUser().getRoleId();
        RoleDto byId = roleService.findById(roleId);
        return ResultDtoFactory.toAckData(byId);
    }

    /**
     * 获取用户对应的按钮权限
     * @return
     */
    @RequestMapping(value = "/permissions",method = RequestMethod.GET)
    public List<PermissionDto> getUserElements(){
        List<PermissionDto> list = permissionService.findElementsByRoleId(SecurityUtils.getCurrentUser().getRoleId());

        return list;
    }

    @GetMapping("/dualRecord")
    public ResultDto getDualRecord() {
        Map<String,Object> map = new HashMap<>(4);
        Boolean result = permissionService.findByCode("image_video_accept");
        map.put("auth",result);
        map.put("username",SecurityUtils.getCurrentUsername());
        return ResultDtoFactory.toAckData(map);
    }

}

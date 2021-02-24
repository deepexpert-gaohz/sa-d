package com.ideatech.ams.controller;

import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.role.dto.RoleDto;
import com.ideatech.ams.system.role.service.RoleService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.dto.UserSearchDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liangding
 * @create 2018-05-06 下午7:51
 **/
@RestController
@RequestMapping("/online")
@Slf4j
public class OnlineController {
    @Autowired
    private UserService userService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganizationService organizationService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ResultDto  query(UserSearchDto userSearchDto) {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        Set<String> nameSet = new HashSet<>();
        for (Object obj : allPrincipals) {
            if (obj != null) {
                if (obj instanceof SecurityUtils.UserInfo) {
                    nameSet.add(((SecurityUtils.UserInfo) obj).getUsername());
                } else {
                    nameSet.add(obj.toString());
                }
            }
        }
        userSearchDto = userService.findByUsernameList(new ArrayList<>(nameSet), userSearchDto);
        for (UserDto userDto : userSearchDto.getList()) {
            userDto.setPassword("");
            Long roleId = userDto.getRoleId();
            Long orgId = userDto.getOrgId();
            if (roleId != null) {
                RoleDto byId = roleService.findById(roleId);
                if (byId != null) {
                    userDto.setRoleName(byId.getName());
                }
            }
            if (orgId != null) {
                OrganizationDto byId = organizationService.findById(orgId);
                if (byId != null) {
                    userDto.setOrgName(byId.getName());
                }
            }
        }
        return ResultDtoFactory.toAckData(userSearchDto);
    }
}

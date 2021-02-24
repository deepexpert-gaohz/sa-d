package com.ideatech.ams.controller;

import com.ideatech.ams.config.AmsWebProperties;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.ams.ws.api.service.SsoValidationService;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/sso")
@Slf4j
public class SsoController {
    public static final String INDEX_URL = "/ui/index.html";
    public static final String LOGIN_URL = "/ui/login.html";

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SsoValidationService ssoValidationService;

    @Autowired
    private AmsWebProperties amsWebProperties;

    @RequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = null;
        try {
            username = ssoValidationService.getValidUsername(request, response);
        } catch (Exception e) {
            log.error("统一认证失败", e);
            response.sendRedirect(request.getContextPath()+amsWebProperties.getSsoFail()+"?errorMsg="+ URLEncoder.encode("统一认证失败", "UTF-8"));
            return;
        }
        if (StringUtils.isEmpty(username)) {
            response.sendRedirect(request.getContextPath()+amsWebProperties.getSsoFail()+"?errorMsg="+ URLEncoder.encode("没有获取到统一登录的用户", "UTF-8"));
            return;
        }

        UserDto userDto = userService.findByUsername(username);
        if (null == userDto) {
            response.sendRedirect(request.getContextPath()+amsWebProperties.getSsoFail()+"?errorMsg="+ URLEncoder.encode("用户不存在", "UTF-8"));
            return;
        }

        if(!userDto.getEnabled()){
            response.sendRedirect(request.getContextPath()+amsWebProperties.getSsoFail()+"?errorMsg="+ URLEncoder.encode("用户已禁用", "UTF-8"));
            return;
        }


        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        SecurityUtils.UserInfo user = new SecurityUtils.UserInfo(username, "", authorities);

        if(userDto.getOrgId() != null) {
            OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
            user.setId(userDto.getId());
            user.setOrgFullId(organizationDto.getFullId());
            user.setOrgId(organizationDto.getId());
            user.setRoleId(userDto.getRoleId());
        } else {
            response.sendRedirect(request.getContextPath()+amsWebProperties.getSsoFail()+"?errorMsg="+ URLEncoder.encode("用户不属于任何机构，无法登陆", "UTF-8"));
            return;
        }

//		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", authorities));
        response.sendRedirect(request.getContextPath()+amsWebProperties.getIndex());
    }
}

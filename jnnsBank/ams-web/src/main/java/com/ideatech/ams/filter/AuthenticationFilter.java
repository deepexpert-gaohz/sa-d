package com.ideatech.ams.filter;

import com.ideatech.ams.config.AmsWebProperties;
import com.ideatech.ams.system.permission.service.PermissionService;
import com.ideatech.common.util.ApplicationContextUtil;
import com.ideatech.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liangding
 * @create 2018-06-05 下午10:32
 **/
@Component
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private AmsWebProperties amsWebProperties;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ConfigurableEnvironment environment;

    private List<AntPathRequestMatcher> antPathRequestMatchers;

    @Override
    protected void initFilterBean() throws ServletException {
        antPathRequestMatchers = new ArrayList<>();
        for (String s : amsWebProperties.getPassby()) {
            antPathRequestMatchers.add(new AntPathRequestMatcher(s));
        }
        super.initFilterBean();

        //打印所有配置文件信息
        ApplicationContextUtil.getAllProp(environment);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 过滤用户权限
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String url = request.getServletPath();
        if (StringUtils.equals(url, "/") || StringUtils.equals(url, amsWebProperties.getLogin())) {
            log.debug("ignored url:{}", url);
            return true;
        }

        for (AntPathRequestMatcher antPathRequestMatcher : antPathRequestMatchers) {
            if (antPathRequestMatcher.matches(request)) {
                log.debug("ignored url:{}, match pattern:{}", url, antPathRequestMatcher.getPattern());
                return true;
            }
        }
        return false;
    }
}

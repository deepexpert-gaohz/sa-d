package com.ideatech.ams.filter;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class KaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private String servletPath;
    public KaptchaAuthenticationFilter(String servletPath, String failureUrl) {
        super(servletPath);
        this.servletPath = servletPath;
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(failureUrl));
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String str = req.getMethod();
        if ("POST".equalsIgnoreCase(str) && servletPath.equals(req.getServletPath())) {
            String expect = (String) req.getSession().getAttribute("sessionImageCode");
            Long time = (Long) req.getSession().getAttribute("imageTime");
            String imageCode = req.getParameter("imageCode");
            if (expect != null && !expect.equalsIgnoreCase(imageCode)) {
                unsuccessfulAuthentication(req, res, new AuthenticationServiceException("7"));
                return;
            }

            Long nowTime = System.currentTimeMillis();
            if(time != null) {
                if((nowTime-time)/1000>100){
                    //验证码超时
                    unsuccessfulAuthentication(req, res, new AuthenticationServiceException("7"));
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        return null;
    }
}

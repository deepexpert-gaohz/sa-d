package com.ideatech.ams.config;

import com.ideatech.ams.filter.AuthenticationFilter;
import com.ideatech.ams.filter.KaptchaAuthenticationFilter;
import com.ideatech.ams.permissions.IdeaAuthenticationProvider;
import com.ideatech.ams.session.IdeaSessionRegistry;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.session.StoreType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.stereotype.Component;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-02 12:02
 */
@Configuration
@EnableWebSecurity
@Component
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private IdeaAuthenticationProvider ideaAuthenticationProvider;

    @Autowired
    private AmsWebProperties amsWebProperties;

    @Autowired(required = false)
    private FindByIndexNameSessionRepository sessionRepository;

    @Value("${spring.session.store-type:none}")
    private String storeType;

    @Value("${identifyCode.enabled:false}")
    private Boolean identifyCodeEnabled;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] passby = amsWebProperties.getPassby().toArray(new String[0]);
        String[] custPassby = amsWebProperties.getCustPassby().split(",");
        String maximumSessions = amsWebProperties.getMaximumSessions();
        boolean maxSessionsPreventsLogin = amsWebProperties.getMaxSessionsPreventsLogin();
        http.formLogin()
                // 登录页面的位置
                .loginPage(amsWebProperties.getLogin())
                // 处理登录请求的url
                .loginProcessingUrl("/auth")
                .defaultSuccessUrl(amsWebProperties.getIndex(), true)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl(amsWebProperties.getLogout())
                .invalidateHttpSession(true)
                // 退出时删除cookie
                .deleteCookies("JSESSIONID")
                .and()
                .authorizeRequests()
                .antMatchers((String[]) ArrayUtils.addAll(passby, (custPassby.length == 1 && "".equals(custPassby[0].trim())) || custPassby.length == 0 ? null : custPassby))
                // 通过oauth2.0来鉴权
                .permitAll()
                .antMatchers("/health").permitAll()
                .antMatchers("/metrics").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .authenticated();
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.httpBasic();

        // 禁用缓存
        http.headers().cacheControl();
        http.headers().contentTypeOptions().disable();
        http.addFilterAfter(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().maximumSessions(Integer.valueOf(maximumSessions)).maxSessionsPreventsLogin(maxSessionsPreventsLogin).sessionRegistry(sessionRegistry());
//    http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
        //登录时候验证码是否启用
        if(identifyCodeEnabled) {
            ////在认证用户名之前认证验证码，如果验证码错误，将不执行用户名和密码的认证
            http.addFilterBefore(new KaptchaAuthenticationFilter("/auth", "/login/fail"), UsernamePasswordAuthenticationFilter.class);
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.httpFirewall(new DefaultHttpFirewall());
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(detailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(ideaAuthenticationProvider);
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }

    /**
     * Spring-session实现SessionRegistry 自定义接口实现并发会话控制，支持集群环境
     *
     * @return
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        StoreType storeTypeEnum = StoreType.valueOf(storeType.toUpperCase());
        if (storeTypeEnum == StoreType.JDBC) {
            return new IdeaSessionRegistry(this.sessionRepository);
        } else if (storeTypeEnum != StoreType.NONE) {
            return new SpringSessionBackedSessionRegistry(this.sessionRepository);
        }
        return new SessionRegistryImpl();
    }

}


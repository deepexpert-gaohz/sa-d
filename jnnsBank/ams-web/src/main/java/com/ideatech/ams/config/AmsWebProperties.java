package com.ideatech.ams.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liangding
 * @create 2018-06-06 下午2:50
 **/
@Component
@ConfigurationProperties(prefix = "ams.web")
@Data
public class AmsWebProperties {
    private List<String> passby;
    private String login;
    private String logout;
    private String ssoFail;
    private String index;
    private String defaultPassword;
    private String defaultSM4Password;
    private String defaultRoleCode;
    private String eccsPasswordPrefix;
    private String custPassby;
    private String maximumSessions;
    private Boolean maxSessionsPreventsLogin;
}

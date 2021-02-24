package com.ideatech.ams.config;

import com.ideatech.ams.system.sm4.SM4PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class PasswordEncoderConfig {

    /**
     * 是否启用SM4加密方式
     */
    @Value("${sms4Encrypt.enabled:false}")
    private Boolean sms4EncryptEnabled;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        if(sms4EncryptEnabled) {
            return new SM4PasswordEncoder();  //SM4加密方式
        }

        return new BCryptPasswordEncoder();  //spring security加密方式
    }


}

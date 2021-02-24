package com.ideatech.ams.system.sm4.service;

import com.ideatech.common.encrypt.Sm4Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EncryptPwdServiceImpl implements EncryptPwdService {
    @Value("${sms4Encrypt.enabled:false}")
    private Boolean sms4EncryptEnabled;

    @Value("${sms4Encrypt.key:11}")
    private String key;

    @Override
    public String encryptPwd(String password) {
        if(StringUtils.isNotBlank(password)) {
            if(sms4EncryptEnabled) {
                try {
                    return Sm4Util.encryptEcb(key, password);
                } catch (Exception e) {
                    log.error("密码SM4加密出错", e);
                }

            }
        }

        return password;
    }

    @Override
    public String decryptEcbPwd(String password) throws Exception {
        if(StringUtils.isNotBlank(password)) {
            if(sms4EncryptEnabled) {
                return Sm4Util.decryptEcb(key, password);
            }
        }

        return password;
    }


}

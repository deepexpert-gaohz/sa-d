package com.ideatech.ams.system.sm4;

import com.ideatech.common.encrypt.Sm4Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class SM4PasswordEncoder implements PasswordEncoder {
    @Value("${sms4Encrypt.key:11}")
    private String key;

    /**
     * 密码加密
     * @param charSequence 明文
     * @return 密文
     */
    @Override
    public String encode(CharSequence charSequence) {
        try {
            return Sm4Util.encryptEcb(key, charSequence.toString());
        } catch (Exception e) {
            log.error("密码SM4加密出错", e);
        }

        return null;
    }

    /**
     * 密码解密
     * @param charSequence 明文
     * @param s  密文
     * @return  true表示解密成功,false表示失败
     */
    @Override
    public boolean matches(CharSequence charSequence, String s) {
        try {
            return Sm4Util.verifyEcb(key, s, charSequence.toString());
        } catch (Exception e) {
            log.error("密码SM4解密出错", e);
        }

        return false;
    }
}

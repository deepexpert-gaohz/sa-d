package com.ideatech.common.encrypt;

import com.ideatech.common.exception.EacException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by hammer on 2018/5/23.
 */
public abstract class AESEncryptUtil {
    private static final Logger log = LoggerFactory.getLogger(AESEncryptUtil.class);
    private static final String AES_CBC_PKC_ALG = "AES/CBC/PKCS5Padding";
    private static final byte[] AES_IV = initIV("AES/CBC/PKCS5Padding");

    public AESEncryptUtil() {
    }

    public static String encode(String content, String password) {
        if(StringUtils.isEmpty(content)) {
            log.info("aes加密的目标数据为空");
            throw new EacException("aes加密的目标数据为空");
        }
        try {
            byte[] bytePwd = password.getBytes("UTF-8");
            SecretKeySpec key = new SecretKeySpec(bytePwd, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] byteContent = content.getBytes("UTF-8");
            cipher.init(1, key, new IvParameterSpec(AES_IV));
            byte[] result = cipher.doFinal(byteContent);
            return Base64.encodeBase64String(result);
        } catch (Exception var7) {
            log.error("Encode failed", var7);
            return null;
        }
    }

    public static String decode(String content, String password) {
        try {
            byte[] byteContent = Base64.decodeBase64(content);
            byte[] enCodeFormat = password.getBytes("UTF-8");
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, key, new IvParameterSpec(AES_IV));
            byte[] result = cipher.doFinal(byteContent);
            return new String(result, "UTF-8");
        } catch (Exception var7) {
            log.error("Decode failed", var7);
            return null;
        }
    }

    private static byte[] initIV(String aesCbcPkcAlg) {
        try {
            Cipher cp = Cipher.getInstance(aesCbcPkcAlg);
            int blockSize = cp.getBlockSize();
            byte[] iv = new byte[blockSize];

            for(int i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }

            return iv;
        } catch (Exception var6) {
            int blockSize = 16;
            byte[] iv = new byte[blockSize];

            for(int i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }

            return iv;
        }
    }
}

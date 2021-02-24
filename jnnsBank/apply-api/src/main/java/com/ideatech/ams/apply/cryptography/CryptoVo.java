package com.ideatech.ams.apply.cryptography;

import lombok.Data;

/**
 * Created by hammer on 2018/5/9.
 */
@Data
public class CryptoVo {

    public static boolean isSupported(Object obj) {
        if (obj == null)
            return false;
        return obj instanceof CryptoVo;
    }


    // the business context after encrypt
    private String encryptBizContext;

    // the random generated key for AES encrypt
    private String encryptKey;

    // 当前组织机构的唯一编码，用于获取当前机构在远端的公钥 (加密的)
    private String encryptOrgCode;

    // the signature for business context
    private String signature;


}

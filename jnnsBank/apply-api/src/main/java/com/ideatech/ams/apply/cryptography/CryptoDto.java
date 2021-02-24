package com.ideatech.ams.apply.cryptography;

import lombok.Data;

/**
 * Created by hammer on 2018/5/22.
 */
@Data
public class CryptoDto {

    public static boolean isSupported(Object obj) {
        if (obj == null)
            return false;
        return obj instanceof CryptoDto;
    }

    // the business context after encrypt
    private String encryptBizContext;

    // the random generated key for AES encrypt
    private String encryptKey;

    // 当前组织机构的唯一编码，用于获取当前机构在远端的公钥 (加密的)
    private String encryptOrgCode;

    // the signature for business context
    private String signature;

    // the original business context
    private String rawBizContext;

    // the original random key
    private String rawKey;

    // 当前组织机构的唯一编码，用于获取当前机构在远端的公钥 (明文的)
    private String thirdPartyOrgCode;


}

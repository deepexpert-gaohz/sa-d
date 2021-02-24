package com.ideatech.ams.apply.cryptography;

/**
 * Created by hammer on 2018/5/24.
 */
public class CryptoOutput extends CryptoDto {

    public static boolean isSupported(Object obj) {
        if (obj == null)
            return false;
        return obj instanceof CryptoOutput;
    }
}

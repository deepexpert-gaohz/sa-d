package com.ideatech.common.encrypt;

import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import com.ideatech.common.exception.EacException;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * Created by hammer on 2018/5/14.
 */
@Slf4j
public class RSAEncryptUtil {
    private static final String RSA = "RSA";
    private static final String RSA_ECB_PKCS1 = "RSA/ECB/PKCS1Padding";
    private static final String SIGN_ALGORITHM = "SHA1withRSA";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    private RSAEncryptUtil() {

    }

    public static RSAEncryptUtil getInstance() {
        return new RSAEncryptUtil();
    }


    /**
     * 根据路径初始化私钥
     * @param privateKeyPath
     * @return
     */
    public RSAEncryptUtil initPriKeyByPath(String privateKeyPath) {
        String privateKeyContent = getKeyPemContent(privateKeyPath);
        privateKey = generatePrivateKey(privateKeyContent);
        return this;
    }

    /**
     * 根据路径初始化公钥
     * @param publicKeyPath
     * @return
     */
    public RSAEncryptUtil initPubKeyByPath(String publicKeyPath) {
        String publicKeyContent = getKeyPemContent(publicKeyPath);
        publicKey = generatePublicKey(publicKeyContent);
        return this;
    }


    /**
     * 根据字符串初始化私钥
     * @param privateKeyPemContent
     * @return
     */
    public RSAEncryptUtil initPriKeyByCnt(String privateKeyPemContent) {
        privateKey = generatePrivateKey(privateKeyPemContent);
        return this;
    }

    /**
     * 根据字符串初始化公钥
     * @param publicKeyPemContent
     * @return
     */
    public RSAEncryptUtil initPubKeyByCnt(String publicKeyPemContent) {
        publicKey = generatePublicKey(publicKeyPemContent);
        return this;
    }

    /**
     * 生成公钥
     * @param keyPemContent 密钥内容
     * @return
     */
    private PublicKey generatePublicKey(String keyPemContent) {

        StringReader stringReader = new StringReader(keyPemContent);
        PemReader pemReader = new PemReader(stringReader);
        try {
            PemObject pemObject = pemReader.readPemObject();
            KeyFactory factory = KeyFactory.getInstance(RSA);
            byte[] content = pemObject.getContent();
            X509EncodedKeySpec spec = new X509EncodedKeySpec(content);
            PublicKey publicKey = factory.generatePublic(spec);
            return publicKey;
        } catch (Exception e) {
            log.error(" 生成公钥时遇到错误 ", e);
            throw new EacException(" 生成公钥时遇到错误  ", e);
        }
    }

    /**
     * 读取文件中的内容
     * @param fileName 文件绝对地址
     * @return
     */
    private String getKeyPemContent(String fileName) {
        try {
            byte[] keysBytes = Files.readAllBytes(new File(fileName).toPath());
            String pkStr = new String(keysBytes, "UTF-8");
            return pkStr;
        } catch (Exception e) {
            log.error("从文件目录中获取生成密钥的字符串遇到错误", e);
            throw new EacException("从文件目录中获取生成密钥的字符串遇到错误", e);
        }
    }

    /**
     * 生成私钥
     *
     * @param keyPemContent
     * @return
     */
    private PrivateKey generatePrivateKey(String keyPemContent) {
        StringReader stringReader = new StringReader(keyPemContent);
        PemReader pemReader = new PemReader(stringReader);
        try {
            PemObject pemObject = pemReader.readPemObject();
            KeyFactory factory = KeyFactory.getInstance(RSA);
            byte[] content = pemObject.getContent();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
            PrivateKey privateKey = factory.generatePrivate(privKeySpec);
            return privateKey;
        } catch (Exception e) {
            log.error("生成私钥时遇到错误 ", e);
            throw new EacException("生成私钥时遇到错误  ", e);

        }
    }

    public PrivateKey getPrivateKey() {
        if(privateKey == null) {
            throw new EacException("私钥加载异常，私钥为空");
        }
        return privateKey;
    }

    public PublicKey getPublicKey() {
        if(publicKey == null) {
            throw new EacException("公钥加载异常，公钥为空");
        }
        return publicKey;
    }

    public String b64encodeByte2Str(byte[] originByteArr) {
        return Base64.encodeBase64String(originByteArr);
    }

    /**
     * 对字符串进行加密
     * @param originMsg
     * @param publicKey
     * @return
     */
    public byte[] encryptStr2Byte(String originMsg, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(originMsg.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new EacException("使用公钥对数据进行加密时遇到错误", e);
        }
    }

    public byte[] b64decodeStr2Byte(String encryptedMsg) {
        return Base64.decodeBase64(encryptedMsg);
    }

    /**
     * 将输入的数据流解密
     * @param encryptedByte
     * @param key
     * @return
     */
    public String decryptByte2Str(byte[] encryptedByte, PrivateKey key) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(encryptedByte), "UTF-8");
        } catch (Exception e) {
            throw new EacException("使用私钥对密文进行解密时遇到错误", e);
        }
    }

    /**
     * 对输入的字符串进行签名
     * @param data
     * @param privateKey
     * @return
     */
    public byte[] sign(String data, PrivateKey privateKey){
        try {
            Signature rsa = Signature.getInstance(SIGN_ALGORITHM);
            rsa.initSign(privateKey);
            rsa.update(data.getBytes("UTF-8"));
            return rsa.sign();
        } catch (Exception e) {
            log.error(" 对数据进行签名时遇到错误", e);
            throw new EacException(" 对数据进行签名时遇到错误", e);
        }
    }

    /**
     * 对原始数据进行验签
     * @param data 原始数据
     * @param signature 签名
     * @param publicKey 公钥
     * @return
     */
    public boolean verifySignature(String data, byte[] signature, PublicKey publicKey) {

        try {
            Signature sig = Signature.getInstance(SIGN_ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(data.getBytes("UTF-8"));
            return sig.verify(signature);
        } catch (Exception e) {
            log.error(" 对签名数据进行验签时遇到错误", e);
            throw new EacException(" 对签名数据进行验签时遇到错误", e);
        }
    }

}

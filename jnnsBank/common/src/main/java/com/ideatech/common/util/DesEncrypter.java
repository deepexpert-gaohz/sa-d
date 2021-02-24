package com.ideatech.common.util;


import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

public class DesEncrypter {
	Cipher ecipher;
	Cipher dcipher;
	byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

	/**
	 * 构造方法
	 * 
	 * @param passPhrase
	 *            将用户的apikey作为密钥传入
	 * @throws Exception
	 */
	public DesEncrypter(String passPhrase) throws Exception {
		int iterationCount = 2;
		KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
		SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
		ecipher = Cipher.getInstance(key.getAlgorithm());
		dcipher = Cipher.getInstance(key.getAlgorithm());
		AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
		ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
	}

	/**
	 * 加密
	 * 
	 * @param str
	 *            要加密的字符串
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String str) throws Exception {
//		str = new String(str.getBytes(), "UTF-8");
		return Base64Utils.encodeToString(ecipher.doFinal(str.getBytes("UTF-8")));
	}

	/**
	 * 解密
	 * 
	 * @param str
	 *            要解密的字符串
	 * @return
	 * @throws Exception
	 */
	public String decrypt(String str) throws Exception {

		// 替换参数字符串包含空格的问题
		str = str.replace(" ", "+");

		return new String(dcipher.doFinal(Base64Utils.decodeFromString(str)), "UTF-8");
	}
}

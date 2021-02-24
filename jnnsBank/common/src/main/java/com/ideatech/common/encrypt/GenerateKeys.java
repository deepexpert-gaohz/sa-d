package com.ideatech.common.encrypt;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public class GenerateKeys {
	private KeyPairGenerator keyGen;
	private KeyPair pair;
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public GenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
		this.keyGen = KeyPairGenerator.getInstance("RSA");
		this.keyGen.initialize(keylength);
	}

	public void createKeys() {
		this.pair = this.keyGen.generateKeyPair();
		this.privateKey = pair.getPrivate();
		this.publicKey = pair.getPublic();
	}

	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	public void writeToFile(String path, byte[] key) throws IOException {
		File f = new File(path);
		f.getParentFile().mkdirs();

		FileOutputStream fos = new FileOutputStream(f);
		fos.write(key);
		fos.flush();
		fos.close();
	}
/*
	public static KeyPair ausoCreateKeys() {
		GenerateKeys gk;
		try {
			gk = new GenerateKeys(2048);
			return gk;
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		
	}*/
	
	public static void main(String[] args) {
		/*GenerateKeys gk;
		try {
			gk = new GenerateKeys(2048);
			gk.createKeys();
			gk.writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
			gk.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
//			String formatPrivate = gk.generatePrivateKey().getFormat(); // PKCS#8
//			String formatPublic = gk.getPublicKey().getFormat(); // X.509
			System.out.println("Private Key : " + Base64.encodeBase64String(gk.getPrivateKey().getEncoded()));
			System.out.println("Public Key : " + Base64.encodeBase64String(gk.getPublicKey().getEncoded()));
//			PEMReader pemReader = new PEMReader(new StringReader(pemKey));
//			PEMWriter pemWriter = new PEMWriter(new StringWriter())
			*//**
			 * using pem to create private key string,
			 *//*
			StringWriter privateKeyWriter = new StringWriter();
			PemWriter pemWriter = new PemWriter(privateKeyWriter);
//			PEMWriter pemWriter = new PEMWriter(privateKeyWriter);

			pemWriter.writeObject(new PemObject("RSA PRIVATE KEY", gk.getPrivateKey().getEncoded()));
			pemWriter.flush();
			String privateKey = privateKeyWriter.toString();
			gk.writeToFile("KeyPair/id_rsa_test", privateKey.getBytes("UTF-8"));

			StringWriter publicKeyWriter = new StringWriter();
			PemWriter publicKeyPemWriter = new PemWriter(publicKeyWriter);
			publicKeyPemWriter.writeObject(new PemObject("RSA PUBLIC KEY", gk.getPublicKey().getEncoded()));
			publicKeyPemWriter.flush();
			String publicKey = publicKeyWriter.toString();
			gk.writeToFile("KeyPair/id_rsa_test.pub", publicKey.getBytes("UTF-8"));


			System.out.println("Private Key : " + gk.getPrivateKey());
			System.out.println("Private key string : " + privateKey);
			System.out.println("Public Key : " + gk.getPublicKey());
			System.out.println("Public Key string : " + publicKey);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}*/

	}
}

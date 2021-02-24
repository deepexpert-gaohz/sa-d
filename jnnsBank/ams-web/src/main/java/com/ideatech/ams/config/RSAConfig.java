package com.ideatech.ams.config;

import java.io.IOException;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ideatech.ams.apply.dto.RSACode;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.common.encrypt.GenerateKeys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;

@Configuration
@Slf4j
@DependsOn("applicationContextUtil")
public class RSAConfig{

	//默认私钥
	@Value("${encrypt.ideatech.privateKey}")
    private String encryptIdeatechPrivateKey;
    
    // 自动产生开关
    @Value("${encrypt.ideatech.autoCreate}")
    private boolean autoCreate;
    
	// 三方公钥
    @Value("${encrypt.thirdPart.publicKey}")
    private String encryptThirdPartPublicKey;
    
    @Autowired
    private ConfigService configService;
    
	@Bean 
	 public RSACode getRSACode(){
		log.info("RSAConfig.getRSACode加载");
		RSACode rsaCode = new RSACode();
		if(!autoCreate) {
			log.info("开始使用文件中默认的公私钥");
			rsaCode.setPrivateKey(encryptIdeatechPrivateKey);
			rsaCode.setThirdPublicKey(encryptThirdPartPublicKey);
			rsaCode.setOrganid("Kdxuc7bMD35E");//易得银行联行号
			log.info("文件中默认的公私钥生成成功,"+rsaCode.toString());
		}else {
			List<ConfigDto> privateKeys = configService.findByKey("privateKey");
			List<ConfigDto> thirdPublicKeys = configService.findByKey("thirdParthPublicKey");
			List<ConfigDto> organids = configService.findByKey("organid");
			if((privateKeys.size() > 0 && privateKeys.get(0) != null) && (thirdPublicKeys.size() > 0 && thirdPublicKeys.get(0) != null)) {
				log.info("开始使用数据库中默认的公私钥");
				ConfigDto privateKey = privateKeys.get(0);
				ConfigDto thirdPublicKey = thirdPublicKeys.get(0);
				ConfigDto organid = organids.get(0);
				rsaCode.setPrivateKey(privateKey.getConfigValue());
				rsaCode.setThirdPublicKey(thirdPublicKey.getConfigValue());
				rsaCode.setOrganid(organid.getConfigValue());
				log.info("数据库默认的公私钥生成成功,"+rsaCode.toString());
			}else {
				createKeys(rsaCode);
			}
		}
		return rsaCode;
	 }
	
	
	private void saveConfigDto(String key,String value) {
		ConfigDto configDto = new ConfigDto();
		configDto.setConfigKey(key);
		configDto.setConfigValue(value);
		configService.save(configDto);
	}
	
	private void createKeys(RSACode rsaCode) {
		GenerateKeys gk;
		try {
			gk =new GenerateKeys(2048);
			gk.createKeys();
			log.info("自动生成新的公钥和私钥");
			StringWriter privateKeyWriter = new StringWriter();
			PemWriter pemWriter = new PemWriter(privateKeyWriter);
			pemWriter.writeObject(new PemObject("RSA PRIVATE KEY", gk.getPrivateKey().getEncoded()));
			pemWriter.flush();
			String privateKey = privateKeyWriter.toString();
			StringWriter publicKeyWriter = new StringWriter();
			PemWriter publicKeyPemWriter = new PemWriter(publicKeyWriter);
			publicKeyPemWriter.writeObject(new PemObject("RSA PUBLIC KEY", gk.getPublicKey().getEncoded()));
			publicKeyPemWriter.flush();
			String publicKey = publicKeyWriter.toString();
			saveConfigDto("privateKey",privateKey);
			saveConfigDto("publicKey",publicKey);
			saveConfigDto("thirdParthPublicKey","");
			saveConfigDto("organid","");
			rsaCode.setPrivateKey(privateKey);
//			rsaCode.setThirdPublicKey(encryptThirdPartPublicKey);
			log.info("新的公钥和私钥生成成功,"+rsaCode.toString());
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			log.error("新生成的私钥和公钥错误 ");
		} catch (IOException e) {
			log.error("新生成的私钥和公钥错误 ");
		}
	}
}

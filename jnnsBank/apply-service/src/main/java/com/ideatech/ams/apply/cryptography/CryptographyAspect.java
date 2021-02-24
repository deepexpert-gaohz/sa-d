package com.ideatech.ams.apply.cryptography;


import java.security.PublicKey;

import org.apache.commons.codec.binary.Base64;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ideatech.ams.apply.dto.RSACode;
import com.ideatech.ams.apply.service.CompanyPreOpenAccountEntService;
import com.ideatech.common.encrypt.AESEncryptUtil;
import com.ideatech.common.encrypt.RSAEncryptUtil;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.RandomUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanghongjie
 *
 * @version 2018-05-28 14:12
 */
@Aspect
@Component
@Slf4j
public class CryptographyAspect {

	@Autowired
	private RSACode rsaCode;
	
	@Autowired
	private CompanyPreOpenAccountEntService companyPreOpenAccountEntService;
	
    private RSAEncryptUtil getSelfRsaUtil() {
        // 数据库取出易得的私钥字符串
    	String privateKey = rsaCode.getPrivateKey();
        if(privateKey == null || StringUtils.isEmpty(privateKey))  {
            log.error("找不到私钥");
            throw new EacException(" 找不到指定的私钥 ");
        }

        //初始化易得的私钥，用于解密，
        RSAEncryptUtil ezhanghuRsaUtil = RSAEncryptUtil.getInstance();
        ezhanghuRsaUtil.initPriKeyByCnt(privateKey);
        return ezhanghuRsaUtil;
    }

    private RSAEncryptUtil getThirdPartyRsaUtil() {
    	String thirdPublicKey = rsaCode.getThirdPublicKey();
        // 数据库取出对应的银行的公钥字符串
        if(thirdPublicKey == null || StringUtils.isEmpty(thirdPublicKey))  {
            log.error("找不到易账户的公钥");
            throw new EacException("找不到指定的公钥 ");
        }

        RSAEncryptUtil thirdPartyRsaUtil = RSAEncryptUtil.getInstance();
        thirdPartyRsaUtil.initPubKeyByCnt(thirdPublicKey);
        return thirdPartyRsaUtil;
    }

    
    @Around("@annotation(EnableReceiveCryptography)")
    public void handleResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        CryptoInput cryptoInput = null;
        CryptoOutput cryptoOutput = null;
        for(Object obj : args) {
            if (CryptoInput.isSupported(obj)) {
                cryptoInput = (CryptoInput) obj;

            }
            if (CryptoOutput.isSupported(obj)) {
                cryptoOutput = (CryptoOutput) obj;
            }
        }

        RSAEncryptUtil ezhanghuRsaUtil = getSelfRsaUtil();
        // 解密，并将解密后的数据放入cryptoInput
        decryptContext(cryptoInput, ezhanghuRsaUtil);

        // 创建副本传回被代理的方法
        CryptoDto replicateCryptoInput = new CryptoDto();
        BeanUtils.copyProperties(cryptoInput, replicateCryptoInput);

        //  返回原方法执行业务逻辑
        joinPoint.proceed();

        if(StringUtils.isEmpty(cryptoOutput.getRawBizContext())) {
            return;
        }
//        if (!CryptoDto.isSupported(proceed)) {
//            throw new EacException("aop代理接受到的返回类型与期望的类型不匹配");
//        }
//        CryptoDto cryptoOutput = (CryptoDto) proceed;
        // 需要将第三方机构加入到参数中，在加密时会用第三方机构的公钥做签名
        cryptoOutput.setThirdPartyOrgCode(cryptoInput.getThirdPartyOrgCode());
        // 加密，并将加密后的数据放入cryptoOutput
        encryptContext(cryptoOutput, ezhanghuRsaUtil);


    }

    @Around("@annotation(EnableSendCryptography)")
    public String processRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        CryptoInput cryptoInput = null;
        CryptoOutput cryptoOutput = null;
        for(Object obj : args) {
            if (CryptoInput.isSupported(obj)) {
                cryptoInput = (CryptoInput) obj;

            }
            if (CryptoOutput.isSupported(obj)) {
                cryptoOutput = (CryptoOutput) obj;
            }
        }

        RSAEncryptUtil ezhanghuRsaUtil = getSelfRsaUtil();
        // 需要注意在发送到第三方服务时，需要在进入切面前提前将目标机构编码放入cryptoDto
        encryptContext(cryptoInput, ezhanghuRsaUtil);

        // 创建副本传回被代理的方法
        CryptoDto replicateCryptoInput = new CryptoDto();
        BeanUtils.copyProperties(cryptoInput, replicateCryptoInput);

        Object result = joinPoint.proceed();
        if(result != null){
            if("000000".equals((String)result) || "000407".equals((String)result)){
                log.info("返回的code 为000000，进行解密");
                if(!StringUtils.isEmpty(cryptoOutput.getEncryptBizContext())) {
                    decryptContext(cryptoOutput, ezhanghuRsaUtil);
                }
            }else{
                log.info("返回的code 为"+(String)result+"，不需要进行解密");
            }
        }else{
            log.info("返回的code 为空，不进行解密");
            return null;
        }
        return (String)result;

//        if(proceed == null) {
//            return null;
//        }
//        if (!CryptoDto.isSupported(proceed)) {
//            throw new EacException("aop代理接受到的返回类型与期望的类型不匹配");
//        }
//        CryptoDto cryptoOutput = (CryptoDto) proceed;


    }

    /**
     * 对cryptoDto中的加密字段进行解密
     *
     * @param cryptoDto
     * @param ezhanghuRsaUtil
     */
    private void decryptContext(CryptoDto cryptoDto, RSAEncryptUtil ezhanghuRsaUtil) {
        if (cryptoDto == null
                || StringUtils.isEmpty(cryptoDto.getEncryptKey())
                || StringUtils.isEmpty(cryptoDto.getEncryptBizContext())
                || StringUtils.isEmpty(cryptoDto.getSignature())
                || StringUtils.isEmpty(cryptoDto.getEncryptOrgCode())) {
            throw new EacException(" 必须传入的参数不能为空");
        }
        log.info("解密前的业务参数为 [{}]", cryptoDto.getEncryptBizContext());

        log.info("解密前的随机密钥 [{}]", cryptoDto.getEncryptKey());

        log.info("解密前的组织机构编码 [{}]", cryptoDto.getEncryptOrgCode());

        log.info("解密前的签名报文 [{}]", cryptoDto.getSignature());

        //  如果参数通过post传递，需要url decode,如果通过get传递，不需要url decode
        //  String urlDecodeContent = URLDecoder.decode(cryptoInput.getEncryptParams(), "UTF-8");
        byte[] b64DecodeByte = Base64.decodeBase64(cryptoDto.getEncryptKey());
        //  通过ezhanghu的rsa私钥 对随机密钥 进行解密
        String decryptKey = ezhanghuRsaUtil.decryptByte2Str(b64DecodeByte, ezhanghuRsaUtil.getPrivateKey());
        cryptoDto.setRawKey(decryptKey);
        log.info("解密后的随机密钥为 [{}]", decryptKey);

        //  用解密后的随机密钥对"业务数据"进行对称的aes解密
        String decryptBizContext = AESEncryptUtil.decode(cryptoDto.getEncryptBizContext(), decryptKey);
        cryptoDto.setRawBizContext(decryptBizContext);
        log.info("解密后的业务参数为 [{}]", decryptBizContext);

        //  同样，需要用解密后的随机密钥对"组织机构代码"进行对称的aes解密
        String decryptOrgCode = AESEncryptUtil.decode(cryptoDto.getEncryptOrgCode(), decryptKey);
        cryptoDto.setThirdPartyOrgCode(decryptOrgCode);
        log.info("解密后的组织机构代码为 [{}]", decryptOrgCode);
        //  通过解密后的第三方组织机构代码，初始化第三方机构的公钥，进行验签
        RSAEncryptUtil thirdPartyRsaUtil = getThirdPartyRsaUtil();

        //  将签名转换为二进制流
        // String urlDecodeSignature = URLDecoder.decode(cryptoInput.getSignature(), "UTF-8");
        byte[] signatureByte = Base64.decodeBase64(cryptoDto.getSignature());
        //  对 解密后的报文 通过签名 和 对应银行的公钥进行验签操作
        boolean isValid = thirdPartyRsaUtil.verifySignature(decryptBizContext, signatureByte, thirdPartyRsaUtil.getPublicKey());
        log.info("解密的验签结果为 [{}]", isValid);
        //  如果验签失败，报文来源存在问题
        if(!isValid) {
            throw new EacException("签名验证失败");
        }
    }

    /**
     * 对cryptoDto中的字段进行加密
     *
     * @param cryptoDto
     * @param ezhanghuRsaUtil
     *
     */
    private void encryptContext(CryptoDto cryptoDto, RSAEncryptUtil ezhanghuRsaUtil) {

        if(StringUtils.isEmpty(cryptoDto.getRawBizContext())) {
            throw new EacException("执行加密的业务数据为空");
        }
        if(StringUtils.isEmpty(cryptoDto.getThirdPartyOrgCode())) {
            throw new EacException("发送报文的目标机构的机构代码为空");
        }

        //  生成随机密钥，
        String randomKey = RandomUtil.getMixStr(16);
        log.info("加密前生成的随机密钥为 [{}]", randomKey);
        //对返回的业务内容进行aes加密
        log.info("加密前的业务数据为 [{}]", cryptoDto.getRawBizContext());
        String encryptRes = AESEncryptUtil.encode(cryptoDto.getRawBizContext(), randomKey);
        cryptoDto.setEncryptBizContext(encryptRes);
        log.info("加密后的业务数据为 [{}]", encryptRes);
        //对自己的组织机构编码进行aes加密
        String organId = companyPreOpenAccountEntService.getRSACodeOrganId(rsaCode);
        log.info("加密前的组织机构代码为 [{}]", organId);
        String encryptOrgCode = AESEncryptUtil.encode(organId, randomKey);
        cryptoDto.setEncryptOrgCode(encryptOrgCode);
        log.info("加密后的组织机构代码为 [{}]", encryptOrgCode);
        //  初始化第三方组织机构的公钥，进行加密
        RSAEncryptUtil thirdPartyRsaUtil = getThirdPartyRsaUtil();
        PublicKey specificPublicKey = thirdPartyRsaUtil.getPublicKey();
        if(specificPublicKey == null) {
            throw new EacException("无法获取" + cryptoDto.getThirdPartyOrgCode() + "的公钥");
        }

        //  对随机密钥利用第三方机构公钥进行rsa加密
        byte[] encryptKeyByte = thirdPartyRsaUtil.encryptStr2Byte(randomKey, thirdPartyRsaUtil.getPublicKey());
        String encryptKey = Base64.encodeBase64String(encryptKeyByte);
        cryptoDto.setEncryptKey(encryptKey);
        log.info("加密后的随机密钥为 [{}]", encryptKey);
        //  对未加密的原始业务数据进行sha1withrsa签名
        byte[] signature = ezhanghuRsaUtil.sign(cryptoDto.getRawBizContext(), ezhanghuRsaUtil.getPrivateKey());
        String signatureStr = Base64.encodeBase64String(signature);
        log.info("加密过程中生成的业务数据签名为 [{}]", signature);
        cryptoDto.setSignature(signatureStr);

    }


}

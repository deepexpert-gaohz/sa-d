package com.ideatech.ams.kyc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.kyc.dto.CarrierOperatorDto;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.proof.dto.ProofReportDto;
import com.ideatech.ams.system.proof.enums.ProofType;
import com.ideatech.ams.system.proof.service.ProofReportService;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.ams.system.user.service.UserService;
import com.ideatech.common.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.net.URLDecoder;

//@Service
@Transactional
public class CarrierOperatorServiceImpl implements CarrierOperatorService {
    // 运营商校验测试key
    @Value("${saic.api.key:111}")
    private String carrierOperatorKey;

    @Autowired
    private SaicRequestService saicRequestService;

    @Value("${ams.company.writeMoney}")
    private boolean writeMoney;

    @Autowired
    private ConfigService configService;
    @Autowired
    private ProofReportService proofReportService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;

    @Override
    public CarrierOperatorDto getCarrierOperatorResult(CarrierOperatorDto dto) {
        return getCarrierOperatorResultCommon(dto, "");
    }

    @Override
    public CarrierOperatorDto getCarrierOperatorResult(CarrierOperatorDto dto, String username) {
        return getCarrierOperatorResultCommon(dto, username);
    }

    private CarrierOperatorDto getCarrierOperatorResultCommon(CarrierOperatorDto dto, String username) {
        CarrierOperatorDto carrierOperatorDto = new CarrierOperatorDto();
        String responseStr = null;
        // 各API需要参数（详细参考画面-各API参数不同）
        String strparam = "";
        String encryptParam = "";

        if(StringUtils.isBlank(dto.getName())) {
            carrierOperatorDto.setResult("姓名不能为空");
            return carrierOperatorDto;
        } else if(StringUtils.isBlank(dto.getMobile())) {
            carrierOperatorDto.setResult("手机号不能为空");
            return carrierOperatorDto;
        } else if(StringUtils.isBlank(dto.getCardno())) {
            carrierOperatorDto.setResult("身份证号不能为空");
            return carrierOperatorDto;
        } else if(!RegexUtils.isPhoneNumber(dto.getMobile())) {
            carrierOperatorDto.setResult("不是合法的手机号码");
            return carrierOperatorDto;
        } else if(StringUtils.isNotBlank(IDCardUtils.IDCardValidate(dto.getCardno()))) {
            carrierOperatorDto.setResult(IDCardUtils.IDCardValidate(dto.getCardno()));
            return carrierOperatorDto;
        }

        // 加密
        try {
            strparam = "name=" + URLDecoder.decode(dto.getName(), "UTF-8") + "&mobile=" + dto.getMobile() + "&cardno=" + dto.getCardno();
            DesEncrypter desEncrypter = new DesEncrypter(carrierOperatorKey);
            encryptParam = desEncrypter.encrypt(strparam);
            //代理模式访问idp接口
            responseStr = saicRequestService.getCarrierResponseStr(encryptParam);
        } catch (Exception e) {
            carrierOperatorDto.setStatus("fail");
            carrierOperatorDto.setReason(e.getMessage());
            return carrierOperatorDto;
        }

        if(StringUtils.isNotBlank(responseStr)) {
            JSONObject parseObject = JSON.parseObject(responseStr);
            if(StringUtils.isBlank(parseObject.getString("status"))) {   //新的数据格式成功返回认证结果的只返回result参数部分
                carrierOperatorDto.setResult(parseObject.getString("validateMsg"));
                carrierOperatorDto.setStatus("success");
            } else {  //兼容老的数据格式和新的数据格式没有返回认证结果的
                carrierOperatorDto.setStatus(parseObject.getString("status"));
                if(StringUtils.isNotBlank(parseObject.getString("result"))) {
                    JSONObject result = JSON.parseObject(parseObject.getString("result"));
                    carrierOperatorDto.setResult(result.getString("validateMsg"));
                } else {
                    carrierOperatorDto.setResult(parseObject.getString("reason"));
                }
            }
        } else {
            carrierOperatorDto.setReason("获取结果失败,请检查输入参数是否合法");
            carrierOperatorDto.setStatus("fail");
        }

        //记录运营商费用
        if(writeMoney && "success".equals(carrierOperatorDto.getStatus())) {
            ProofReportDto accountProofReportDto = new ProofReportDto();

            if (StringUtils.isBlank(username)){
                username = SecurityUtils.getCurrentUsername();
                if (StringUtils.isBlank(username)) {
                    UserDto userDto = userService.findById(2L);
                    OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
                    accountProofReportDto.setTypeDetil("接口方式手机号实名校验");
                    accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                    accountProofReportDto.setUsername(userDto.getUsername());
                    accountProofReportDto.setProofBankName(organizationDto.getName());
                } else {
                    OrganizationDto organizationDto = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());
                    accountProofReportDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                    accountProofReportDto.setUsername(username);
                    accountProofReportDto.setTypeDetil("账管系统手机号实名校验");
                    accountProofReportDto.setProofBankName(organizationDto.getName());
                }
            } else {  //接口传入用户名和机构号
                UserDto userDto = userService.findByUsername(username);
                OrganizationDto organizationDto = organizationService.findById(userDto.getOrgId());
                accountProofReportDto.setTypeDetil("接口方式手机号实名校验");
                accountProofReportDto.setOrganFullId(organizationDto.getFullId());
                accountProofReportDto.setUsername(username);
                accountProofReportDto.setProofBankName(organizationDto.getName());
            }

            accountProofReportDto.setPhone(dto.getMobile());
            ConfigDto configDto = configService.findOneByConfigKey("phoneMoney");
            if(configDto!=null){
                accountProofReportDto.setPrice(configDto.getConfigValue());
            }else{
                accountProofReportDto.setPrice("0");
            }
            accountProofReportDto.setType(ProofType.PHONE);
            accountProofReportDto.setResult(carrierOperatorDto.getStatus());
            accountProofReportDto.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
            proofReportService.save(accountProofReportDto);
        }

        return carrierOperatorDto;
    }
}

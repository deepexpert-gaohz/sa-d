package com.ideatech.ams.service;

import com.ideatech.ams.apply.dto.RSACode;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.entity.ConfigPo;
import com.ideatech.common.converter.ConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Description 配置更新后的服务层
 * @Author wanghongjie
 * @Date 2018/8/29
 **/
@Service
@Transactional
public class ConfigUpdateService {

    @Autowired
    private RSACode rsaCode;

    public void updateRSACode(List<ConfigDto> configDtos){
        for (ConfigDto configDto : configDtos) {
            String configKey = configDto.getConfigKey();
            if("thirdParthPublicKey".equals(configKey)){
                rsaCode.setThirdPublicKey(configDto.getConfigValue());
            }else if("organid".equals(configKey)){
                rsaCode.setOrganid(configDto.getConfigValue());
            }
        }
    }
}

package com.ideatech.ams.system.notice.service;

import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private ConfigService configService;

    @Override
    public Map<String, Boolean> setNoticeConfigPermission() {
        Map<String, Boolean> map = new HashMap<>();
        Boolean resvrConfigEnabled = true;
        Boolean tempConfigEnabled = true;
        Boolean fileDueConfigEnabled = true;
        Boolean legalDueConfigEnabled = true;
        Boolean operatorDueConfigEnabled = true;

        List<ConfigDto> resvrConfig = configService.findByKey("resvrConfigEnabled");
        if (CollectionUtils.isNotEmpty(resvrConfig)) {
            resvrConfigEnabled = Boolean.valueOf(resvrConfig.get(0).getConfigValue());
        }
        List<ConfigDto> tempConfig  = configService.findByKey("tempConfigEnabled");
        if (CollectionUtils.isNotEmpty(tempConfig)) {
            tempConfigEnabled = Boolean.valueOf(tempConfig.get(0).getConfigValue());
        }
        List<ConfigDto> fileDueConfig  = configService.findByKey("fileDueConfigEnabled");
        if (CollectionUtils.isNotEmpty(fileDueConfig)) {
            fileDueConfigEnabled = Boolean.valueOf(fileDueConfig.get(0).getConfigValue());
        }
        List<ConfigDto> legalDueConfig  = configService.findByKey("legalDueConfigEnabled");
        if (CollectionUtils.isNotEmpty(legalDueConfig)) {
            legalDueConfigEnabled = Boolean.valueOf(legalDueConfig .get(0).getConfigValue());
        }
        List<ConfigDto> operatorDueConfig  = configService.findByKey("operatorDueConfigEnabled");
        if (CollectionUtils.isNotEmpty(operatorDueConfig)) {
            operatorDueConfigEnabled = Boolean.valueOf(operatorDueConfig .get(0).getConfigValue());
        }

        map.put("resvrConfigEnabled", resvrConfigEnabled);
        map.put("tempConfigEnabled", tempConfigEnabled);
        map.put("fileDueConfigEnabled", fileDueConfigEnabled);
        map.put("legalDueConfigEnabled", legalDueConfigEnabled);
        map.put("operatorDueConfigEnabled", operatorDueConfigEnabled);

        return map;
    }

    @Override
    public Map<String, Boolean> overNoticeConfig() {
        Map<String, Boolean> map = new HashMap<>();
        ConfigDto config1 = configService.findOneByConfigKey("fileOverConfigEnabled");
        ConfigDto config2 = configService.findOneByConfigKey("tempAcctOverConfigEnabled");
        ConfigDto config3 = configService.findOneByConfigKey("legalOverConfigEnabled");
        ConfigDto config4 = configService.findOneByConfigKey("operatorOverConfigEnabled");

        if(config1 != null) {
            map.put("fileOverConfigEnabled", Boolean.valueOf(config1.getConfigValue()));
        } else {
            map.put("fileOverConfigEnabled", true);
        }
        if(config2 != null) {
            map.put("tempAcctOverConfigEnabled", Boolean.valueOf(config2.getConfigValue()));
        } else {
            map.put("tempAcctOverConfigEnabled", true);
        }
        if(config3 != null) {
            map.put("legalOverConfigEnabled", Boolean.valueOf(config3.getConfigValue()));
        } else {
            map.put("legalOverConfigEnabled", true);
        }
        if(config4 != null) {
            map.put("operatorOverConfigEnabled", Boolean.valueOf(config4.getConfigValue()));
        } else {
            map.put("operatorOverConfigEnabled", true);
        }

        return map;
    }

}

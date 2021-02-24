package com.ideatech.ams.system.config.service;

import com.ideatech.ams.system.config.dao.ConfigDao;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.entity.ConfigPo;
import com.ideatech.common.constant.IdeaConstant;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liangding
 * @create 2018-05-15 下午4:24
 **/
@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigDao configDao;

    @Override
    public void save(ConfigDto configDto) {
        ConfigPo configPo = configDao.findByConfigKey(configDto.getConfigKey());
        if (configPo == null) {
            configPo = new ConfigPo();
        }
        ConverterService.convert(configDto, configPo);
        configDao.save(configPo);
    }

    @Override
    public List<ConfigDto> list() {
        List<ConfigPo> all = configDao.findAll();
        return ConverterService.convertToList(all, ConfigDto.class);
    }

    @Override
    public void save(List<ConfigDto> configDtos) {
        for (ConfigDto configDto : configDtos) {
            String configKey = configDto.getConfigKey();
            ConfigPo configPo = configDao.findByConfigKey(configKey);
            if (configPo == null) {
                configPo = new ConfigPo();
            }
            ConverterService.convert(configDto, configPo);
            configDao.save(configPo);
        }
    }

    @Override
    public void saveImageVideoRemind(List<ConfigDto> configDtos) {
        List<ConfigPo> data = configDao.findByConfigKeyStartingWithOrderByConfigKeyDesc("imageVideoRemind");
        if(configDtos.size()>=data.size()){
            //代表双录的提示有增加和更新
            save(configDtos);
        }else{
            //代表需要删除后面的几个
            int sum = data.size()-configDtos.size();
            for (int i=0;i<sum;i++){
                configDao.delete(data.get(i));
            }
        }
    }

    @Transactional()
    @Override
    public void saveDualRecordRemind(List<ConfigDto> configDtos) {
        List<ConfigPo> data = configDao.findByConfigKeyStartingWithOrderByConfigKeyDesc("dualRecordRemind");
        configDao.delete(data);
        save(configDtos);
    }

    @Transactional()
    @Override
    public void saveRiskWarning(List<ConfigDto> configDtos) {
        List<ConfigPo> data = configDao.findByConfigKeyStartingWithOrderByConfigKeyDesc("riskWarning");
        configDao.delete(data);
        save(configDtos);
    }

    @Override
    public List<ConfigDto> findByKey(String configKey) {
        ConfigPo configPo = new ConfigPo();
        configPo.setConfigKey("%" + configKey + "%");
        Example<ConfigPo> example = Example.of(configPo,
                ExampleMatcher.matching()
                        .withMatcher("configKey", ExampleMatcher.GenericPropertyMatchers.contains()));
        List<ConfigPo> all = configDao.findAll(example);
        return ConverterService.convertToList(all, ConfigDto.class);
    }

    @Override
    public List<ConfigDto> findImageVideoRemind(String configKey) {
        List<ConfigPo> data = configDao.findByConfigKeyStartingWithOrderByConfigKeyAsc(configKey);
        return ConverterService.convertToList(data, ConfigDto.class);
    }

    @Override
    public void deleteByConfigKey(String configKey) {
        ConfigPo configPo = configDao.findByConfigKey(configKey);
        if (configPo != null) {
            configDao.delete(configPo);
        }
    }

    @Override
    public Long findOneByKey(String configKey) {
        List<ConfigDto> configList = findByKey(configKey);
        Long processDay = null;
        if (configList.size() > 0) {
            String configValue = configList.get(0).getConfigValue();
            //进行判空   防止null以及“” 进行数字的转换造成报错
            if(StringUtils.isNotBlank(configValue)){
                try {
                    processDay = Long.valueOf(configValue);
                } catch (Exception e) {
                    //ignore exception
                    log.error("config根据configKey查找数据异常，", e);
                }
            }
        }

        return processDay;
    }

    @Override
    public ConfigDto findOneByConfigKey(String configKey) {
        try {
            return ConverterService.convert(configDao.findByConfigKey(configKey), ConfigDto.class);
        } catch (Exception e) {
            log.error("查找配置项" + configKey + "异常", e);
            return null;
        }
    }

    @Override
    public boolean isRunningScheduledIp() {
        ConfigDto configDto = findOneByConfigKey(IdeaConstant.SYSTEM_IP_CONFIG);
        if (configDto != null) {
            return SystemUtils.systemIP(configDto.getConfigValue());
        } else {
            log.info("未指定定时任务运行服务器IP地址");
        }
        return false;
    }

}

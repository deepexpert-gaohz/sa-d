package com.ideatech.ams.system.config.service;

import com.ideatech.ams.system.config.dto.ConfigDto;

import java.util.List;

public interface ConfigService {
    void save(ConfigDto configDto);

    List<ConfigDto> list();

    void save(List<ConfigDto> configDtos);

    void saveImageVideoRemind(List<ConfigDto> configDtos);

    void saveDualRecordRemind(List<ConfigDto> configDtos);

    void saveRiskWarning(List<ConfigDto> configDtos);

    List<ConfigDto> findByKey(String configKey);

    List<ConfigDto> findImageVideoRemind(String configKey);

    void deleteByConfigKey(String configKey);

    Long findOneByKey(String configKey);

    ConfigDto findOneByConfigKey(String configKey);
    /**
     * 判断是否是当前运行定时任务机器IP
     * @return
     */
    boolean isRunningScheduledIp();

}

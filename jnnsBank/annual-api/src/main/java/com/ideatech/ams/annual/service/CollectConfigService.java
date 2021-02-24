package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.CollectConfigDto;

/**
 * @Description 采集配置的服务
 * @Author wanghongjie
 * @Date 2018/8/10
 **/
public interface CollectConfigService {
    CollectConfigDto findByAnnualTaskId(Long annualTaskId);

    void saveCollectConfig(CollectConfigDto collectConfigDto);

}

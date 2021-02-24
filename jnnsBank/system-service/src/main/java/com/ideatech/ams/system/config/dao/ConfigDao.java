package com.ideatech.ams.system.config.dao;

import com.ideatech.ams.system.config.entity.ConfigPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ConfigDao extends JpaRepository<ConfigPo, Long>, JpaSpecificationExecutor<ConfigPo> {

    /**
     * 
     * @param configKey
     * @return
     */
    ConfigPo findTopByConfigKey(String configKey);

    ConfigPo findByConfigKey(String configKey);

    List<ConfigPo> findByConfigKeyStartingWithOrderByConfigKeyDesc(String configKey);

    List<ConfigPo> findByConfigKeyStartingWithOrderByConfigKeyAsc(String configKey);
}

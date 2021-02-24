package com.ideatech.ams.customer.service.newcompany;

import com.ideatech.ams.customer.dao.newcompany.FreshCompanyConfigDao;
import com.ideatech.ams.customer.dto.neecompany.FreshCompanyConfigDto;
import com.ideatech.ams.customer.entity.newcompany.FreshCompanyConfig;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class FreshCompanyConfigServiceImpl implements FreshCompanyConfigService {

    @Autowired
    private FreshCompanyConfigDao freshCompanyConfigDao;

    @Override
    public FreshCompanyConfigDto getConfig() {
        FreshCompanyConfig config = null;
        FreshCompanyConfigDto configDto = null;

        List<FreshCompanyConfig> configList = freshCompanyConfigDao.findAll();
        if(CollectionUtils.isNotEmpty(configList)) {
            config = configList.get(0);
            configDto = new FreshCompanyConfigDto();
            BeanCopierUtils.copyProperties(config, configDto);
        }

        return configDto;
    }

    @Override
    public void saveConfig(FreshCompanyConfigDto dto) {
        FreshCompanyConfig config = new FreshCompanyConfig();

        List<FreshCompanyConfig> configList = freshCompanyConfigDao.findAll();
        if(CollectionUtils.isNotEmpty(configList)) {
            config = configList.get(0);
            config.setProvinceCode(dto.getProvinceCode());
//            config.setTimes(dto.getTimes());
//            config.setExcuteCycle(dto.getExcuteCycle());
            config.setSelectRange(dto.getSelectRange());
            config.setUnlimited(dto.getUnlimited());
            if("months".equals(dto.getSelectRange())) {
                config.setBeginDate(dto.getBeginDate());
                config.setEndDate(dto.getEndDate());
                config.setDayRange("");
            } else if("days".equals(dto.getSelectRange())) {
                config.setDayRange(dto.getDayRange());
                config.setBeginDate("");
                config.setEndDate("");
            }
        } else {
            BeanCopierUtils.copyProperties(dto, config);
        }

        if(StringUtils.isBlank(dto.getDayRange())) {
            config.setDayRange("1");
        }
        if(StringUtils.isBlank(dto.getEndDate())) {
            config.setEndDate(DateUtils.getNowDateShort());
        }

        freshCompanyConfigDao.save(config);

    }
}


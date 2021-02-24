package com.ideatech.ams.customer.service.newcompany;

import com.ideatech.ams.customer.dto.neecompany.FreshCompanyConfigDto;

public interface FreshCompanyConfigService {
    FreshCompanyConfigDto getConfig();

    void saveConfig(FreshCompanyConfigDto dto);
}

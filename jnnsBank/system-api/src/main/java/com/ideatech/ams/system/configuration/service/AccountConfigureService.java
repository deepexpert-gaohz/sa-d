package com.ideatech.ams.system.configuration.service;

import com.ideatech.ams.system.configuration.dto.AccountConfigureDto;
import com.ideatech.common.service.BaseService;

public interface AccountConfigureService extends BaseService<AccountConfigureDto> {

    void del(Long id);

    AccountConfigureDto query(String acctType,String depositorType,String operateType);
}

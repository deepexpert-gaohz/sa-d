package com.ideatech.ams.system.eav.service;

import com.ideatech.ams.system.eav.dto.EavDto;
import com.ideatech.common.service.BaseService;

import java.util.Map;

public interface EavService extends BaseService<EavDto> {
    Map<String, String> findByEntityIdAndDocCode(Long entityId, String docName);

    void save(Long entityId, String docName, Map<String,String> attrs);
}

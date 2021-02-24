package com.ideatech.ams.system.whitelist.service;

import com.ideatech.ams.system.whitelist.dto.WhiteListDto;
import com.ideatech.ams.system.whitelist.dto.WhiteListSearchDto;
import com.ideatech.common.dto.ResultDto;

import java.util.List;

public interface WhiteListService {

    List<WhiteListDto> list();

    WhiteListSearchDto search(WhiteListSearchDto dto);

    ResultDto create(WhiteListDto dto);

    ResultDto update(WhiteListDto dto);

    void delete(Long id);

    WhiteListDto getById(Long id);

    WhiteListDto getByEntName(String name);

    WhiteListDto getByEntnameAndOrgId(String name,Long orgId);

    long count();
}

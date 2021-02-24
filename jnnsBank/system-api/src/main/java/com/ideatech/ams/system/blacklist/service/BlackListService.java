package com.ideatech.ams.system.blacklist.service;

import com.ideatech.ams.system.blacklist.dto.BlackListEntryDto;
import com.ideatech.ams.system.blacklist.dto.BlackListSearchDto;
import com.ideatech.ams.system.blacklist.enums.BlackListResultEnum;
import com.ideatech.common.dto.ResultDto;

import java.util.List;

public interface BlackListService {
    List<BlackListEntryDto> list();

    ResultDto create(BlackListEntryDto blackListEntryDto);

    void update(BlackListEntryDto blackListEntryDto);

    void delete(Long id);

    BlackListEntryDto getById(Long id);

    BlackListSearchDto search(BlackListSearchDto blackListSearchDto);

    List<BlackListEntryDto> findByName(String entName);

    /**
     * 找出其中含有白名单的对象
     *
     * @param entName
     * @return BlackListEntryDto
     */
    BlackListResultEnum findByNameMixWhite(String entName);
}

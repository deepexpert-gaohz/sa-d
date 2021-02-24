package com.ideatech.ams.apply.service;

import com.ideatech.ams.apply.dto.EzhMessageDto;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

public interface EzhMessageService {

    TableResultResponse<EzhMessageDto> query(EzhMessageDto dto, Pageable pageable);

    EzhMessageDto getOne(Long id);

    void edit(EzhMessageDto dto);
}

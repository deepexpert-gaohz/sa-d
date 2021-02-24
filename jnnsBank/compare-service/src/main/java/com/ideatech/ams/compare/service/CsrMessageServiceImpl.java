package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dao.CsrMessageDao;
import com.ideatech.ams.compare.dto.CsrMessageDto;
import com.ideatech.ams.compare.entity.CsrMessage;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019/6/28.
 */

@Service
@Slf4j
public class CsrMessageServiceImpl extends BaseServiceImpl<CsrMessageDao, CsrMessage, CsrMessageDto> implements CsrMessageService {
    @Override
    public TableResultResponse page(CsrMessageDto csrMessageDto, Pageable pageable) {
        Page<CsrMessage> csrMessagesPage = getBaseDao().findAll(pageable);
        return new TableResultResponse((int)csrMessagesPage.getTotalElements(), ConverterService.convertToList(csrMessagesPage.getContent(),CsrMessageDto.class));
    }
}

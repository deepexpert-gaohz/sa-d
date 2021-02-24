package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.CsrMessageDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

/**
 * @author jzh
 * @date 2019/6/28.
 */
public interface CsrMessageService extends BaseService<CsrMessageDto> {

    TableResultResponse page(CsrMessageDto csrMessageDto, Pageable pageable);
}

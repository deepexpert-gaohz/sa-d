package com.ideatech.ams.system.trace.service;

import com.ideatech.ams.system.trace.dto.UserTraceDTO;
import com.ideatech.ams.system.trace.dto.UserTraceSearchDTO;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

/**
 * @author jzh
 * @date 2019-10-30.
 */
public interface UserTraceService extends BaseService<UserTraceDTO> {

    TableResultResponse query(UserTraceSearchDTO userTraceSearchDTO, Pageable pageable);
}

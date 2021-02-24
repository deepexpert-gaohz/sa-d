package com.ideatech.ams.system.trace.service;

import com.ideatech.ams.system.trace.dao.UserTraceDao;
import com.ideatech.ams.system.trace.dto.UserTraceDTO;
import com.ideatech.ams.system.trace.dto.UserTraceSearchDTO;
import com.ideatech.ams.system.trace.entity.UserTrace;
import com.ideatech.ams.system.trace.spec.UserTraceSpec;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019-10-30.
 */

@Service
public class UserTraceServiceImpl extends BaseServiceImpl<UserTraceDao, UserTrace, UserTraceDTO> implements UserTraceService {

    @Override
    public TableResultResponse query(UserTraceSearchDTO userTraceSearchDTO, Pageable pageable) {
        Page<UserTrace> userTracePage = getBaseDao().findAll(new UserTraceSpec(userTraceSearchDTO),pageable);
        return new TableResultResponse<UserTraceDTO>((int) userTracePage.getTotalElements(),ConverterService.convertToList(userTracePage.getContent(),UserTraceDTO.class));
    }
}

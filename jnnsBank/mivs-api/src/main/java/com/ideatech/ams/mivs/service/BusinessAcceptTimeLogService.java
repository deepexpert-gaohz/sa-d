package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.BusinessAcceptTimeLogDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

/**
 * @author jzh
 * @date 2019/7/30.
 */
public interface BusinessAcceptTimeLogService extends BaseService<BusinessAcceptTimeLogDto> {

    TableResultResponse<BusinessAcceptTimeLogDto> query(BusinessAcceptTimeLogDto businessAcceptTimeLogDto, Pageable pageable);
}

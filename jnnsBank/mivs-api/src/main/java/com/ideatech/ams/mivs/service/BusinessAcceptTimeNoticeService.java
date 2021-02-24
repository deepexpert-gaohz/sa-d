package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.BusinessAcceptTimeLogDto;
import com.ideatech.ams.mivs.dto.bnd.BusinessAcceptTimeNoticeDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

/**
 * @author jzh
 * @date 2019/7/30.
 */
public interface BusinessAcceptTimeNoticeService extends BaseService<BusinessAcceptTimeNoticeDto> {

    TableResultResponse<BusinessAcceptTimeNoticeDto> query(BusinessAcceptTimeNoticeDto businessAcceptTimeNoticeDto, Pageable pageable);
}

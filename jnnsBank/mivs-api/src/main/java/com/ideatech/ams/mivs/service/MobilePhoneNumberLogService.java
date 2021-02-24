package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.MobilePhoneNumberLogDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

/**
 * @author jzh
 * @date 2019/7/25.
 */
public interface MobilePhoneNumberLogService extends BaseService<MobilePhoneNumberLogDto> {
    TableResultResponse<MobilePhoneNumberLogDto> query(MobilePhoneNumberLogDto mobilePhoneNumberLogDto, Pageable pageable);
}

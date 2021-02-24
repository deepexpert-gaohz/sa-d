package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.RegisterInformationLogDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

/**
 * @author jzh
 * @date 2019/7/31.
 *
 */

public interface RegisterInformationLogService extends BaseService<RegisterInformationLogDto> {

    TableResultResponse<RegisterInformationLogDto> query(RegisterInformationLogDto registerInformationLogDto, Pageable pageable);
}

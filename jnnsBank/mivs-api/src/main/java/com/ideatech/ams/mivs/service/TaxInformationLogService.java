package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.TaxInformationLogDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

/**
 * @author jzh
 * @date 2019/7/29.
 */
public interface TaxInformationLogService extends BaseService<TaxInformationLogDto> {

    TableResultResponse<TaxInformationLogDto> query(TaxInformationLogDto taxInformationLogDto, Pageable pageable);
}

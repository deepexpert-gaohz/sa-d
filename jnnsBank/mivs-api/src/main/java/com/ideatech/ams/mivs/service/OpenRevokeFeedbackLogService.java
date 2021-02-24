package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.OpenRevokeFeedbackLogDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

/**
 * @author jzh
 * @date 2019-08-08.
 */
public interface OpenRevokeFeedbackLogService extends BaseService<OpenRevokeFeedbackLogDto> {
    TableResultResponse<OpenRevokeFeedbackLogDto> query(OpenRevokeFeedbackLogDto openRevokeFeedbackLogDto, Pageable pageable);
}

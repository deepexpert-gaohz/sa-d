package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.CommonFeedbackLogDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

/**
 * @author jzh
 * @date 2019-08-06.
 */
public interface CommonFeedbackLogService extends BaseService<CommonFeedbackLogDto> {
    TableResultResponse query(CommonFeedbackLogDto commonFeedbackLogDto, Pageable pageable);
}

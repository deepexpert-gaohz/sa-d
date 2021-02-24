package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.bnd.BATNServiceInformationDto;
import com.ideatech.common.service.BaseService;

import java.util.List;

/**
 * @author jzh
 * @date 2019-09-20.
 */
public interface BATNServiceInformationService extends BaseService<BATNServiceInformationDto> {

    List<BATNServiceInformationDto> findByNoticeId(Long noticeId);
}

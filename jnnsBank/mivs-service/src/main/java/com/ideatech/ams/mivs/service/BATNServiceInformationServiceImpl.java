package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.BATNServiceInformationDao;
import com.ideatech.ams.mivs.dto.bnd.BATNServiceInformationDto;
import com.ideatech.ams.mivs.entity.BATNServiceInformation;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jzh
 * @date 2019-09-20.
 */

@Service
public class BATNServiceInformationServiceImpl extends BaseServiceImpl<BATNServiceInformationDao, BATNServiceInformation, BATNServiceInformationDto> implements BATNServiceInformationService {
    @Override
    public List<BATNServiceInformationDto> findByNoticeId(Long noticeId) {
        return ConverterService.convertToList(getBaseDao().findAllByNoticeId(noticeId),BATNServiceInformationDto.class);
    }
}

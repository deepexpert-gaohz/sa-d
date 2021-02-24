package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.MobilePhoneNumberLogDao;
import com.ideatech.ams.mivs.dto.MobilePhoneNumberLogDto;
import com.ideatech.ams.mivs.entity.MobilePhoneNumberLog;
import com.ideatech.ams.mivs.spec.MobilePhoneNumberLogSpec;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019/7/25.
 */

@Service
public class MobilePhoneNumberLogServiceImpl extends BaseServiceImpl<MobilePhoneNumberLogDao, MobilePhoneNumberLog, MobilePhoneNumberLogDto> implements MobilePhoneNumberLogService{
    @Override
    public TableResultResponse<MobilePhoneNumberLogDto> query(MobilePhoneNumberLogDto mobilePhoneNumberLogDto, Pageable pageable) {
        mobilePhoneNumberLogDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        MobilePhoneNumberLogSpec mobilePhoneNumberLogSpec = new MobilePhoneNumberLogSpec(mobilePhoneNumberLogDto);
        Page<MobilePhoneNumberLog> phoneNumberLogPage = getBaseDao().findAll(mobilePhoneNumberLogSpec,pageable);
        return new TableResultResponse((int) phoneNumberLogPage.getTotalElements(), ConverterService.convertToList(phoneNumberLogPage.getContent(),MobilePhoneNumberLogDto.class));
    }
}

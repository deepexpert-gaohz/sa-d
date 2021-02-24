package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.mivs.dto.ad.AnnouncementInformationDto;
import com.ideatech.ams.mivs.dto.bnd.BusinessAcceptTimeNoticeDto;
import com.ideatech.ams.mivs.dto.end.EnterpriseAbnormalNoticeDto;
import com.ideatech.ams.mivs.dto.ind.InstitutionAbnormalNoticeDto;
import com.ideatech.ams.mivs.service.QueryMsgService;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

/**
 * @author jzh
 * @date 2019-09-02.
 */
public class DefaultQueryMsgServiceImpl implements QueryMsgService {

    @Override
    public TableResultResponse<AnnouncementInformationDto> queryAnnouncementInformation(AnnouncementInformationDto announcementInformationDto, Pageable pageable) {
        return null;
    }

    @Override
    public TableResultResponse<InstitutionAbnormalNoticeDto> queryInstitutionAbnormalNotice(InstitutionAbnormalNoticeDto institutionAbnormalNoticeDto, Pageable pageable) {
        return null;
    }

    @Override
    public TableResultResponse<EnterpriseAbnormalNoticeDto> queryEnterpriseAbnormalNotice(EnterpriseAbnormalNoticeDto enterpriseAbnormalNoticeDto, Pageable pageable) {
        return null;
    }

    @Override
    public TableResultResponse<EnterpriseAbnormalNoticeDto> queryBusinessAcceptTimeNotice(BusinessAcceptTimeNoticeDto businessAcceptTimeNoticeDto, Pageable pageable) {
        return null;
    }
}

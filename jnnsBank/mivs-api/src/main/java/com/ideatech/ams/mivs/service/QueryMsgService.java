package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.ad.AnnouncementInformationDto;
import com.ideatech.ams.mivs.dto.bnd.BusinessAcceptTimeNoticeDto;
import com.ideatech.ams.mivs.dto.end.EnterpriseAbnormalNoticeDto;
import com.ideatech.ams.mivs.dto.ind.InstitutionAbnormalNoticeDto;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

/**
 * 查询报文（提供给现场：针对MIVS向行内系统推送的数据不入库到本账管系统，通过该接口直接向行内系统查询数据。）
 * @author jzh
 * @date 2019-09-02.
 */
public interface QueryMsgService {

    /**
     * 查询公告信息
     * @param announcementInformationDto
     * @param pageable
     * @return
     */
    TableResultResponse<AnnouncementInformationDto> queryAnnouncementInformation(AnnouncementInformationDto announcementInformationDto, Pageable pageable);

    /**
     * 查询机构异常核查通知报文
     * @param institutionAbnormalNoticeDto
     * @param pageable
     * @return
     */
    TableResultResponse<InstitutionAbnormalNoticeDto> queryInstitutionAbnormalNotice(InstitutionAbnormalNoticeDto institutionAbnormalNoticeDto, Pageable pageable);

    /**
     * 查询企业异常核查通知报文
     * @param enterpriseAbnormalNoticeDto
     * @param pageable
     * @return
     */
    TableResultResponse<EnterpriseAbnormalNoticeDto> queryEnterpriseAbnormalNotice(EnterpriseAbnormalNoticeDto enterpriseAbnormalNoticeDto, Pageable pageable);

    /**
     * 查询业务受理时间通知报文
     * @param businessAcceptTimeNoticeDto
     * @param pageable
     * @return
     */
    TableResultResponse<EnterpriseAbnormalNoticeDto> queryBusinessAcceptTimeNotice(BusinessAcceptTimeNoticeDto businessAcceptTimeNoticeDto, Pageable pageable);
}

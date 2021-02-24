package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.ind.InstitutionAbnormalNoticeDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

/**
 * @author jzh
 * @date 2019/7/24.
 * 机构异常核查通知service
 */
public interface InstitutionAbnormalNoticeService extends BaseService<InstitutionAbnormalNoticeDto> {

    TableResultResponse<InstitutionAbnormalNoticeDto> query(InstitutionAbnormalNoticeDto institutionAbnormalNoticeDto,Pageable pageable);
}

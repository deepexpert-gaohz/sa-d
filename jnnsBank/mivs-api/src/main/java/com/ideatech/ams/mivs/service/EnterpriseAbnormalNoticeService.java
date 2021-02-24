package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.end.EnterpriseAbnormalNoticeDto;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

/**
 * @author jzh
 * @date 2019/7/25.
 * 企业异常核查通知
 */


public interface EnterpriseAbnormalNoticeService extends BaseService<EnterpriseAbnormalNoticeDto> {

    TableResultResponse<EnterpriseAbnormalNoticeDto> query(EnterpriseAbnormalNoticeDto institutionAbnormalNoticeDto, Pageable pageable);
}

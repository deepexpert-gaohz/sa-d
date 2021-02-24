package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.EnterpriseAbnormalNoticeDao;
import com.ideatech.ams.mivs.dto.end.EnterpriseAbnormalNoticeDto;
import com.ideatech.ams.mivs.entity.EnterpriseAbnormalNotice;
import com.ideatech.ams.mivs.spec.EnterpriseAbnormalNoticeSpec;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019/7/25.
 */

@Service
public class EnterpriseAbnormalNoticeServiceImpl extends BaseServiceImpl<EnterpriseAbnormalNoticeDao, EnterpriseAbnormalNotice, EnterpriseAbnormalNoticeDto> implements EnterpriseAbnormalNoticeService{

    @Override
    public TableResultResponse<EnterpriseAbnormalNoticeDto> query(EnterpriseAbnormalNoticeDto institutionAbnormalNoticeDto, Pageable pageable) {
        EnterpriseAbnormalNoticeSpec enterpriseAbnormalNoticeSpec = new EnterpriseAbnormalNoticeSpec(institutionAbnormalNoticeDto);
        Page<EnterpriseAbnormalNotice> page= getBaseDao().findAll(enterpriseAbnormalNoticeSpec,pageable);
        return new TableResultResponse((int)page.getTotalElements(),page.getContent());
    }
}

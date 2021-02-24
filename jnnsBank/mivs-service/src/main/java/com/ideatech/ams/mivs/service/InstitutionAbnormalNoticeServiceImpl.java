package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.InstitutionAbnormalNoticeDao;
import com.ideatech.ams.mivs.dto.ind.InstitutionAbnormalNoticeDto;
import com.ideatech.ams.mivs.entity.InstitutionAbnormalNotice;
import com.ideatech.ams.mivs.spec.InstitutionAbnormalNoticeSpec;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019/7/24.
 */

@Service
public class InstitutionAbnormalNoticeServiceImpl extends BaseServiceImpl<InstitutionAbnormalNoticeDao, InstitutionAbnormalNotice, InstitutionAbnormalNoticeDto> implements InstitutionAbnormalNoticeService{

    @Override
    public TableResultResponse<InstitutionAbnormalNoticeDto> query(InstitutionAbnormalNoticeDto institutionAbnormalNoticeDto, Pageable pageable) {
        InstitutionAbnormalNoticeSpec institutionAbnormalNoticeSpec = new InstitutionAbnormalNoticeSpec(institutionAbnormalNoticeDto);
        Page<InstitutionAbnormalNotice> page= getBaseDao().findAll(institutionAbnormalNoticeSpec,pageable);
        return new TableResultResponse((int)page.getTotalElements(),page.getContent());
    }
}

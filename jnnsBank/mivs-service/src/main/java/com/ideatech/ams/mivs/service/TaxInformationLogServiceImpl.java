package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dao.TaxInformationLogDao;
import com.ideatech.ams.mivs.dto.TaxInformationLogDto;
import com.ideatech.ams.mivs.entity.TaxInformationLog;
import com.ideatech.ams.mivs.spec.TaxInformationLogSpec;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019/7/29.
 */

@Service
public class TaxInformationLogServiceImpl extends BaseServiceImpl<TaxInformationLogDao, TaxInformationLog, TaxInformationLogDto>implements TaxInformationLogService {
    @Override
    public TableResultResponse<TaxInformationLogDto> query(TaxInformationLogDto taxInformationLogDto, Pageable pageable) {
        taxInformationLogDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        TaxInformationLogSpec taxInformationLogSpec = new TaxInformationLogSpec(taxInformationLogDto);
        Page<TaxInformationLog> taxInformationLogPage = getBaseDao().findAll(taxInformationLogSpec,pageable);
        return new TableResultResponse((int) taxInformationLogPage.getTotalElements(), ConverterService.convertToList(taxInformationLogPage.getContent(),TaxInformationLogDto.class));
    }
}

package com.ideatech.ams.system.template.service;

import com.ideatech.ams.system.template.dto.TemplateDto;
import com.ideatech.ams.system.template.dto.TemplateSearchDto;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.CompanyAcctType;
import com.ideatech.common.enums.DepositorType;
import com.ideatech.common.service.BaseService;

import java.util.List;

public interface TemplateService extends BaseService<TemplateDto> {
    TemplateSearchDto search(TemplateSearchDto searchDto);

    List<String> listTemplateName(BillType billType, DepositorType depositorType);

    List<String> listTemplate(BillType billType, DepositorType depositorType,CompanyAcctType acctType);

    TemplateDto findByBillTypeAndDepositorTypeAndTemplateName(BillType billType, DepositorType depositorType, String templateName);

    TemplateDto findByBillTypeAndDepositorType(BillType billType, DepositorType depositorType);

    TemplateDto findByTemplateName(String templateName);
}

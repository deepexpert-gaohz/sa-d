package com.ideatech.ams.system.template.dto;

import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.CompanyAcctType;
import com.ideatech.common.enums.DepositorType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liangding
 * @create 2018-07-11 下午3:45
 **/
@Data
public class TemplateSearchDto extends PagingDto<TemplateDto> implements Serializable {
    private BillType billType;
    private DepositorType depositorType;
    private String templateName;
    private CompanyAcctType acctType;
}

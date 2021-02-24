package com.ideatech.ams.system.template.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.CompanyAcctType;
import com.ideatech.common.enums.DepositorType;
import lombok.Data;

/**
 * @author liangding
 * @create 2018-07-11 上午10:47
 **/
@Data
public class TemplateDto extends BaseMaintainableDto {
    private Long id;

    private BillType billType;

    private DepositorType depositorType;

    private String templateName;

    private String fileName;

    private CompanyAcctType acctType;

    @JsonIgnore
    private byte[] templaeContent;
}

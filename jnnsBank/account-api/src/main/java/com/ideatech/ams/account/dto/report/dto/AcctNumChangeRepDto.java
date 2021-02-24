package com.ideatech.ams.account.dto.report.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class AcctNumChangeRepDto extends PagingDto<AcctNumChangeRepDto> {
    private String org;

    private String totalData;

    private String upDateData;

}

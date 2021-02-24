package com.ideatech.ams.risk.tableManager.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class TableInfoSearchDto extends PagingDto<TableInfoDto> {
    private String cname;//中文名称
    private String xtly;//系统来源
    private String ename;//英文名称
}

package com.ideatech.ams.risk.tableManager.dto;

import lombok.Data;

@Data
public class TableInfoDto {
    private String cname;//中文名称
    private String xtly;//系统来源
    private String ename;//英文名称
    private String bz;//备注
    private Long id;

}

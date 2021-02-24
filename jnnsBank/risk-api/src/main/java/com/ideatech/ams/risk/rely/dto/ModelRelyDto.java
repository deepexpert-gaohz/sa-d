package com.ideatech.ams.risk.rely.dto;

import lombok.Data;

@Data
public class ModelRelyDto {
    private Long id;
    private String modelTable;//被依赖表
    private String relyTable;//依赖表
    private String modelTableCn;//被依赖表名
    private String relyTableCn;//依赖表名
}

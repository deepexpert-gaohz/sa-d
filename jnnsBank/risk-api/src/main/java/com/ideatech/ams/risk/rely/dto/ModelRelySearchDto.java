package com.ideatech.ams.risk.rely.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class ModelRelySearchDto extends PagingDto<ModelRelyDto> {
    private String modelTable;//被依赖表
    private String relyTable;//依赖表

}

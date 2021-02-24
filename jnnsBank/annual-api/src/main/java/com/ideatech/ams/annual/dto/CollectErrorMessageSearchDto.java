package com.ideatech.ams.annual.dto;

import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class CollectErrorMessageSearchDto extends PagingDto<CollectTaskErrorMessageDto> {
    /**
     * 查询关键字
     */
    private String key;
    /**
     * 机构名称
     */
    private String bankName;
    /**
     * 组织结构ID
     */
    private Long organizationId;
    /**
     * 人行机构号
     */
    private String pbcCode;
}

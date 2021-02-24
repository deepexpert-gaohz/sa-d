package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class SaicSearchHistorySearchDto extends PagingDto<SaicSearchHistoryDto> {
    /**
     * 查询关键字
     */
    private String key;
    /**
     * 其实日期
     */
    private String bigenDate;
    /**
     * 结束日期
     */
    private String endDate;
    private Long orgId;
}

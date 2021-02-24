package com.ideatech.ams.annual.dto;

import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class SaicCollectResultSearchDto extends PagingDto<FetchSaicInfoDto> {
    /**
     * 查询关键字
     */
    private String key;

    /*
     * 每条记录添加采集状态字段
     */
    private CollectState collectState;

    /*
     * 企业名称
     */
    private String customerName;
    /**
     * 经营状态
     */
    private String state;
}

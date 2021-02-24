package com.ideatech.ams.annual.dto;

import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class PbcCollectResultSearchDto extends PagingDto<PbcCollectAccountDto> {
    /**
     * 查询关键字
     */
    private String key;

    /*
     * 每条记录添加采集状态字段
     */
    private CollectState collectState;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 存款人名称
     */
    private String depositorName;
}
